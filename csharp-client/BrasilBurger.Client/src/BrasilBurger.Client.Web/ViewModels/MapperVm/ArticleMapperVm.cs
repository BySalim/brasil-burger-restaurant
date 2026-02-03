using BrasilBurger.Client.Application.Abstractions.External;
using BrasilBurger.Client.Domain.Entities;
using BrasilBurger.Client.Domain.Enums;
using BrasilBurger.Client.Web.EnumsUi.Abstractions;
using BrasilBurger.Client.Web.EnumsUi.Mappings;
using BrasilBurger.Client.Web.ViewModels.GetVm;

namespace BrasilBurger.Client.Web.ViewModels.MapperVm;

public sealed class ArticleMapperVm
{
    private readonly ICloudinaryUrlService _cloudinaryUrlService;

    // Instanciés une seule fois (singleton)
    private readonly IEnumUi<CategorieArticle> _categorieArticleUi = new CategorieArticleUi();
    private readonly IEnumUi<TypeComplement> _typeComplementUi = new TypeComplementUi();

    public ArticleMapperVm(ICloudinaryUrlService cloudinaryUrlService)
        => _cloudinaryUrlService = cloudinaryUrlService ?? throw new ArgumentNullException(nameof(cloudinaryUrlService));

    public ArticleGetVm ToGetVm(Article article)
        => ToGetVmInternal(article, includeMenuComposition: true);

    public  List<ArticleGetVm> ToGetVms(IEnumerable<Article> articles)
    {
        if (articles is null) throw new ArgumentNullException(nameof(articles));
        return articles.Select(a => ToGetVmInternal(a, includeMenuComposition: true)).ToList();
    }

    public ArticleQuantifierGetVm ToGetVm(ArticleQuantifier ligne)
        => ToGetVm(ligne, includeMenuArticleComposition: false);

    public  List<ArticleQuantifierGetVm> ToGetVms(IEnumerable<ArticleQuantifier> lignes)
    {
        if (lignes is null) throw new ArgumentNullException(nameof(lignes));
        return lignes.Select(l => ToGetVm(l, includeMenuArticleComposition: false)).ToList();
    }

    private ArticleQuantifierGetVm ToGetVm(ArticleQuantifier ligne, bool includeMenuArticleComposition)
    {
        if (ligne is null) throw new ArgumentNullException(nameof(ligne));
        if (ligne.Article is null) throw new ArgumentException("Ligne d'article sans article associé.", nameof(ligne));

        // on ne descend jamais récursivement sur la composition d’un article enfant
        var articleVm = ToGetVmInternal(ligne.Article, includeMenuComposition: includeMenuArticleComposition);

        return new ArticleQuantifierGetVm(
            Article: articleVm,
            Quantite: ligne.Quantite,
            Montant: ligne.Montant
        );
    }

    private ArticleGetVm ToGetVmInternal(Article article, bool includeMenuComposition)
    {
        if (article is null) throw new ArgumentNullException(nameof(article));

        var prix = article.GetPrix() ?? article.Prix ?? 0;
        var imageUrl = _cloudinaryUrlService.GetPublicUrl(article.ImagePublicId);


         List<ArticleQuantifierGetVm>? articleQuantifiers = null;

        if (includeMenuComposition && article is Menu menu)
        {
            articleQuantifiers = menu.MenuComposition
                .Select(l => ToGetVm(l, includeMenuArticleComposition: false))
                .ToList();
        }

        return new ArticleGetVm(
            Id: article.Id,
            Libelle: article.Libelle,
            ImageUrl: imageUrl,
            TypeCategorieUi: _categorieArticleUi.Get(article.Categorie),
            Description: article is not Complement ? article.Description : null,
            TypeComplementUi: article.TypeComplement is TypeComplement tc ? _typeComplementUi.Get(tc) : null,
            Prix: prix,
            ArticleQuantifiers: articleQuantifiers
        );
    }
}
