namespace BrasilBurger.Client.Application.Dtos;

/// <summary>
/// DTO pour l'inscription complète d'un client (Step1 + Step2)
/// </summary>
public sealed class RegisterClientDto
{
    //  Step 1 
    public string Nom { get; set; } = null!;
    public string Prenom { get; set; } = null!;
    public string Email { get; set; } = null!;
    public string Password { get; set; } = null!;

    //  Step 2 
    public string Telephone { get; set; } = null!;
    public int? QuartierId { get; set; }
    public string? ModePaiement { get; set; }  // "WAVE" | "OM"
    public string? ModeRecuperation { get; set; }  // "SUR_PLACE" | "EMPORTER" | "LIVRER"
}
