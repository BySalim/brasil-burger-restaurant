using BrasilBurger.Client.Domain.Common;
using BrasilBurger.Client.Domain.Enums;

namespace BrasilBurger.Client.Domain.Entities;

public class ArticleQuantifier : Entity
{
    private int? _prixUnitaireSnapshot;
    private CategorieArticle? _categorieSnapshot;

    private ArticleQuantifier() { } // EF

    public ArticleQuantifier(Article article, int quantite)
    {
        Article = article ?? throw new DomainException("L'article est obligatoire.");
        ArticleId = article.Id;

        // snapshot useful if entity is later detached
        _prixUnitaireSnapshot = article.GetPrix();
        _categorieSnapshot = article.Categorie;

        Quantite = Guard.Positive(quantite, nameof(quantite));
        RecalculerMontant();
    }

    public ArticleQuantifier(int articleId, int quantite, int? prixUnitaire, CategorieArticle categorie)
    {
        ArticleId = articleId;
        _prixUnitaireSnapshot = prixUnitaire;
        _categorieSnapshot = categorie;

        Quantite = Guard.Positive(quantite, nameof(quantite));
        RecalculerMontant();
    }

    public int Quantite { get; private set; }
    public int Montant { get; private set; }

    public CategorieArticleQuantifier CategorieArticleQuantifier { get; private set; }

    public int? MenuId { get; private set; }
    public Menu? Menu { get; private set; }

    public int? PanierId { get; private set; }
    public Panier? Panier { get; private set; }

    public int ArticleId { get; private set; }
    public Article? Article { get; private set; } = null;

    public void ChangerQuantite(int quantite)
    {
        Quantite = Guard.Positive(quantite, nameof(quantite));
        RecalculerMontant();
    }

    public void RecalculerMontant()
    {
        var prixUnitaire = Article?.GetPrix() ?? _prixUnitaireSnapshot ?? 0;
        Montant = Guard.NonNegative(prixUnitaire * Quantite, nameof(Montant));
    }

    public void AffecterAuPanier(Panier panier)
    {
        if (panier is null) throw new DomainException("Le panier est obligatoire.");

        // Règles de cohérence “parent unique”
        if (MenuId is not null)
            throw new DomainException("La ligne est déjà rattachée à un menu. Impossible de la rattacher à un panier.");

        // Règles métier liées à l’article → ici, pas dans Panier.
        var categorieArticle = Article?.Categorie ?? _categorieSnapshot ?? CategorieArticle.BURGER;
        if (!EstCompatibleAvecPanier(panier, categorieArticle))
            throw new DomainException("L'article n'est pas compatible avec la catégorie du panier." +
                $" Article.Catégorie={categorieArticle}, Panier.CatégoriePanier={panier.CategoriePanier}");

        CategorieArticleQuantifier = CategorieArticleQuantifier.COMMANDE;

        Panier = panier;
        PanierId = panier.Id;
    }

    public void AffecterAuMenu(Menu menu)
    {
        if (menu is null) throw new DomainException("Le menu est obligatoire.");

        if (PanierId is not null)
            throw new DomainException("La ligne est déjà rattachée à un panier. Impossible de la rattacher à un menu.");

        // Exemple minimal de règle : éviter un menu dans un menu (à ajuster selon votre Symfony/BDD)
        var categorieArticle = Article?.Categorie ?? _categorieSnapshot ?? CategorieArticle.BURGER;
        if (categorieArticle == CategorieArticle.MENU)
            throw new DomainException("Un menu ne peut pas contenir un autre menu.");

        CategorieArticleQuantifier = CategorieArticleQuantifier.MENU;

        Menu = menu;
        MenuId = menu.Id;
    }

    private bool EstCompatibleAvecPanier(Panier panier, CategorieArticle categorieArticle)
    {
        return panier.CategoriePanier switch
        {
            CategoriePanier.BURGER => categorieArticle == CategorieArticle.BURGER || categorieArticle == CategorieArticle.COMPLEMENT,
            CategoriePanier.MENU => categorieArticle == CategorieArticle.MENU,
            _ => true
        };
    }
}
