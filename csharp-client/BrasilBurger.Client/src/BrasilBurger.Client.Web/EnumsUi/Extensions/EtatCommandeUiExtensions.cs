using BrasilBurger.Client.Domain.Enums;
using BrasilBurger.Client.Web.EnumsUi.Options;
using BrasilBurger.Client.Web.EnumsUi.Helpers;

namespace BrasilBurger.Client.Web.EnumsUi.Extensions;

public static class EtatCommandeUiExtensions
{
    public static EnumUiMeta Ui(this EtatCommande v) => v switch
    {
        EtatCommande.EN_ATTENTE => new(
            nameof(EtatCommande.EN_ATTENTE),
            "En attente",
            new UiIcon("schedule"),
            UiColor.Jaune),

        EtatCommande.EN_PREPARATION => new(
            nameof(EtatCommande.EN_PREPARATION),
            "En préparation",
            new UiIcon("kitchen"),
            UiColor.Bleu),

        EtatCommande.TERMINER => new(
            nameof(EtatCommande.TERMINER),
            "Terminée",
            new UiIcon("check_circle"),
            UiColor.Vert),

        EtatCommande.ANNULER => new(
            nameof(EtatCommande.ANNULER),
            "Annulée",
            new UiIcon("cancel"),
            UiColor.Rouge),

        _ => throw new ArgumentOutOfRangeException(nameof(v), v, "EtatCommande non mappé (UI).")
    };
    
    private static readonly EnumUiMeta[] Items = EnumUiHelper.All<EtatCommande>(Ui);
    
    public static IEnumerable<EnumUiMeta> AllUi() => Items;
    
    public static EtatCommande? ToEnum(string value) => EnumUiHelper.ToEnum<EtatCommande>(value);
    
    public static EtatCommande DefaultSelected() => EtatCommande.ANNULER;
}
