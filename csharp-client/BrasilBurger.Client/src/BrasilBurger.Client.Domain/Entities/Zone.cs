using BrasilBurger.Client.Domain.Common;

namespace BrasilBurger.Client.Domain.Entities;

public class Zone : Entity
{
    private readonly List<Quartier> _quartiers = new();

    private Zone() { } // EF

    public Zone(string nom, int prixLivraison)
    {
        Nom = Guard.NotNullOrWhiteSpace(nom, nameof(nom));
        PrixLivraison = Guard.NonNegative(prixLivraison, nameof(prixLivraison));
        EstArchiver = false;
    }

    public string Nom { get; private set; } = default!;
    public int PrixLivraison { get; private set; }
    public bool EstArchiver { get; private set; }

    public IReadOnlyCollection<Quartier> Quartiers => _quartiers;

    public void Renommer(string nom) => Nom = Guard.NotNullOrWhiteSpace(nom, nameof(nom));
    public void ChangerPrixLivraison(int prix) => PrixLivraison = Guard.NonNegative(prix, nameof(prix));

    public void Archiver() => EstArchiver = true;
    public void Desarchiver() => EstArchiver = false;

    public Quartier AjouterQuartier(string nomQuartier)
    {
        var q = new Quartier(this, nomQuartier);
        _quartiers.Add(q);
        return q;
    }
}
