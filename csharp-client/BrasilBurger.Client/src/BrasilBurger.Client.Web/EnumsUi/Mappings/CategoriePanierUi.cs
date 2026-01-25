using BrasilBurger.Client.Domain.Enums;
using BrasilBurger.Client.Web.EnumsUi.Abstractions;
using BrasilBurger.Client.Web.EnumsUi.ColorUi;

namespace BrasilBurger.Client.Web.EnumsUi.Mappings;

public sealed class CategoriePanierUi : EnumUiBase<CategoriePanier>
{
    public CategoriePanierUi() : base(new List<EnumUiItem<CategoriePanier>>
    {
        new(CategoriePanier.BURGER, "Burger simple", UiColor.Blue, "lunch_dining"),
        new(CategoriePanier.MENU, "Menu", UiColor.Yellow, "restaurant_menu"),
    }.AsReadOnly())
    { }
}
