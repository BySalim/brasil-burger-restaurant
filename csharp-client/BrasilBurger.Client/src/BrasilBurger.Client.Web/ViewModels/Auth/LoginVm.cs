using System.ComponentModel.DataAnnotations;

namespace BrasilBurger.Client.ViewModels.Auth;

public sealed class LoginVm
{
    [Required(ErrorMessage = "L'adresse e-mail est requise.")]
    [EmailAddress(ErrorMessage = "L'adresse e-mail n'est pas valide.")]
    public string? Email { get; set; }

    [Required(ErrorMessage = "Le mot de passe est requis.")]
    public string? Password { get; set; }

    public bool RememberMe { get; set; }
}
