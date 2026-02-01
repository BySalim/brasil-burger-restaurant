namespace BrasilBurger.Client.Web.ViewModels.Shared;

public class PaginationVm
{
    public required SelectVm PerPageSelect { get; init; }
    public required int CurrentPage { get; set; }
    public required int TotalPages { get; set; }
    public required int TotalItems { get; set; }
    public required string ItemsName { get; init; } = "items";
    private string RcFirstValue => PerPageSelect.Options.Count > 0 ? PerPageSelect.Options[0].Value : "0";
    public int PerPage
    {
        get{
            var value = !string.IsNullOrWhiteSpace(PerPageSelect.SelectedValue)
                ? PerPageSelect.SelectedValue
                : RcFirstValue;
            return int.TryParse(value, out var tmp) ? tmp : 0;
        }
    }

    public int From => (CurrentPage - 1) * PerPage + 1;
    public int To => Math.Min(CurrentPage * PerPage, TotalItems);

    public bool HasPrevious => CurrentPage > 1;
    public bool HasNext => CurrentPage < TotalPages;
}