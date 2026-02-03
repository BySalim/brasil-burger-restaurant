using BrasilBurger.Client.Web.ViewModels.Shared;

namespace BrasilBurger.Client.Web.ViewModels.Orders;

public sealed class OrderLivraisonVm
{
    // Prix de livraison
    public required int PrixLivraison { get; init; }
    
    // Adresse
    public required string Quartier { get; init; }
    public required string Zone { get; init; }
    public string? NoteLivraison { get; init; }
    
    // Statut (badge, peut être null si livraison pas encore assignée)
    public BadgeVm? StatutBadge { get; init; }
    
    // Livreur (peut être null si pas encore assigné)
    public OrderLivreurVm? Livreur { get; init; }
    
    // Helpers
    public bool HasStatut => StatutBadge != null;
    public bool HasLivreur => Livreur != null;
}

public sealed class OrderLivreurVm
{
    public required string Nom { get; init; }
    public required string Prenom { get; init; }
    public required string Telephone { get; init; }
    
    public string NomComplet => $"{Prenom} {Nom}";
    public string Initiales => $"{(Prenom.Length > 0 ? Prenom[0] : ' ')}{(Nom.Length > 0 ? Nom[0] : ' ')}".ToUpper();
}