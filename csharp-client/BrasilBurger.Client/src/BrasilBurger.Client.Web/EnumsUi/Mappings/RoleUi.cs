using BrasilBurger.Client.Domain.Enums;
using BrasilBurger.Client.Web.EnumsUi.Abstractions;
using BrasilBurger.Client.Web.EnumsUi.ColorUi;

namespace BrasilBurger.Client.Web.EnumsUi.Mappings;

public sealed class RoleUi : EnumUiBase<Role>
{
    public RoleUi() : base(new List<EnumUiItem<Role>>
    {
        new(Role.CLIENT, "Client", UiColor.Blue, "person"),
        new(Role.GESTIONNAIRE, "Gestionnaire", UiColor.Purple, "admin_panel_settings"),
    }.AsReadOnly())
    { }
}
