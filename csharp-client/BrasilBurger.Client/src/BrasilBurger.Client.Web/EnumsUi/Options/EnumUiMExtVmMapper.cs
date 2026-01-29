using BrasilBurger.Client.Web.ViewModels.Shared;

namespace BrasilBurger.Client.Web.EnumsUi.Options;

public static class EnumUiMExtVmMapper
{
    public static BadgeCardItemVm ToBadgeCardItemVm(this EnumUiMeta meta, BadgeVariant variant, bool withCssIcon = false)
    {
        var cssBadge = BadgeVariantCss.Css(variant, meta.Color);

        // Couleur icône
        var cssIcon = withCssIcon ? meta.Color.Icon() : null;
        
        string? iconName = null;
        string? iconUrl = null;

        switch (meta.Visual)
        {
            case UiIcon icon:
                iconName = icon.IconName;
                break;

            case UiImage img:
                iconUrl = img.Path;
                break;

            default:
                throw new InvalidOperationException("UiVisual non supporté.");
        }

        return new BadgeCardItemVm
        {
            Label = meta.Label,
            IconName = iconName,
            IconUrl = iconUrl,
            CssBadge = cssBadge,
            CssIcon = cssIcon,
        };
    }

    public static BadgeCardItemVm ToBadgeCardItemVmWithoutVisual(this EnumUiMeta meta, BadgeVariant variant)
    {
        var cssBadge = BadgeVariantCss.Css(variant, meta.Color);

        return new BadgeCardItemVm
        {
            Label = meta.Label,
            IconName = null,
            IconUrl = null,
            CssBadge = cssBadge,
            CssIcon = null,
        };
    }
    
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
