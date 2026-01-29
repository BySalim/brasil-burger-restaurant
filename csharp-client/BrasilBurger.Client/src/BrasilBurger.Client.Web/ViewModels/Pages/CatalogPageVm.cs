using System.ComponentModel.DataAnnotations;
using BrasilBurger.Client.Domain.Enums;
using BrasilBurger.Client.Web.ViewModels.GetVm;
using BrasilBurger.Client.Web.ViewModels.Shared;

namespace BrasilBurger.Client.Web.ViewModels.Pages;

// ViewModel pour le POST
public sealed class CatalogVmFormVm
{
    
    [Required(ErrorMessage = "Le mode de paiement est requis.")]
    public string? PaymentMethod { get; set; }
    
    [Required(ErrorMessage = "Le mode de retrait est requis.")]
    public string? RetrievalMethod { get; set; }
    
    // Champs de livraison (optionnels sauf si RetrievalMethod == "LIVRER")
    public int? DeliveryQuartierId { get; set; }
    public int? DeliveryPrice { get; set; }
    public string? NoteLivraison { get; set; }
    
    // Liste des articles avec quantités
    public List<ArticleQuantifierPostVm> ArticleQuantifiers { get; set; } = new();
}

public sealed class ArticleQuantifierPostVm
{
    [Required]
    public int ArticleId { get; set; }
    
    [Required]
    [Range(1, int.MaxValue, ErrorMessage = "La quantité doit être au moins 1.")]
    public int Quantite { get; set; }
}

public sealed class CatalogVm
{
    public CatalogVmFormVm Form { get; set; }
    public List<ArticleGetVm> Articles { get; set; }
    public ChoiceCardsVm PaymentMethodCards { get; set; }
    public ChoiceCardsVm RetrievalMethodCards { get; set; }
    public List<SelectOptionVm> DeliveryZoneOptions { get; set; }
    
    public CatalogVm(
        List<ArticleGetVm> articles,
        ChoiceCardsVm paymentMethodCards, 
        ChoiceCardsVm retrievalMethodCards,
        List<SelectOptionVm> deliveryZoneOptions)
    {
        Form = new CatalogVmFormVm();
        Articles = articles ?? throw new ArgumentNullException(nameof(articles));
        PaymentMethodCards = paymentMethodCards ?? throw new ArgumentNullException(nameof(paymentMethodCards));
        RetrievalMethodCards = retrievalMethodCards ?? throw new ArgumentNullException(nameof(retrievalMethodCards));
        DeliveryZoneOptions = deliveryZoneOptions ?? throw new ArgumentNullException(nameof(deliveryZoneOptions));
    }
}
