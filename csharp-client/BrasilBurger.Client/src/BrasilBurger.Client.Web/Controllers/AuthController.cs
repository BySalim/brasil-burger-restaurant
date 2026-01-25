using Microsoft.AspNetCore.Mvc;
using BrasilBurger.Client.Web.ViewModels.Auth;
using BrasilBurger.Client.Application.Abstractions.Services;
using BrasilBurger.Client.Application.Dtos;
using BrasilBurger.Client.Application.Abstractions.Persistence;
using BrasilBurger.Client.Domain.Enums;
using BrasilBurger.Client.Web.Extensions;
using BrasilBurger.Client.Web.ViewModels.Shared;
using BrasilBurger.Client.Web.EnumsUi.Helpers;
using BrasilBurger.Client.Web.Mappers;
using System.Security.Claims;
using Microsoft.AspNetCore.Authentication;
using System.Text.Json;
using BrasilBurger.Client.Web.EnumsUi.Extensions;
using BrasilBurger.Client.Web.FormatsApp;

namespace BrasilBurger.Client.Web.Controllers;

[Route("Auth")]
public sealed class AuthController : Controller
{
    private readonly IAuthService _authService;
    private readonly IUtilisateurRepository _utilisateurRepo;
    private readonly IQuartierRepository _quartierRepo;
    private readonly IFormatsApp _formatsApp;

    public AuthController(
        IAuthService authService,
        IUtilisateurRepository utilisateurRepo,
        IQuartierRepository quartierRepo,
        IFormatsApp formatsApp)
    {
        _authService = authService;
        _utilisateurRepo = utilisateurRepo;
        _quartierRepo = quartierRepo;
        _formatsApp = formatsApp;
    }

    [HttpGet("")]
    [HttpGet("Login")]
    public IActionResult Login()
    {
        ViewData["AuthNavbarRightText"] = "← Retour à l'accueil";
        ViewData["AuthNavbarRightHref"] = Url.Action("Index", "Home");
        return View("~/Views/Auth/Login.cshtml", new LoginVm());
    }

    [HttpPost("Login")]
    [ValidateAntiForgeryToken]
    public async Task<IActionResult> Login(
        LoginVm vm,
        string? returnUrl,
        CancellationToken ct)
    {
        ViewData["AuthNavbarRightText"] = "← Retour à l'accueil";
        ViewData["AuthNavbarRightHref"] = Url.Action("Index", "Home");

        if (!ModelState.IsValid)
        {
            return View("~/Views/Auth/Login.cshtml", vm);
        }

        // Authentification
        var user = await _authService.AuthenticateAsync(vm.LoginEmail, vm.Password, ct);

        if (user is null || user.Role != Role.CLIENT)
        {
            ModelState.AddModelError(string.Empty, "Identifiants invalides.");
            return View("~/Views/Auth/Login.cshtml", vm);
        }

        // Création des claims
        await SignInUserAsync(user, vm.RememberMe);
        
        this.AddSuccessMessage($"Inscription réussie ! Bienvenue chez {_formatsApp.GetAppName()}, {user.Prenom} !");

        // Redirection
        if (!string.IsNullOrWhiteSpace(returnUrl) && Url.IsLocalUrl(returnUrl))
            return Redirect(returnUrl);

        return RedirectToAction("Index", "Catalog");
    }


    [HttpGet("Register")]
    public IActionResult Register()
    {
        ViewData["AuthNavbarRightText"] = "← Accueil";
        ViewData["AuthNavbarRightHref"] = Url.Action("Index", "Home");
        
        // Si par exemple on revient en arrière depuis l'étape 2
        if (TempData.Peek("RegisterStep1") is string json)
        {
            var vm = JsonSerializer.Deserialize<RegisterStep1Vm>(json) ?? new RegisterStep1Vm();
            
            TempData.Keep("RegisterStep1");

            return View("~/Views/Auth/RegisterStep1.cshtml", vm);
        }
        
        return View("~/Views/Auth/RegisterStep1.cshtml", new RegisterStep1Vm());
    }

    // Register - Step 1
    [HttpPost(template: "Register")]
    [ValidateAntiForgeryToken]
    public async Task<IActionResult> Register(RegisterStep1Vm vm, CancellationToken ct)
    {
        ViewData["AuthNavbarRightText"] = "← Accueil";
        ViewData["AuthNavbarRightHref"] = Url.Action("Index", "Home");

        if (!ModelState.IsValid)
            return View("~/Views/Auth/RegisterStep1.cshtml", vm);


        var existing = await _utilisateurRepo.GetByLoginAsync(vm.Email, ct);
        if (existing is not null)
        {
            ModelState.AddModelError(nameof(vm.Email), "Cet email est déjà utilisé.");
            return View("~/Views/Auth/RegisterStep1.cshtml", vm);
        }

        TempData["RegisterStep1"] = JsonSerializer.Serialize(vm);
        TempData.Remove("RegisterStep2");
        
        return RedirectToAction("RegisterStep2");
    }



    // Register - Step 2
    [HttpGet("Register/step-2")]
    public async Task<IActionResult> RegisterStep2(CancellationToken ct)
    {
        ViewData["AuthNavbarRightText"] = "← Précédent";
        ViewData["AuthNavbarRightHref"] = Url.Action(nameof(Register));

        // Vérifier que Step1 existe
        if (!TempData.ContainsKey("RegisterStep1"))
        {
            this.AddErrorMessage("Session expirée. Veuillez recommencer.");
            return RedirectToAction(nameof(Register));
        }

        var vm = await BuildRegisterStep2VmAsync(ct);
        return View("~/Views/Auth/RegisterStep2.cshtml", vm);
    }

    [HttpPost("Register/step-2")]
    [ValidateAntiForgeryToken]
    public async Task<IActionResult> RegisterStep2(
        RegisterStep2FormVm form,
        CancellationToken ct)
    {
        ViewData["AuthNavbarRightText"] = "← Précédent";
        ViewData["AuthNavbarRightHref"] = Url.Action(nameof(Register));

        if (!ModelState.IsValid)
        {
            // Reconstruction pour l'affichage avec erreurs
            var vm = await BuildRegisterStep2VmAsync(ct);
            vm.Form = form;
            vm.PaymentMethodCards.SelectedValue = form.PaymentMethod;
            vm.RetrievalMethodCards.SelectedValue = form.RetrievalMethod;

            return View("~/Views/Auth/RegisterStep2.cshtml", vm);
        }

        // Récupération des données Step1
        if (!TempData.TryGetValue("RegisterStep1", out var step1Json) || step1Json is not string json)
        {
            this.AddErrorMessage("Session expirée. Veuillez recommencer.");
            return RedirectToAction(nameof(Register));
        }
        TempData["RegisterStep1"] = json;


        var step1 = JsonSerializer.Deserialize<RegisterStep1Vm>(json);
        if (step1 is null)
        {
            this.AddErrorMessage("Erreur lors de la récupération des données.");
            return RedirectToAction(nameof(Register));
        }

        var dto = new RegisterClientDto
        {
            Nom = step1.Nom!,
            Prenom = step1.Prenom!,
            Email = step1.Email!,
            Password = step1.Password!,
            Telephone = form.Phone!,
            QuartierId = string.IsNullOrWhiteSpace(form.DeliveryQuartierId)
                ? null
                : int.TryParse(form.DeliveryQuartierId, out var qId)
                    ? qId
                    : null,
            ModePaiement = form.PaymentMethod,
            ModeRecuperation = form.RetrievalMethod
        };

        // Inscription via le service
        var result = await _authService.RegisterClientAsync(dto, ct);

        if (!result.IsSuccess)
        {
            ModelState.AddModelError(string.Empty, result.ErrorMessage!);
            
            var vm = await BuildRegisterStep2VmAsync(ct);
            vm.Form = form;
            vm.PaymentMethodCards.SelectedValue = form.PaymentMethod;
            vm.RetrievalMethodCards.SelectedValue = form.RetrievalMethod;

            return View("~/Views/Auth/RegisterStep2.cshtml", vm);
        }

        // Connexion
        await SignInUserAsync(result.Value!, isPersistent: false);

        this.AddSuccessMessage(
            $"Inscription réussie ! Bienvenue chez {_formatsApp.GetAppName()}, {result.Value!.Prenom} !");
        
        TempData.Remove("RegisterStep1");
        TempData.Remove("RegisterStep2");

        return RedirectToAction("Index", "Catalog");
    }

    // Logout
    [HttpPost("Logout")]
    [ValidateAntiForgeryToken]
    public async Task<IActionResult> Logout()
    {
        await HttpContext.SignOutAsync("Cookies");
        return RedirectToAction("Index", "Home");
    }


    private async Task SignInUserAsync(Domain.Entities.Utilisateur user, bool isPersistent)
    {
        var claims = new List<Claim>
        {
            new("uid", user.Id.ToString()),
            new(ClaimTypes.NameIdentifier, user.Id.ToString()),
            new("login", user.Login),
            new(ClaimTypes.Name, user.Login),
            new(ClaimTypes.Name, $"{user.Prenom} {user.Nom}"),
            new("role", user.Role.ToString())
        };

        var identity = new ClaimsIdentity(claims, "Cookies");
        var principal = new ClaimsPrincipal(identity);
        var props = new AuthenticationProperties { IsPersistent = isPersistent };

        await HttpContext.SignInAsync("Cookies", principal, props);
    }

    private async Task<RegisterStep2Vm> BuildRegisterStep2VmAsync(CancellationToken ct)
    {
        // Modes de paiement
        var paymentMethodCards = new ChoiceCardsVm
        {
            Name = "Form.PaymentMethod",
            Legend = "Moyen de paiement par défaut",
            SelectedValue = ModePaiementUiExtensions.DefaultSelected().ToString(),
            Items = ChoiceCardItemVmMapper.ToChoiceCardItems(ModePaiementUiExtensions.AllUi())
        };

        // Modes de récupération
        var retrievalMethodCards = new ChoiceCardsVm
        {
            Name = "Form.RetrievalMethod",
            Legend = "Mode de récupération préféré",
            SelectedValue = ModeRecuperationUiExtensions.DefaultSelected().ToString(),
            Items = ChoiceCardItemVmMapper.ToChoiceCardItems(ModeRecuperationUiExtensions.AllUi())
        };

        // Quartiers avec formatage
        var quartiers = await _quartierRepo.ListWithZonesAsync(ct);

        // Template pour le formatage des quartiers
        const string template = "{quartier} ({zone})";

        var deliveryQuartierOptions = _formatsApp.FormatQuartiers(quartiers, template).ToList();

        var vm = new RegisterStep2Vm(
            paymentMethodCards,
            retrievalMethodCards,
            deliveryQuartierOptions
        );


        return vm;
    }
}
