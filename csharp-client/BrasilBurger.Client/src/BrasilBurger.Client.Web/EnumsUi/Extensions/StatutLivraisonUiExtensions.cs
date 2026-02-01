using BrasilBurger.Client.Domain.Enums;
using BrasilBurger.Client.Web.EnumsUi.Options;
using BrasilBurger.Client.Web.EnumsUi.Helpers;

namespace BrasilBurger.Client.Web.EnumsUi.Extensions;

public static class StatutLivraisonUiExtensions
{
    public static EnumUiMeta Ui(this StatutLivraison v) => v switch
    {
        StatutLivraison.EN_COURS => new(
            nameof(StatutLivraison.EN_COURS),
            "En cours",
            new UiIcon("route"),
            UiColor.Bleu),

        StatutLivraison.TERMINER => new(
            nameof(StatutLivraison.TERMINER),
            "Terminée",
            new UiIcon("check_circle"),
            UiColor.Vert),

        _ => throw new ArgumentOutOfRangeException(nameof(v), v, "StatutLivraison non mappé (UI).")
    };
    
    private static readonly EnumUiMeta[] Items = EnumUiHelper.All<StatutLivraison>(Ui);
    
    public static IEnumerable<EnumUiMeta> AllUi() => Items;
    
    public static StatutLivraison? ToEnum(string value) => EnumUiHelper.ToEnum<StatutLivraison>(value);
    
    public static StatutLivraison DefaultSelected() => StatutLivraison.EN_COURS;
}
