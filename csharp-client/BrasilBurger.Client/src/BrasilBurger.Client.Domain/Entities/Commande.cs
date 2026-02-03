using BrasilBurger.Client.Domain.Common;
using BrasilBurger.Client.Domain.Enums;

namespace BrasilBurger.Client.Domain.Entities;

public class Commande : Entity
{
    private Commande() { } // EF

    public Commande(string numCmd, Client client, ModeRecuperation typeRecuperation, InfoLivraison? infoLivraison, Panier? panier)
    {
        NumCmd = Guard.NotNullOrWhiteSpace(numCmd, nameof(numCmd));
        Client = client ?? throw new DomainException("Le client est obligatoire.");
        ClientId = client.Id;

        TypeRecuperation = typeRecuperation;
        InfoLivraison = infoLivraison;
        InfoLivraisonId = infoLivraison?.Id;

        Panier = panier;
        PanierId = panier?.Id;

        DateDebut = DateTime.UtcNow;
        Etat = EtatCommande.EN_ATTENTE;

        RecalculerMontant();
    }

    // Overload that accepts clientId instead of Client instance to avoid attaching a tracked Client
    public Commande(string numCmd, int clientId, ModeRecuperation typeRecuperation, InfoLivraison? infoLivraison, Panier? panier)
    {
        NumCmd = Guard.NotNullOrWhiteSpace(numCmd, nameof(numCmd));
        ClientId = clientId;

        TypeRecuperation = typeRecuperation;
        InfoLivraison = infoLivraison;
        InfoLivraisonId = infoLivraison?.Id;

        Panier = panier;
        PanierId = panier?.Id;

        DateDebut = DateTime.UtcNow;
        Etat = EtatCommande.EN_ATTENTE;

        RecalculerMontant();
    }

    public string NumCmd { get; private set; } = default!;
    public DateTime DateDebut { get; private set; }
    public DateTime? DateFin { get; private set; }
    public int Montant { get; private set; }

    public EtatCommande Etat { get; private set; }
    public ModeRecuperation TypeRecuperation { get; private set; }

    public int? PanierId { get; private set; }
    public Panier? Panier { get; private set; }

    public int? LivraisonId { get; private set; }
    public Livraison? Livraison { get; private set; }

    public int ClientId { get; private set; }
    public Client Client { get; private set; } = default!;

    public int? InfoLivraisonId { get; private set; }
    public InfoLivraison? InfoLivraison { get; private set; } = default!;

    public Paiement? Paiement { get; private set; }

    public void AssocierPanier(Panier panier)
    {
        Panier = panier ?? throw new DomainException("Le panier est obligatoire.");
        PanierId = panier.Id;
        RecalculerMontant();
    }

    public void AssignerLivraison(Livraison livraison)
    {
        Livraison = livraison ?? throw new DomainException("La livraison est obligatoire.");
        LivraisonId = livraison.Id;
    }

    public void AssocierPaiement(Paiement paiement)
    {
        Paiement = paiement ?? throw new DomainException("Le paiement est obligatoire.");
    }

    public void RecalculerMontant()
    {
        var panierTotal = Panier?.MontantTotal ?? 0;
        var fraisLivraison = TypeRecuperation == ModeRecuperation.LIVRER ? InfoLivraison?.PrixLivraison ?? 0 : 0;
        Montant = Guard.NonNegative(panierTotal + fraisLivraison, nameof(Montant));
    }

    public void PasserEnPreparation()
    {
        if (Etat != EtatCommande.EN_ATTENTE)
            throw new DomainException("Transition invalide : la commande doit être EN_ATTENTE.");
        Etat = EtatCommande.EN_PREPARATION;
    }

    public void Terminer()
    {
        if (Etat != EtatCommande.EN_PREPARATION)
            throw new DomainException("Transition invalide : la commande doit être EN_PREPARATION.");
        Etat = EtatCommande.TERMINER;
        DateFin = DateTime.UtcNow;
    }

    public void Annuler()
    {
        if (Etat == EtatCommande.TERMINER)
            throw new DomainException("Impossible d'annuler une commande terminée.");
        Etat = EtatCommande.ANNULER;
        DateFin = DateTime.UtcNow;
    }
}
