using BrasilBurger.Domain.Exceptions;

namespace BrasilBurger.Domain.ValueObjects;

public class Montant
{
    public int Valeur { get; private set; }

    private Montant(int valeur)
    {
        if (valeur < 0)
            throw new DomainException("Le montant ne peut pas être négatif.");
        Valeur = valeur;
    }

    public static Montant From(int valeur) => new Montant(valeur);

    public static implicit operator int(Montant montant) => montant.Valeur;
}