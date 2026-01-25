using System.ComponentModel.DataAnnotations;

namespace BrasilBurger.Client.Web.ViewModels.Auth;

public sealed class LoginVm
{
    [Required(ErrorMessage = "L'adresse e-mail est requise.")]
    [EmailAddress(ErrorMessage = "L'adresse e-mail n'est pas valide.")]
    public string LoginEmail { get; set; } = string.Empty;

    [Required(ErrorMessage = "Le mot de passe est requis.")]
    public string Password { get; set; } = string.Empty;

    public bool RememberMe { get; set; }
}
