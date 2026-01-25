namespace BrasilBurger.Client.Web.ViewModels.Shared;

using BrasilBurger.Client.Web.EnumsUi.Options;

public static class BadgeVariantCss
{
    public static string Css(this BadgeVariant v, UiColor c) => v switch
    {
        BadgeVariant.Soft     => c.BadgeSoft(),
        BadgeVariant.Solid    => c.BadgeSolid(),
        BadgeVariant.Outline  => c.BadgeOutline(),
        BadgeVariant.PillSoft => c.BadgePillSoft(),
        BadgeVariant.Compact  => c.BadgeCompact(),
        _ => UiColorCss.BadgeNone()
    };
}
