using BrasilBurger.Domain.Entities;
using BrasilBurger.Domain.ValueObjects;

namespace BrasilBurger.Domain.Entities;

public class Livreur : Entity
{
    public string Nom { get; private set; }
    public string Prenom { get; private set; }
    public Telephone Telephone { get; private set; }
    public bool EstArchiver { get; private set; }
    public bool EstDisponible { get; private set; }

    protected Livreur() { }

    public Livreur(string nom, string prenom, Telephone telephone)
    {
        Nom = nom ?? throw new ArgumentNullException(nameof(nom));
        Prenom = prenom ?? throw new ArgumentNullException(nameof(prenom));
        Telephone = telephone;
        EstDisponible = true;
    }

    public void Archiver() => EstArchiver = true;
    public void SetDisponible(bool disponible) => EstDisponible = disponible;
}