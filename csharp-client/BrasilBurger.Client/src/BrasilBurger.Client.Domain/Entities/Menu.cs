using BrasilBurger.Client.Domain.Common;
using BrasilBurger.Client.Domain.Enums;

namespace BrasilBurger.Client.Domain.Entities;

public class Menu : Article
{
    private readonly List<ArticleQuantifier> _menuComposition = new();

    private Menu() { } // EF

    public Menu(string code, string libelle, string imagePublicId, string? description)
        : base(code, libelle, imagePublicId)
    {
        Description = description;
    }

    public override CategorieArticle Categorie => CategorieArticle.MENU;

    public IReadOnlyCollection<ArticleQuantifier> MenuComposition => _menuComposition;

    public void AjouterLigneComposition(ArticleQuantifier ligne)
    {
        if (ligne is null) throw new DomainException("La ligne est obligatoire.");

        // Les règles sont appliquées par ArticleQuantifier.
        ligne.AffecterAuMenu(this);

        _menuComposition.Add(ligne);
    }

    public void RetirerLigneComposition(ArticleQuantifier ligne)
        => _menuComposition.Remove(ligne);

    public override int? GetPrix()
    {
        var total = 0;
        foreach (var aq in _menuComposition)
        {
            aq.RecalculerMontant();
            total += aq.Montant;
        }
        return total;
    }
}
