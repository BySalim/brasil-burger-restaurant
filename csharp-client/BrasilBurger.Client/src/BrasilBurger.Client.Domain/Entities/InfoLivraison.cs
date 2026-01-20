using BrasilBurger.Client.Domain.Common;

namespace BrasilBurger.Client.Domain.Entities;

public sealed class InfoLivraison : Entity
{
    private InfoLivraison() { }

    public int IdZone { get; private set; }
    public Zone Zone { get; private set; } = null!;

    public int IdQuartier { get; private set; }
    public Quartier Quartier { get; private set; } = null!;

    public string? NoteLivraison { get; private set; }
    public int PrixLivraison { get; private set; }

    public InfoLivraison(int idZone, int idQuartier, int prixLivraison, string? note = null)
    {
        IdZone = idZone;
        IdQuartier = idQuartier;
        PrixLivraison = prixLivraison;
        NoteLivraison = note;
    }
}
