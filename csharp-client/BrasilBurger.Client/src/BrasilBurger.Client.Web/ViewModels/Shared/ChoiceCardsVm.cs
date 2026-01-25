namespace BrasilBurger.Client.Web.ViewModels.Shared;

public sealed class ChoiceCardsVm
{
    public required string Name { get; init; }
    public string? Legend { get; init; }
    public string? SelectedValue { get; set; }
    public required IReadOnlyList<ChoiceCardItemVm> Items { get; init; }
}
