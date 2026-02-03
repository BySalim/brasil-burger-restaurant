using BrasilBurger.Client.Web.ViewModels.Shared;

namespace BrasilBurger.Client.Web.ViewModels.Orders;

public sealed class OrderArticleVm
{
    public required int Id { get; init; }
    public required string Nom { get; init; }
    public string? ImagePublicId { get; init; }
    
    public required BadgeVm Categorie { get; init; }
    
    public required int PrixUnitaire { get; init; }
    public required int Quantite { get; init; }
    public int Total => PrixUnitaire * Quantite;
}