using BrasilBurger.Domain.Entities;
using BrasilBurger.Domain.ValueObjects;

namespace BrasilBurger.Domain.Entities;

public class Commande : Entity
{
    public NumeroCommande NumCmd { get; private set; }
    public DateTime DateDebut { get; private set; }
    public DateTime? DateFin { get; private set; }
    public Montant Montant { get; private set; }
    public EtatCommande Etat { get; private set; }
    public ModeRecuperation TypeRecuperation { get; private set; }
    public int? IdPanier { get; private set; }
    public Telephone TelClient { get; private set; }
    public int? IdZoneLivraison { get; private set; }
    public int? IdLivraison { get; private set; }

    protected Commande() { }

    public Commande(NumeroCommande numCmd, Montant montant, ModeRecuperation typeRecuperation, Telephone telClient)
    {
        NumCmd = numCmd;
        DateDebut = DateTime.Now;
        Montant = montant;
        Etat = EtatCommande.EnAttente;
        TypeRecuperation = typeRecuperation;
        TelClient = telClient;
    }

    public void Terminer() => Etat = EtatCommande.Terminer;
}