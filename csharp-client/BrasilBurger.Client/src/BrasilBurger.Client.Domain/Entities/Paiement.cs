using BrasilBurger.Client.Domain.Common;
using BrasilBurger.Client.Domain.Enums;

namespace BrasilBurger.Client.Domain.Entities;

public class Paiement : Entity
{
    private Paiement() { } // EF

    public Paiement(Commande commande, int montant, ModePaiement mode, string? referenceExterne, bool test, Client? client = null)
    {
        Commande = commande ?? throw new DomainException("La commande est obligatoire.");
        CommandeId = commande.Id;

        MontantPaie = Guard.Positive(montant, nameof(montant));
        ModePaie = mode;
        ReferencePaiementExterne = referenceExterne;
        Test = test;

        Client = client;
        ClientId = client?.Id;

        DatePaie = DateTime.UtcNow;
    }

    public DateTime DatePaie { get; private set; }
    public int MontantPaie { get; private set; }
    public ModePaiement ModePaie { get; private set; }
    public string? ReferencePaiementExterne { get; private set; }
    public bool Test { get; private set; }

    public int? ClientId { get; private set; }
    public Client? Client { get; private set; }

    public int CommandeId { get; private set; }
    public Commande Commande { get; private set; } = default!;
}
