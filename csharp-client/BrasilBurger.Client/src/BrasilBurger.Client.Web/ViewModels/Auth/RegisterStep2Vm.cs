using System.ComponentModel.DataAnnotations;

namespace BrasilBurger.Client.ViewModels.Auth;

public sealed class RegisterStep2Vm
{
    [Required(ErrorMessage = "Le numéro de téléphone est requis.")]
    public string? Phone { get; set; }

    [Required(ErrorMessage = "Veuillez choisir un moyen de paiement.")]
    public string? PaymentMethod { get; set; }

    [Required(ErrorMessage = "Veuillez choisir un mode de récupération.")]
    public string? RetrievalMethod { get; set; }

    public string? DeliveryZone { get; set; }

    // Options mockées (pas de DB)
    public List<SelectOptionVm> PaymentMethodOptions { get; } = new();
    public List<SelectOptionVm> RetrievalMethodOptions { get; } = new();
    public List<SelectOptionVm> DeliveryZoneOptions { get; } = new();
}

public sealed class SelectOptionVm
{
    public SelectOptionVm(string value, string text)
    {
        Value = value;
        Text = text;
    }

    public string Value { get; }
    public string Text { get; }
}
