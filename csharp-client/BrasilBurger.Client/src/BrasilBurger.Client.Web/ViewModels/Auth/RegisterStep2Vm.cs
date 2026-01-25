using System.ComponentModel.DataAnnotations;
using BrasilBurger.Client.Web.Options;
using BrasilBurger.Client.Web.ViewModels.Shared;

namespace BrasilBurger.Client.Web.ViewModels.Auth;

// ViewModel pour le POST
public sealed class RegisterStep2FormVm
{
    [Required(ErrorMessage = "Le numéro de téléphone est requis.")]
    [Phone(ErrorMessage = "Le numéro de téléphone n'est pas valide.")]
    [RegularExpression(@"^(70|75|76|77|78)\d{7}$", ErrorMessage = "Le numéro doit commencer par 70, 75, 76, 77 ou 78 et contenir 9 chiffres.")]
    public string? Phone { get; set; }
    
    public string? PaymentMethod { get; set; }
    public string? RetrievalMethod { get; set; }
    public string? DeliveryQuartierId { get; set; }
}

// ViewModel pour le GET (affichage avec les listes)
public sealed class RegisterStep2Vm
{
    public RegisterStep2FormVm Form { get; set; }
    public ChoiceCardsVm PaymentMethodCards { get; set; }
    public ChoiceCardsVm RetrievalMethodCards { get; set; }
    public List<SelectOptionVm> DeliveryZoneOptions { get; set; }

    public RegisterStep2Vm(
        ChoiceCardsVm paymentMethodCards, 
        ChoiceCardsVm retrievalMethodCards,
        List<SelectOptionVm> deliveryZoneOptions)
    {
        Form = new RegisterStep2FormVm();
        PaymentMethodCards = paymentMethodCards ?? throw new ArgumentNullException(nameof(paymentMethodCards));
        RetrievalMethodCards = retrievalMethodCards ?? throw new ArgumentNullException(nameof(retrievalMethodCards));
        DeliveryZoneOptions = deliveryZoneOptions ?? throw new ArgumentNullException(nameof(deliveryZoneOptions));
    }
}
