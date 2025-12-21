using BrasilBurger.Domain.Exceptions;

namespace BrasilBurger.Domain.ValueObjects;

public class NumeroCommande
{
    public string Valeur { get; private set; }

    private NumeroCommande(string valeur)
    {
        if (string.IsNullOrWhiteSpace(valeur))
            throw new DomainException("Le numéro de commande ne peut pas être vide.");
        Valeur = valeur;
    }

    public static NumeroCommande From(string valeur) => new NumeroCommande(valeur);

    public static implicit operator string(NumeroCommande numero) => numero.Valeur;
}