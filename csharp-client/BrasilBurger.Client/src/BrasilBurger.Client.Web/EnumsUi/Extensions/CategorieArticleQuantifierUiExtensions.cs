using System.Runtime.CompilerServices;
using System.Runtime.InteropServices.Swift;
using BrasilBurger.Client.Domain.Enums;
using BrasilBurger.Client.Web.EnumsUi.Options;
using BrasilBurger.Client.Web.EnumsUi.Helpers;

namespace BrasilBurger.Client.Web.EnumsUi.Extensions;

public static class CategorieArticleQuantifierUiExtensions
{
    public static EnumUiMeta Ui(this CategorieArticleQuantifier v) => v switch
    {
        CategorieArticleQuantifier.MENU     => new(
            nameof(CategorieArticleQuantifier.MENU),
            "Menu", 
            new UiIcon("lunch_dining"),
            UiColor.Bleu),
        
        CategorieArticleQuantifier.COMMANDE => new(
            nameof(CategorieArticleQuantifier.COMMANDE),
            "Commande", 
            new UiIcon("restaurant_menu"),
            UiColor.Violet),

        _ => throw new ArgumentOutOfRangeException(nameof(v), v, "CategorieArticleQuantifier non mappée (UI).")
    };
    
    private static readonly EnumUiMeta[] Items = EnumUiHelper.All<CategorieArticleQuantifier>(Ui);
    
    public static IEnumerable<EnumUiMeta> AllUi() => Items;
    
    public static CategorieArticleQuantifier ToEnum(string value) => EnumUiHelper.ToEnum<CategorieArticleQuantifier>(value);
    
    public static CategorieArticleQuantifier DefaultSelected() => CategorieArticleQuantifier.MENU;
}
