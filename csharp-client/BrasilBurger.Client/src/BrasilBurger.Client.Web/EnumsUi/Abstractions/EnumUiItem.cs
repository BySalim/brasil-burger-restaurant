using BrasilBurger.Client.Web.EnumsUi.ColorUi;

namespace BrasilBurger.Client.Web.EnumsUi.Abstractions;

public sealed record EnumUiItem<TEnum>(
    TEnum Value,
    string Label,
    UiColor? Color = null,
    string? Icon = null
) where TEnum : struct, Enum
{
    public string Key => Value.ToString();
}
