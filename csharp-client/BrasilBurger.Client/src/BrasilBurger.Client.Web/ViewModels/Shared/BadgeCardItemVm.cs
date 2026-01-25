namespace BrasilBurger.Client.Web.ViewModels.Shared;

public class BadgeCardItemVm
{
    public required string Label { get; init; }

    public string? IconName { get; init; }
    public string? IconUrl { get; init; }
    
    public string? CssBadge { get; init; }
    public string? CssIcon { get; init; }
}
