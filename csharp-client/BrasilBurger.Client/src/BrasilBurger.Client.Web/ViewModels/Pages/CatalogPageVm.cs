using BrasilBurger.Client.Domain.Enums;
using BrasilBurger.Client.Web.EnumsUi.Abstractions;
using BrasilBurger.Client.Web.ViewModels.GetVm;

namespace BrasilBurger.Client.Web.ViewModels.Pages;

public sealed record CatalogVm(
    IReadOnlyList<ArticleGetVm> Articles,
    IReadOnlyList<EnumUiItem<ModeRecuperation>> ModesRecuperation,
    IReadOnlyList<EnumUiItem<ModePaiement>> ModesPaiement,
    IReadOnlyList<ZoneGetVm> Zones
);
