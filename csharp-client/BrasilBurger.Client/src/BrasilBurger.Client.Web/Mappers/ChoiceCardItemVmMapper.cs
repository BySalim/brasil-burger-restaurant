using BrasilBurger.Client.Web.EnumsUi.Options;
using BrasilBurger.Client.Web.ViewModels.Shared;

namespace BrasilBurger.Client.Web.Mappers;

public static class ChoiceCardItemVmMapper
{
    public static ChoiceCardItemVm ToChoiceCardItem(this EnumUiMeta meta)
    {
        string? iconName = null;
        string? iconUrl = null;

        switch (meta.Visual)
        {
            case UiIcon icon:  iconName = icon.IconName; break;
            case UiImage img:  iconUrl  = img.Path;      break;
            default: throw new InvalidOperationException("UiVisual non supporté.");
        }

        return new ChoiceCardItemVm
        {
            Value = meta.Value,
            Label = meta.Label,
            IconName = iconName,
            IconUrl = iconUrl
        };
    }
    
    public static IReadOnlyList<ChoiceCardItemVm> ToChoiceCardItems(this IEnumerable<EnumUiMeta> metas)
        => metas.Select(m => m.ToChoiceCardItem()).ToList();
    
    public static ChoiceCardItemVm[] ToChoiceCardItemsArray(this IEnumerable<EnumUiMeta> metas)
        => metas.Select(m => m.ToChoiceCardItem()).ToArray();
}
