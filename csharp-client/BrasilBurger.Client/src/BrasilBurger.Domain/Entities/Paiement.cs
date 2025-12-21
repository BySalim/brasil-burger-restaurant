using BrasilBurger.Domain.Entities;
using BrasilBurger.Domain.ValueObjects;

namespace BrasilBurger.Domain.Entities;

public class Paiement : Entity
{
    public DateTime DatePaie { get; private set; }
    public Montant MontantPaie { get; private set; }
    public ModePaiement ModePaie { get; private set; }
    public ReferencePaiement? ReferencePaiementExterne { get; private set; }
    public int? IdClient { get; private set; }
    public int IdCommande { get; private set; }

    protected Paiement() { }

    public Paiement(Montant montant, ModePaiement mode, int commandeId, ReferencePaiement? reference = null)
    {
        DatePaie = DateTime.Now;
        MontantPaie = montant;
        ModePaie = mode;
        IdCommande = commandeId;
        ReferencePaiementExterne = reference;
    }
}