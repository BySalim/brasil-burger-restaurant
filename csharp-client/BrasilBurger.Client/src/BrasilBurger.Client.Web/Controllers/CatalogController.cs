using BrasilBurger.Client.Application.Abstractions.Persistence;
using BrasilBurger.Client.web.Mappers;
using BrasilBurger.Client.Web.FormatsApp;
using BrasilBurger.Client.Web.ViewModels.GetVm;
using BrasilBurger.Client.Web.ViewModels.MapperVm;
using BrasilBurger.Client.Web.ViewModels.Pages;
using BrasilBurger.Client.Web.ViewModels.Shared;
using BrasilBurger.Client.Web.EnumsUi.Extensions;
using BrasilBurger.Client.Web.EnumsUi.Options;
using BrasilBurger.Client.Web.Extensions;
using BrasilBurger.Client.Application.Abstractions.Services;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Authorization;
using BrasilBurger.Client.Application.Abstractions.Security;
namespace BrasilBurger.Client.Web.Controllers;

[Route("Catalog")]
public sealed class CatalogController : Controller
{
    private readonly IUnitOfWork _db;
    private readonly ArticleMapperVm _articleMapperVm;
    private readonly IFormatsApp _formatsApp;
    private readonly IPaiementService _paiementService;
    private readonly IUserSession _session;
    private readonly ICommandeService _commandeService;

    public CatalogController(
        IUnitOfWork unitOfWork,
        ArticleMapperVm articleMapperVm,
        IFormatsApp formatsApp,
        IPaiementService paiementService,
        IUserSession session,
        ICommandeService commandeService)
    {
        _db = unitOfWork ?? throw new ArgumentNullException(nameof(unitOfWork));
        _articleMapperVm = articleMapperVm ?? throw new ArgumentNullException(nameof(articleMapperVm));
        _formatsApp = formatsApp ?? throw new ArgumentNullException(nameof(formatsApp));
        _paiementService = paiementService ?? throw new ArgumentNullException(nameof(paiementService));
        _session = session ?? throw new ArgumentNullException(nameof(session));
        _commandeService = commandeService ?? throw new ArgumentNullException(nameof(commandeService));
    }

    [HttpGet("")]
    [HttpGet("index")]
    public async Task<IActionResult> Index(string? paymentMethodSelected = null, string? deliveryZoneSelected = null, CancellationToken ct = default)
    {
        var articles = await _db.Articles.ListActifsAsync(ct);
        List<ArticleGetVm> articleGetVms = _articleMapperVm.ToGetVms(articles);

        // DeliveryZoneOptions avec formatage
        var quartiers = await _db.Quartiers.ListWithZonesAsync(ct);

        // Modes de paiement
        var paymentMethodCards = new ChoiceCardsVm
        {
            Name = "PaymentMethod",
            Legend = "Moyen de paiement",
            SelectedValue = string.IsNullOrWhiteSpace(paymentMethodSelected) ? ModePaiementUiExtensions.DefaultSelected().ToString() : paymentMethodSelected,
            Items = EnumUiMExtVmMapper.ToChoiceCardItems(ModePaiementUiExtensions.AllUi()),
            Layout = "stacked",
        };

        // Modes de récupération
        var retrievalMethodCards = new ChoiceCardsVm
        {
            Name = "RetrievalMethod",
            Legend = "Mode de récupération",
            SelectedValue = string.IsNullOrWhiteSpace(deliveryZoneSelected) ? ModeRecuperationUiExtensions.DefaultSelected().ToString() : deliveryZoneSelected,
            Items = EnumUiMExtVmMapper.ToChoiceCardItems(ModeRecuperationUiExtensions.AllUi()),
        };

        // Template pour le formatage des quartiers
        const string template = "{quartier} ({zone}) - {price}";

        // Options avec le format "zoneId|quartierId|prix" pour que le JS  puisse parser correctement les data-attributes
        var deliveryQuartierOptions = quartiers.Select(q =>
        {
            var formattedText = _formatsApp.FormatQuartier(q, template);
            var value = $"{q.ZoneId}|{q.Id}|{q.Zone.PrixLivraison}";
            return new SelectOptionVm(value, formattedText);
        }).ToList();

        var vm = new CatalogVm(
            articles: articleGetVms,
            paymentMethodCards: paymentMethodCards,
            retrievalMethodCards: retrievalMethodCards,
            deliveryZoneOptions: deliveryQuartierOptions
        );

        return View(vm);
    }

    [HttpPost("PayerCommande")]
    [Authorize]
    [ValidateAntiForgeryToken]
    public async Task<IActionResult> PayerCommande(CatalogVmFormVm vm, CancellationToken ct)
    {
        var paymentMethodSelected = vm.PaymentMethod;
        var deliveryZoneSelected = vm.RetrievalMethod;
        if (!ModelState.IsValid)
        {
            this.AddErrorMessage("Une erreur est survenue. Veuillez réessayer.");
            return RedirectToAction(nameof(Index), new { paymentMethodSelected, deliveryZoneSelected });
        }

        var commandeDto = CommandeMapper.ToDto(vm, _session.UserId!.Value);
        var cmdResult = _commandeService.NewCommande(commandeDto, ct).GetAwaiter().GetResult();
        if (!cmdResult.IsSuccess)
        {
            this.AddErrorMessage(cmdResult.ErrorMessage ?? "Une erreur est survenue lors de la commande.\nVeuillez réessayer.");
            return RedirectToAction(nameof(Index), new { paymentMethodSelected, deliveryZoneSelected });
        }
        var commande = cmdResult.Value; 

        var paiementResult = _paiementService.NewPaiement(commande!, vm.PaymentMethod!, ct).GetAwaiter().GetResult();
        if (!paiementResult.IsSuccess)
        {
            this.AddErrorMessage(paiementResult.ErrorMessage ?? "Une erreur est survenue lors du paiement.\nVeuillez réessayer.");
            return RedirectToAction(nameof(Index), new { paymentMethodSelected, deliveryZoneSelected });
        }
        
        var paiement = paiementResult.Value;
        await _db.Paiements.AddAsync(paiement!, ct);
        await _db.SaveChangesAsync(ct);
        this.AddSuccessMessage("Paiement effectué avec succès. \nCommande enregistrée avec succès.");

        TempData["ClearCartStorage"] = true;
        
        return RedirectToAction(nameof(Index));
    }
}

