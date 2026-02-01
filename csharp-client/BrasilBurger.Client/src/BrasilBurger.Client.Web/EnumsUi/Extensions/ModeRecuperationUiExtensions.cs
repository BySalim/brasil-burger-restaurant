using BrasilBurger.Client.Domain.Enums;
using BrasilBurger.Client.Web.EnumsUi.Options;
using BrasilBurger.Client.Web.EnumsUi.Helpers;

namespace BrasilBurger.Client.Web.EnumsUi.Extensions;

public static class ModeRecuperationUiExtensions
{
    public static EnumUiMeta Ui(this ModeRecuperation v) => v switch
    {
        ModeRecuperation.SUR_PLACE => new(
            nameof(ModeRecuperation.SUR_PLACE),
            "Sur place",
            new UiIcon("storefront"),
            UiColor.Bleu),

        ModeRecuperation.EMPORTER => new(
            nameof(ModeRecuperation.EMPORTER),
            "À emporter",
            new UiIcon("shopping_bag"),
            UiColor.Indigo),

        ModeRecuperation.LIVRER => new(
            nameof(ModeRecuperation.LIVRER),
            "Livraison",
            new UiIcon("local_shipping"),
            UiColor.Violet),

        _ => throw new ArgumentOutOfRangeException(nameof(v), v, "ModeRecuperation non mappé (UI).")
    };
    
    private static readonly EnumUiMeta[] Items = EnumUiHelper.All<ModeRecuperation>(Ui);
    
    public static IEnumerable<EnumUiMeta> AllUi() => Items;
    
    public static ModeRecuperation? ToEnum(string value) => EnumUiHelper.ToEnum<ModeRecuperation>(value);
    
    public static ModeRecuperation DefaultSelected() => ModeRecuperation.EMPORTER;
}
