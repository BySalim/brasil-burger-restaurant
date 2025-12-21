using BrasilBurger.Domain.Entities;
using BrasilBurger.Domain.ValueObjects;

namespace BrasilBurger.Domain.Entities;

public class Zone : Entity
{
    public string Nom { get; private set; }
    public Montant PrixLivraison { get; private set; }
    public bool EstArchiver { get; private set; }

    protected Zone() { }

    public Zone(string nom, Montant prixLivraison)
    {
        Nom = nom ?? throw new ArgumentNullException(nameof(nom));
        PrixLivraison = prixLivraison;
    }

    public void Archiver() => EstArchiver = true;
}