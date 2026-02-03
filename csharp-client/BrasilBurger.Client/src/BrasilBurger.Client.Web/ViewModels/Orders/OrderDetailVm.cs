using BrasilBurger.Client.Web.ViewModels.Shared;

namespace BrasilBurger.Client.Web.ViewModels.Orders;

public sealed class OrderDetailVm
{
    public required int Id { get; init; }
    public required string Code { get; init; }
    public required DateTime CreatedAt { get; init; }
    public required int Montant { get; init; }
    
    // Type de commande (catégorie du panier)
    public required BadgeVm Type { get; init; }
    
    // Statut actuel
    public required BadgeVm EtatBadge { get; init; }
    
    // Mode de récupération
    public required BadgeVm ModeRecuperationBadge { get; init; }
    
    // Tracking
    public required OrderTrackingVm Tracking { get; init; }
    
    // Articles
    public required IReadOnlyList<OrderArticleVm> Articles { get; init; }
    public int TotalArticles => Articles.Sum(a => a.Quantite);
    public int SousTotalArticles => Articles.Sum(a => a.Total);
    
    // Paiement (optionnel)
    public OrderPaiementVm? Paiement { get; init; }
    
    // Livraison (optionnel, uniquement si mode livraison)
    public OrderLivraisonVm? Livraison { get; init; }
    
    // Helpers
    public bool EstLivraison => Livraison != null;
    public bool EstAnnulee => Tracking.EstAnnulee;
    
    // Formatage
    public string FormattedDate => CreatedAt.ToString("dd MMM yyyy");
    public string FormattedTime => CreatedAt.ToString("HH:mm");
    public string FormattedDateTime => $"{FormattedDate} • {FormattedTime}";
}