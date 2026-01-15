using System.ComponentModel.DataAnnotations;

namespace BrasilBurger.Client.Models.DTOs.Auth;

public class LoginRequestDto
{
    [Required(ErrorMessage = "Email est requis")]
    [EmailAddress(ErrorMessage = "Format d'email invalide")]
    public string Email { get; set; }

    [Required(ErrorMessage = "Mot de passe est requis")]
    [MinLength(6, ErrorMessage = "Le mot de passe doit avoir au moins 6 caractères")]
    public string Password { get; set; }
}
