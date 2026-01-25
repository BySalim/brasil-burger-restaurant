using BrasilBurger.Client.Domain.Enums;
using BrasilBurger.Client.Web.EnumsUi.Helpers;
using BrasilBurger.Client.Web.EnumsUi.Options;

namespace BrasilBurger.Client.Web.EnumsUi.Extensions;

public static class CategorieArticleUiExtensions
{
    public static EnumUiMeta Ui(this CategorieArticle v) => v switch
    {
        CategorieArticle.BURGER     => new(
            nameof(CategorieArticle.BURGER),
            "Burger",     
            new UiIcon("lunch_dining"),   
            UiColor.Rouge),
        
        CategorieArticle.MENU       => new(
            nameof(CategorieArticle.MENU),
            "Menu",       
            new UiIcon("restaurant_menu"), 
            UiColor.Bleu),
        
        CategorieArticle.COMPLEMENT => new(
            nameof(CategorieArticle.COMPLEMENT),
            "Complément", 
            new UiIcon("add"),
            UiColor.Jaune),

        _ => throw new ArgumentOutOfRangeException(nameof(v), v, "CategorieArticle non mappée (UI).")
    };
    
    private static readonly EnumUiMeta[] Items = EnumUiHelper.All<CategorieArticle>(Ui);
    
    public static IEnumerable<EnumUiMeta> AllUi() => Items;
    
    public static CategorieArticle ToEnum(string value) => EnumUiHelper.ToEnum<CategorieArticle>(value);
    
    public static CategorieArticle DefaultSelected() => CategorieArticle.MENU;
}
