using System.ComponentModel.DataAnnotations;

namespace BrasilBurger.Client.ViewModels.Auth;

public sealed class RegisterStep1Vm
{
    [Required(ErrorMessage = "Le nom est requis.")]
    public string? Nom { get; set; }

    [Required(ErrorMessage = "Le prénom est requis.")]
    public string? Prenom { get; set; }

    [Required(ErrorMessage = "L'adresse e-mail est requise.")]
    [EmailAddress(ErrorMessage = "L'adresse e-mail n'est pas valide.")]
    public string? Email { get; set; }

    [Required(ErrorMessage = "Le mot de passe est requis.")]
    [MinLength(8, ErrorMessage = "Le mot de passe doit contenir au moins 8 caractères.")]
    public string? Password { get; set; }
}
