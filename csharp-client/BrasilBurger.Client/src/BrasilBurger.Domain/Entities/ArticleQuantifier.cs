using BrasilBurger.Domain.Entities;
using BrasilBurger.Domain.ValueObjects;

namespace BrasilBurger.Domain.Entities;

public class ArticleQuantifier : Entity
{
    public int Quantite { get; private set; }
    public Montant Montant { get; private set; }
    public CategorieArticleQuantifier CategorieArticleQuantifier { get; private set; }
    public int? IdMenu { get; private set; }
    public int? IdPanier { get; private set; }
    public int? IdArticle { get; private set; }

    protected ArticleQuantifier() { }

    public ArticleQuantifier(int quantite, Montant montant, CategorieArticleQuantifier categorie, int? idMenu, int? idPanier, int? idArticle)
    {
        Quantite = quantite;
        Montant = montant;
        CategorieArticleQuantifier = categorie;
        IdMenu = idMenu;
        IdPanier = idPanier;
        IdArticle = idArticle;
    }
}