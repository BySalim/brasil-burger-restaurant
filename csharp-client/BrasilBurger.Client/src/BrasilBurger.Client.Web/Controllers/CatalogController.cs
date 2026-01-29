using BrasilBurger.Client.Application.Abstractions.Persistence;
using BrasilBurger.Client.Web.EnumsUi.Mappings;
using BrasilBurger.Client.Web.FormatsApp;
using BrasilBurger.Client.Web.ViewModels.GetVm;
using BrasilBurger.Client.Web.ViewModels.MapperVm;
using BrasilBurger.Client.Web.ViewModels.Pages;
using BrasilBurger.Client.Web.ViewModels.Shared;
using BrasilBurger.Client.Web.EnumsUi.Extensions;
using BrasilBurger.Client.Web.EnumsUi.Options;
using BrasilBurger.Client.Web.ViewModels.PostVm.PaiementCommandePostVm;
using Microsoft.AspNetCore.Mvc;

namespace BrasilBurger.Client.Web.Controllers;

[Route("Catalog")]
public sealed class CatalogController : Controller
{
    private readonly IArticleRepository _articleRepository;
    private readonly ArticleMapperVm _articleMapperVm;
    private readonly IQuartierRepository _quartierRepository;
    private readonly IFormatsApp _formatsApp;

    public CatalogController(
        IArticleRepository articleRepository,
        ArticleMapperVm articleMapperVm,
        IQuartierRepository zoneRepository,
        IFormatsApp formatsApp)
    {
        _articleRepository = articleRepository ?? throw new ArgumentNullException(nameof(articleRepository));
        _articleMapperVm = articleMapperVm ?? throw new ArgumentNullException(nameof(articleMapperVm));
        _quartierRepository = zoneRepository ?? throw new ArgumentNullException(nameof(zoneRepository));
        _formatsApp = formatsApp ?? throw new ArgumentNullException(nameof(formatsApp));
    }

    [HttpGet("")]
    [HttpGet("index")]
    public async Task<IActionResult> Index(CancellationToken ct)
    {
        var articles = await _articleRepository.ListActifsAsync(ct);
        List<ArticleGetVm> articleGetVms = _articleMapperVm.ToGetVms(articles);

        // DeliveryZoneOptions avec formatage
        var quartiers = await _quartierRepository.ListWithZonesAsync(ct);

        // Modes de paiement
        var paymentMethodCards = new ChoiceCardsVm
        {
            Name = "Form.PaymentMethod",
            Legend = "Moyen de paiement",
            SelectedValue = ModePaiementUiExtensions.DefaultSelected().ToString(),
            Items = EnumUiMExtVmMapper.ToChoiceCardItems(ModePaiementUiExtensions.AllUi()),
            Layout = "stacked",
        };

        // Modes de récupération
        var retrievalMethodCards = new ChoiceCardsVm
        {
            Name = "Form.RetrievalMethod",
            Legend = "Mode de récupération",
            SelectedValue = ModeRecuperationUiExtensions.DefaultSelected().ToString(),
            Items = EnumUiMExtVmMapper.ToChoiceCardItems(ModeRecuperationUiExtensions.AllUi()),
        };

        // Template pour le formatage des quartiers
        const string template = "{quartier} ({zone}) - {price}";

        // IMPORTANT: Formater les options avec le format "zoneId|quartierId|prix"
        // pour que le JavaScript puisse parser correctement les data-attributes
        var deliveryQuartierOptions = quartiers.Select(q =>
        {
            var formattedText = _formatsApp.FormatQuartier(q, template);
            // Supposons que q a les propriétés: ZoneId, Id (quartierId), PrixLivraison
            // Adapter selon votre modèle de données réel
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

    [HttpPost("payer")]
    [ValidateAntiForgeryToken]
    public IActionResult Payer(CatalogVmFormVm vm)
    {
        if (!ModelState.IsValid)
        {
            // Pour l'instant, aucune logique métier réelle.
            // On se contente de revenir sur le catalogue.
            return RedirectToAction(nameof(Index));
        }

        // Ici, tu brancheras plus tard la vraie création de commande / paiement.
        return RedirectToAction(nameof(Index));
    }

}