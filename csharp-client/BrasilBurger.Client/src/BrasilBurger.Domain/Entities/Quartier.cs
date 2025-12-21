using BrasilBurger.Domain.Entities;

namespace BrasilBurger.Domain.Entities;

public class Quartier : Entity
{
    public string Nom { get; private set; }
    public int IdZone { get; private set; }

    protected Quartier() { }

    public Quartier(string nom, int idZone)
    {
        Nom = nom ?? throw new ArgumentNullException(nameof(nom));
        IdZone = idZone;
    }
}