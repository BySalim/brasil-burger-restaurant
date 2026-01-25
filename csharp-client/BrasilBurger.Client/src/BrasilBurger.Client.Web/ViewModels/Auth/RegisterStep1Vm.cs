using System.ComponentModel.DataAnnotations;

namespace BrasilBurger.Client.Web.ViewModels.Auth;

public sealed class RegisterStep1Vm
{
    [Required(ErrorMessage = "Le nom est requis.")]
    public string? Nom { get; set; }

    [Required(ErrorMessage = "Le prénom est requis.")]
    public string? Prenom { get; set; }

    [Required(ErrorMessage = "L'adresse e-mail est requise.")]
    [EmailAddress(ErrorMessage = "L'adresse e-mail n'est pas valide.")]
    public string Email { get; set; } = string.Empty;

    [Required(ErrorMessage = "Le mot de passe est requis.")]
    public string? Password { get; set; }
}
