using BrasilBurger.Client.Domain.Common;
using BrasilBurger.Client.Domain.Enums;

namespace BrasilBurger.Client.Domain.Entities;

public class GroupeLivraison : Entity
{
    private readonly List<Livraison> _livraisons = new();

    private GroupeLivraison() { } // EF

    public GroupeLivraison(Livreur livreur)
    {
        Livreur = livreur ?? throw new DomainException("Le livreur est obligatoire.");
        LivreurId = livreur.Id;
        Statut = StatutLivraison.EN_COURS;
    }

    public int LivreurId { get; private set; }
    public Livreur Livreur { get; private set; } = default!;

    public StatutLivraison Statut { get; private set; }

    public IReadOnlyCollection<Livraison> Livraisons => _livraisons;

    public Livraison CreerLivraison()
    {
        if (Statut == StatutLivraison.TERMINER)
            throw new DomainException("Impossible d'ajouter une livraison sur un groupe terminé.");

        var livraison = new Livraison(this);
        _livraisons.Add(livraison);
        return livraison;
    }

    public void Terminer()
        => Statut = StatutLivraison.TERMINER;
}
