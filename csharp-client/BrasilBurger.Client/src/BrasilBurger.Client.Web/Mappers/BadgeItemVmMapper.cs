using BrasilBurger.Client.Web.ViewModels.Shared;
using BrasilBurger.Client.Web.EnumsUi.Options;

namespace BrasilBurger.Client.Web.Mappers;

public static  class BadgeItemVmMapper
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
}
