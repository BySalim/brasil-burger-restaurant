using BrasilBurger.Client.Domain.Enums;
using BrasilBurger.Client.Web.EnumsUi.Options;
using BrasilBurger.Client.Web.EnumsUi.Helpers;

namespace BrasilBurger.Client.Web.EnumsUi.Extensions;

public static class RoleUiExtensions
{
    public static EnumUiMeta Ui(this Role v) => v switch
    {
        Role.CLIENT => new(
            nameof(Role.CLIENT),
            "Client",
            new UiIcon("person"),
            UiColor.Bleu),

        Role.GESTIONNAIRE => new(
            nameof(Role.GESTIONNAIRE),
            "Gestionnaire",
            new UiIcon("manage_accounts"),
            UiColor.Violet),

        _ => throw new ArgumentOutOfRangeException(nameof(v), v, "Role non mappé (UI).")
    };
    
    private static readonly EnumUiMeta[] Items = EnumUiHelper.All<Role>(Ui);
    
    public static IEnumerable<EnumUiMeta> AllUi() => Items;
    
    public static Role ToEnum(string value) => EnumUiHelper.ToEnum<Role>(value);
    
    public static Role DefaultSelected() => Role.CLIENT;
}
