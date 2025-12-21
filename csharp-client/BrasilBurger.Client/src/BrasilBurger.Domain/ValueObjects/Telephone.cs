using BrasilBurger.Domain.Exceptions;

namespace BrasilBurger.Domain.ValueObjects;

public class Telephone
{
    public string Numero { get; private set; }

    private Telephone(string numero)
    {
        if (string.IsNullOrWhiteSpace(numero) || !IsValidPhoneNumber(numero))
            throw new DomainException("Numéro de téléphone invalide.");
        Numero = numero;
    }

    public static Telephone From(string numero) => new Telephone(numero);

    private static bool IsValidPhoneNumber(string numero)
    {
        // Validation simple pour Sénégal (ex: +221XXXXXXXXX ou 77XXXXXXX)
        return numero.Length >= 9 && numero.All(char.IsDigit);
    }

    public static implicit operator string(Telephone telephone) => telephone.Numero;
}