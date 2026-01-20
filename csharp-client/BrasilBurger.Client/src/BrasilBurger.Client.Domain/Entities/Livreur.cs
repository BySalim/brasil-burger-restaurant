using BrasilBurger.Client.Domain.Common;

namespace BrasilBurger.Client.Domain.Entities;

public class Livreur : Entity
{
    private Livreur() { } // EF

    public Livreur(string nom, string prenom, string telephone)
    {
        Nom = Guard.NotNullOrWhiteSpace(nom, nameof(nom));
        Prenom = Guard.NotNullOrWhiteSpace(prenom, nameof(prenom));
        Telephone = Guard.NotNullOrWhiteSpace(telephone, nameof(telephone));
        EstDisponible = true;
        EstArchiver = false;
    }

    public string Nom { get; private set; } = default!;
    public string Prenom { get; private set; } = default!;
    public string Telephone { get; private set; } = default!;
    public bool EstArchiver { get; private set; }
    public bool EstDisponible { get; private set; }

    public void DefinirDisponibilite(bool disponible)
    {
        if (EstArchiver && disponible)
            throw new DomainException("Un livreur archivé ne peut pas être rendu disponible.");
        EstDisponible = disponible;
    }

    public void Archiver()
    {
        EstArchiver = true;
        EstDisponible = false;
    }
}
