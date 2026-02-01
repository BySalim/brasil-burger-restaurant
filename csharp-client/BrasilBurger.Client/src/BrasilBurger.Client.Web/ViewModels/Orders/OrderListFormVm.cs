namespace BrasilBurger.Client.Web.ViewModels.Orders;

public sealed class OrderFiltersFormVm
{
    public string? Search { get; set; }
    public string? Date { get; set; }
    public string? OrderStatus { get; set; }
    public string? PickupMode { get; set; }
    public int? PerPage { get; set; }
    public int? Page { get; set; }
}