using BrasilBurger.Client.Domain.Enums;
using BrasilBurger.Client.Web.EnumsUi.Abstractions;
using BrasilBurger.Client.Web.EnumsUi.ColorUi;

namespace BrasilBurger.Client.Web.EnumsUi.Mappings;

public sealed class CategorieArticleQuantifierUi : EnumUiBase<CategorieArticleQuantifier>
{
    public CategorieArticleQuantifierUi() : base(new List<EnumUiItem<CategorieArticleQuantifier>>
    {
        new(CategorieArticleQuantifier.MENU, "Composition de menu", UiColor.Yellow, "restaurant_menu"),
        new(CategorieArticleQuantifier.COMMANDE, "Article de commande", UiColor.Blue, "shopping_cart"),
    }.AsReadOnly())
    { }
}
