using BrasilBurger.Client.Domain.Common;

namespace BrasilBurger.Client.Domain.Entities;

public class Quartier : Entity
{
    private Quartier() { } // EF

    internal Quartier(Zone zone, string nom)
    {
        Zone = zone ?? throw new DomainException("La zone est obligatoire.");
        ZoneId = zone.Id;

        Nom = Guard.NotNullOrWhiteSpace(nom, nameof(nom));
    }

    public string Nom { get; private set; } = null!;

    public int ZoneId { get; private set; }
    public Zone Zone { get; private set; } = null!;

    public void Renommer(string nom) => Nom = Guard.NotNullOrWhiteSpace(nom, nameof(nom));
}
