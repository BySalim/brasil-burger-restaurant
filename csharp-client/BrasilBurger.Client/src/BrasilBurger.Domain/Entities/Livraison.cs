using BrasilBurger.Domain.Entities;

namespace BrasilBurger.Domain.Entities;

public class Livraison : Entity
{
    public StatutLivraison Statut { get; private set; }
    public DateTime DateDebut { get; private set; }
    public DateTime? DateFin { get; private set; }
    public int IdZoneLivraison { get; private set; }
    public int IdLivreur { get; private set; }

    protected Livraison() { }

    public Livraison(int idZone, int idLivreur)
    {
        Statut = StatutLivraison.EnCours;
        DateDebut = DateTime.Now;
        IdZoneLivraison = idZone;
        IdLivreur = idLivreur;
    }

    public void Terminer() 
    {
        Statut = StatutLivraison.Terminer;
        DateFin = DateTime.Now;
    }
}