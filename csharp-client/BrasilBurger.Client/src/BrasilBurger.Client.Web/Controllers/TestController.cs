using Microsoft.AspNetCore.Mvc;
using BrasilBurger.Client.ViewModels.Auth;

namespace BrasilBurger.Client.Controllers;

[Route("test")]
public sealed class TestController : Controller
{
    // GET /test/home
    [HttpGet("home")]
    public IActionResult Home()
        => View("~/Views/Home/Index.cshtml");

    // GET /test/connexion
    [HttpGet("connexion")]
    public IActionResult Login()
    {
        ViewData["AuthNavbarRightText"] = "← Retour à l'accueil";
        ViewData["AuthNavbarRightHref"] = Url.Action("Home", "Test");

        return View("~/Views/Auth/Login.cshtml", new LoginVm());
    }

    // POST /test/connexion
    [HttpPost("connexion")]
    [ValidateAntiForgeryToken]
    public IActionResult Login(LoginVm vm)
    {
        ViewData["AuthNavbarRightText"] = "← Retour à l'accueil";
        ViewData["AuthNavbarRightHref"] = Url.Action("Home", "Test");

        if (!ModelState.IsValid)
        {
            return View("~/Views/Auth/Login.cshtml", vm);
        }

        // Simulation : pas de vraie authentification.
        return RedirectToAction(nameof(Home));
    }

    // GET /test/inscription
    [HttpGet("inscription")]
    public IActionResult Register()
    {
        ViewData["AuthNavbarRightText"] = "← Accueil";
        ViewData["AuthNavbarRightHref"] = Url.Action("Home", "Test");

        return View("~/Views/Auth/RegisterStep1.cshtml", new RegisterStep1Vm());
    }

    // POST /test/inscription
    [HttpPost("inscription")]
    [ValidateAntiForgeryToken]
    public IActionResult Register(RegisterStep1Vm vm)
    {
        ViewData["AuthNavbarRightText"] = "← Accueil";
        ViewData["AuthNavbarRightHref"] = Url.Action("Home", "Test");

        if (!ModelState.IsValid)
        {
            return View("~/Views/Auth/RegisterStep1.cshtml", vm);
        }

        // Simulation : on enchaîne sur l'étape 2 (sans persistance).
        return RedirectToAction(nameof(RegisterStep2));
    }

    // GET /test/inscription/etape-2
    [HttpGet("inscription/etape-2")]
    public IActionResult RegisterStep2()
    {
        ViewData["AuthNavbarRightText"] = "← Précédent";
        ViewData["AuthNavbarRightHref"] = Url.Action(nameof(Register), "Test");

        var vm = BuildRegisterStep2Vm();
        return View("~/Views/Auth/RegisterStep2.cshtml", vm);
    }

    // POST /test/inscription/etape-2
    [HttpPost("inscription/etape-2")]
    [ValidateAntiForgeryToken]
    public IActionResult RegisterStep2(RegisterStep2Vm vm)
    {
        ViewData["AuthNavbarRightText"] = "← Précédent";
        ViewData["AuthNavbarRightHref"] = Url.Action(nameof(Register), "Test");

        // Recharger les listes (mockées) pour réaffichage en cas d'erreur
        var rebuilt = BuildRegisterStep2Vm();
        vm.PaymentMethodOptions.AddRange(rebuilt.PaymentMethodOptions);
        vm.RetrievalMethodOptions.AddRange(rebuilt.RetrievalMethodOptions);
        vm.DeliveryZoneOptions.AddRange(rebuilt.DeliveryZoneOptions);

        if (!ModelState.IsValid)
        {
            return View("~/Views/Auth/RegisterStep2.cshtml", vm);
        }

        // Simulation : inscription terminée -> retour connexion (ou home).
        return RedirectToAction(nameof(Login));
    }

    private static RegisterStep2Vm BuildRegisterStep2Vm()
    {
        var vm = new RegisterStep2Vm
        {
            PaymentMethod = "wave",
            RetrievalMethod = "livraison"
        };

        vm.PaymentMethodOptions.AddRange(new[]
        {
            new SelectOptionVm("wave", "Wave"),
            new SelectOptionVm("om", "Orange Money"),
            new SelectOptionVm("cash", "Espèces"),
            new SelectOptionVm("card", "Carte Bancaire"),
        });

        vm.RetrievalMethodOptions.AddRange(new[]
        {
            new SelectOptionVm("livraison", "Se faire livrer"),
            new SelectOptionVm("sur_place", "Sur place"),
            new SelectOptionVm("emporter", "Venir récupérer (A emporter)"),
        });

        vm.DeliveryZoneOptions.AddRange(new[]
        {
            new SelectOptionVm("plateau", "Dakar Plateau"),
            new SelectOptionVm("mermoz", "Mermoz / Sacré-Cœur"),
            new SelectOptionVm("almadies", "Les Almadies"),
            new SelectOptionVm("yoff", "Yoff"),
            new SelectOptionVm("point_e", "Point E"),
            new SelectOptionVm("parcelles", "Parcelles Assainies"),
        });

        return vm;
    }
}
