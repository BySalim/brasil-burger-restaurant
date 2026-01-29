namespace BrasilBurger.Client.Web.ViewModels.Shared;

public sealed class ChoiceCardsVm
{
    public required string Name { get; init; }
    public string? Legend { get; init; }
    public string? SelectedValue { get; set; }
    public required IReadOnlyList<ChoiceCardItemVm> Items { get; init; }

    public string Layout { get; init; } = "grid";   // grid, inline, stacked
    public string Columns { get; init; } = "auto";  // auto, 2, 3, 4, 5
    public string Size { get; init; } = "xs";       // xs, sm, md
    public bool ShowIcons { get; init; } = true;
}
