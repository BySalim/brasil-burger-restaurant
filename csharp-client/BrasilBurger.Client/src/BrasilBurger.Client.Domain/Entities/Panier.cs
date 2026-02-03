using BrasilBurger.Client.Domain.Common;
using BrasilBurger.Client.Domain.Enums;

namespace BrasilBurger.Client.Domain.Entities;

public class Panier : Entity
{
    private readonly List<ArticleQuantifier> _lignes = new();

    private Panier() { } // EF

    public Panier(CategoriePanier categoriePanier)
    {
        CategoriePanier = categoriePanier;
        MontantTotal = 0;
    }

    public CategoriePanier CategoriePanier { get; private set; }
    public int MontantTotal { get; set; }

    public IReadOnlyCollection<ArticleQuantifier> Lignes => _lignes;


    public void AjouterLigne(ArticleQuantifier ligne)
    {
        if (ligne is null) throw new DomainException("La ligne est obligatoire.");
        ligne.AffecterAuPanier(this);

        _lignes.Add(ligne);
        RecalculerMontantTotal();
    }

    public void RetirerLigne(ArticleQuantifier ligne)
    {
        if (ligne is null) return;

        if (_lignes.Remove(ligne))
            RecalculerMontantTotal();
    }

    public void RecalculerMontantTotal()
    {
        var total = 0;
        foreach (var l in _lignes)
        {
            l.RecalculerMontant(); // Montant dépend de l’article + quantité → responsabilité de la ligne
            total += l.Montant;
        }

        MontantTotal = Guard.NonNegative(total, nameof(MontantTotal));
    }
}
