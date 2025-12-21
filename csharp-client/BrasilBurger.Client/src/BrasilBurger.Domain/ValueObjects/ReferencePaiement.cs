namespace BrasilBurger.Domain.ValueObjects;

public class ReferencePaiement
{
    public string Valeur { get; private set; }

    private ReferencePaiement(string valeur)
    {
        Valeur = valeur ?? throw new ArgumentNullException(nameof(valeur));
    }

    public static ReferencePaiement From(string valeur) => new ReferencePaiement(valeur);

    public static implicit operator string(ReferencePaiement reference) => reference.Valeur;
}