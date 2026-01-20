using BrasilBurger.Client.Domain.Common;
using BrasilBurger.Client.Domain.Enums;

namespace BrasilBurger.Client.Domain.Entities;

public class Livraison : Entity
{
    private Livraison() { } // EF

    internal Livraison(GroupeLivraison groupe)
    {
        GroupeLivraison = groupe ?? throw new DomainException("Le groupe de livraison est obligatoire.");
        GroupeLivraisonId = groupe.Id;

        Statut = StatutLivraison.EN_COURS;
        DateDebut = DateTime.UtcNow;
    }

    public StatutLivraison Statut { get; private set; }
    public DateTime DateDebut { get; private set; }
    public DateTime? DateFin { get; private set; }

    public int GroupeLivraisonId { get; private set; }
    public GroupeLivraison GroupeLivraison { get; private set; } = default!;

    public void Terminer()
    {
        if (Statut == StatutLivraison.TERMINER)
            return;

        Statut = StatutLivraison.TERMINER;
        DateFin = DateTime.UtcNow;
    }
}
