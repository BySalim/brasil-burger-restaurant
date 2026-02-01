using BrasilBurger.Client.Domain.Enums;
using BrasilBurger.Client.Web.EnumsUi.Options;
using BrasilBurger.Client.Web.EnumsUi.Helpers;

namespace BrasilBurger.Client.Web.EnumsUi.Extensions;

public static class ModePaiementUiExtensions
{
    public static EnumUiMeta Ui(this ModePaiement v) => v switch
    {
        ModePaiement.WAVE => new(
            nameof(ModePaiement.WAVE),
            "Wave",
            new UiIcon("account_balance_wallet"),
            UiColor.Vert),

        ModePaiement.OM => new(
            nameof(ModePaiement.OM),
            "Orange Money",
            new UiIcon("smartphone"),
            UiColor.Orange),

        _ => throw new ArgumentOutOfRangeException(nameof(v), v, "ModePaiement non mappé (UI).")
    };
    
    private static readonly EnumUiMeta[] Items = EnumUiHelper.All<ModePaiement>(Ui);
    
    public static IEnumerable<EnumUiMeta> AllUi() => Items;
    
    public static ModePaiement? ToEnum(string value) => EnumUiHelper.ToEnum<ModePaiement>(value);
    
    public static ModePaiement DefaultSelected() => ModePaiement.OM;
}
