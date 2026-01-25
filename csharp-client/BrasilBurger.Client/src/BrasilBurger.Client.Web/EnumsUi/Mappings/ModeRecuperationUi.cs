using BrasilBurger.Client.Domain.Enums;
using BrasilBurger.Client.Web.EnumsUi.Abstractions;
using BrasilBurger.Client.Web.EnumsUi.ColorUi;

namespace BrasilBurger.Client.Web.EnumsUi.Mappings;

public sealed class ModeRecuperationUi : EnumUiBase<ModeRecuperation>
{
    public ModeRecuperationUi() : base(new List<EnumUiItem<ModeRecuperation>>
    {
        new(ModeRecuperation.SUR_PLACE, "Sur place", UiColor.Purple, "restaurant"),
        new(ModeRecuperation.EMPORTER, "À emporter", UiColor.Blue, "shopping_bag"),
        new(ModeRecuperation.LIVRER, "Livraison", UiColor.Orange, "two_wheeler"),
    }.AsReadOnly())
    { }
}
