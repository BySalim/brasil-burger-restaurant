using System;
using System.Collections.Generic;
using System.Threading;
using System.Threading.Tasks;
using BrasilBurger.Client.Application.Abstractions.Persistence;
using BrasilBurger.Client.Web.EnumsUi.Abstractions;
using BrasilBurger.Client.Web.EnumsUi.Mappings;
using BrasilBurger.Client.Web.ViewModels.GetVm;
using BrasilBurger.Client.Web.ViewModels.Mapper;
using BrasilBurger.Client.Web.ViewModels.Pages;
using BrasilBurger.Client.Web.ViewModels.PostVm.PaiementCommandePostVm;
using Microsoft.AspNetCore.Mvc;

namespace BrasilBurger.Client.Web.Controllers;

[Route("Catalog")]
public sealed class CatalogController : Controller
{
    private readonly IArticleRepository _articleRepository;
    private readonly ArticleMapperVm _articleMapperVm;
    private readonly IZoneRepository _zoneRepository;
    private readonly ZoneMapperVm _zoneMapperVm;

    public CatalogController(
        IArticleRepository articleRepository,
        ArticleMapperVm articleMapperVm,
        IZoneRepository zoneRepository,
        ZoneMapperVm zoneMapperVm)
    {
        _articleRepository = articleRepository ?? throw new ArgumentNullException(nameof(articleRepository));
        _articleMapperVm = articleMapperVm ?? throw new ArgumentNullException(nameof(articleMapperVm));
        _zoneRepository = zoneRepository ?? throw new ArgumentNullException(nameof(zoneRepository));
        _zoneMapperVm = zoneMapperVm ?? throw new ArgumentNullException(nameof(zoneMapperVm));
    }

    [HttpGet("")]
    [HttpGet("index")]
    public async Task<IActionResult> Index(CancellationToken cancellationToken)
    {
        // 1) Récupération de tous les articles actifs
        var articles = await _articleRepository.ListActifsAsync(cancellationToken);

        // 2) Mapping Domain -> VM
        IReadOnlyList<ArticleGetVm> articleGetVms = _articleMapperVm.ToGetVms(articles);

        // 3) Récupération de toutes les zones actives
        var zones = await _zoneRepository.ListActivesAsync(cancellationToken);

        // 4) Mapping Domain -> VM
        IReadOnlyList<ZoneGetVm> zoneGetVms = _zoneMapperVm.ToGetVms(zones);

        var vm = new CatalogVm(
            Articles: articleGetVms,
            ModesRecuperation: new ModeRecuperationUi().All(),
            ModesPaiement: new ModePaiementUi().All(),
            Zones: zoneGetVms
        );

        // 5) Passage à la vue Catalog/Index.cshtml
        return View(vm);
    }

    [HttpPost("payer")]
    [ValidateAntiForgeryToken]
    public IActionResult Payer(PaiementCommandePostVm vm)
    {
        if (!ModelState.IsValid)
        {
            // Pour l’instant, aucune logique métier réelle.
            // On se contente de revenir sur le catalogue.
            return RedirectToAction(nameof(Index));
        }

        // Ici, tu brancheras plus tard la vraie création de commande / paiement.
        return RedirectToAction(nameof(Index));
    }

}
