using BrasilBurger.Client.Domain.Enums;
using BrasilBurger.Client.Web.EnumsUi.Abstractions;
using BrasilBurger.Client.Web.EnumsUi.ColorUi;

namespace BrasilBurger.Client.Web.EnumsUi.Mappings;

public sealed class CategorieArticleUi : EnumUiBase<CategorieArticle>
{
    public CategorieArticleUi() : base(new List<EnumUiItem<CategorieArticle>>
    {
        new(CategorieArticle.BURGER, "Burger", UiColor.Blue, "lunch_dining"),
        new(CategorieArticle.MENU, "Menu", UiColor.Yellow, "restaurant_menu"),
        new(CategorieArticle.COMPLEMENT, "Complément", UiColor.Gray, "fastfood"),
    }.AsReadOnly())
    { }
}
