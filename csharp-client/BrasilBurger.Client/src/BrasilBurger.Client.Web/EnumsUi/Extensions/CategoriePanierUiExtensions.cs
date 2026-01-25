using BrasilBurger.Client.Domain.Enums;
using BrasilBurger.Client.Web.EnumsUi.Options;
using BrasilBurger.Client.Web.EnumsUi.Helpers;

namespace BrasilBurger.Client.Web.EnumsUi.Extensions;

public static class CategoriePanierUiExtensions
{
    public static EnumUiMeta Ui(this CategoriePanier v) => v switch
    {
        CategoriePanier.BURGER => new(
            nameof(CategoriePanier.BURGER),
            "Burger", 
            new UiIcon("restaurant_menu"), 
            UiColor.Rouge),
        
        CategoriePanier.MENU   => new(
            nameof(CategoriePanier.MENU),
            "Menu",
            new UiIcon("receipt_long"),
            UiColor.Bleu),

        _ => throw new ArgumentOutOfRangeException(nameof(v), v, "CategoriePanier non mappée (UI).")
    };
    
    private static readonly EnumUiMeta[] Items = EnumUiHelper.All<CategoriePanier>(Ui);
    
    public static IEnumerable<EnumUiMeta> AllUi() => Items;
    
    public static CategoriePanier ToEnum(string value) => EnumUiHelper.ToEnum<CategoriePanier>(value);
    
    public static CategoriePanier DefaultSelected() => CategoriePanier.BURGER;
}
