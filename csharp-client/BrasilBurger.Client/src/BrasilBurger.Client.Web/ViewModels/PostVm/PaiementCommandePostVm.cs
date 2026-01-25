using System.ComponentModel.DataAnnotations;
using BrasilBurger.Client.Domain.Enums;
using BrasilBurger.Client.Domain.Entities;

namespace BrasilBurger.Client.Web.ViewModels.PostVm.PaiementCommandePostVm;

public sealed class ArticleQuantifierPostVm
{
    [Required]
    public int ArticleId { get; set; }

    [Range(1, 999)]
    public int Quantite { get; set; }
}
public sealed class InfoLivraisonPostVm
{
    [Required]
    public int idQuartier { get; set; }
    public string? NoteLivraison { get; set; } = null;
}

public sealed class PaiementCommandePostVm
{
    // Étape 1 : contenu du panier
    [MinLength(1)]
    public List<ArticleQuantifierPostVm> ArticleQuantifiers { get; set; } = new();

    // Étape 2 : récupération / livraison / paiement
    [Required]
    public ModeRecuperation TypeRecuperation { get; set; }

    public InfoLivraisonPostVm? InfoLivraison { get; set; } = null;

    [Required]
    public ModePaiement ModePaie { get; set; }
}
