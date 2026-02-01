using BrasilBurger.Client.Web.ViewModels.Shared;

namespace BrasilBurger.Client.Web.ViewModels.Orders;

public class OrderItemVm
{
    public int Id { get; init; }
    public required string Code { get; init; }
    public required DateTime CreatedAt { get; init; }
    
    public required BadgeVm Type { get; init; }
    public required int TotalAmount { get; init; }
    public required BadgeVm Status { get; init; }
    public required BadgeVm PickupMode { get; init; }
    public required BadgeVm? DeliveryStatus { get; init; }
    public string FormattedDate => CreatedAt.ToString("dd/MM/yyyy");
    public string FormattedTime => CreatedAt.ToString("HH:mm");
}