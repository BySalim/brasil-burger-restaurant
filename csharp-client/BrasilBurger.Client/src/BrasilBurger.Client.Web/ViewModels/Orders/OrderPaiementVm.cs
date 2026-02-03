using BrasilBurger.Client.Web.ViewModels.Shared;

namespace BrasilBurger.Client.Web.ViewModels.Orders;

public sealed class OrderPaiementVm
{
    public required int Montant { get; init; }
    public required BadgeVm MethodeBadge { get; init; }
    public required DateTime DatePaiement { get; init; }
    public string? Reference { get; init; }
    
    public string FormattedDate => DatePaiement.ToString("dd/MM/yy • HH:mm");
}