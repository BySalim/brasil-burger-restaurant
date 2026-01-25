namespace BrasilBurger.Client.Web.ViewModels.Shared;

public sealed class ChoiceCardItemVm
{
    public required string Value { get; init; }
    public required string Label { get; init; }
    
    public string? IconUrl { get; init; }
    public string? IconName { get; init; }
}
