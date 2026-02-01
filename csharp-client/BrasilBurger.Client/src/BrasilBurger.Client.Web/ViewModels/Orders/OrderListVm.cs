using BrasilBurger.Client.Web.ViewModels.Shared;


namespace BrasilBurger.Client.Web.ViewModels.Orders;


public class OrderListVm
{
    public required IReadOnlyList<OrderItemVm> Orders { get; init; }
    
    public required SelectVm OrderStatusSelect { get; init; }
    
    public required SelectVm RecoveryMode { get; init; }
    
    public required PaginationVm Pagination { get; init; }

    public string? SearchFilter { get; set; }
    public string? DateFilter { get; set; }
}