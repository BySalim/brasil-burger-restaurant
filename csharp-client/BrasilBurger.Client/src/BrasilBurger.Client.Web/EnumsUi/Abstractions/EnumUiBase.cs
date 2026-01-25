using BrasilBurger.Client.Web.EnumsUi.ColorUi;
using System.Globalization;
using Microsoft.AspNetCore.Mvc.Rendering;

namespace BrasilBurger.Client.Web.EnumsUi.Abstractions;

public abstract class EnumUiBase<TEnum> : IEnumUi<TEnum>
    where TEnum : struct, Enum
{
    private readonly IReadOnlyList<EnumUiItem<TEnum>> _items;
    private readonly IReadOnlyDictionary<TEnum, EnumUiItem<TEnum>> _map;

    protected EnumUiBase(IReadOnlyList<EnumUiItem<TEnum>> items)
    {
        _items = items ?? throw new ArgumentNullException(nameof(items));
        _map = _items.ToDictionary(i => i.Value);
    }

    public IReadOnlyList<EnumUiItem<TEnum>> All() => _items;

    public EnumUiItem<TEnum> Get(TEnum value)
        => _map.TryGetValue(value, out var item)
            ? item
            : new EnumUiItem<TEnum>(value, value.ToString(), UiColor.Gray);

    public List<SelectListItem> ToSelect(TEnum? selected = null)
    {
        string? selectedValue = selected.HasValue ? ToInvariantInt(selected.Value) : null;

        return _items.Select(i =>
        {
            var value = ToInvariantInt(i.Value);

            return new SelectListItem
            {
                Value = value,
                Text = i.Label,
                Selected = selectedValue != null && selectedValue == value
            };
        }).ToList();
    }

    private static string ToInvariantInt(TEnum value)
        => Convert.ToInt64(value, CultureInfo.InvariantCulture)
            .ToString(CultureInfo.InvariantCulture);
}
