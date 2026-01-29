using BrasilBurger.Client.Domain.Entities;
using BrasilBurger.Client.Web.ViewModels.Catalog;
using BrasilBurger.Client.Web.ViewModels.Shared;
using BrasilBurger.Client.Web.EnumsUi.Extensions;
using BrasilBurger.Client.Web.EnumsUi.Options;

namespace BrasilBurger.Client.Web.Mappers;

public class ArticleCardItemVmMapper
{
    public ArticleCardItem ToArticleCardItem(Article article, BadgeVariant badgeVariant = BadgeVariant.Soft)
    {
        var badge = article.Categorie.Ui().ToBadgeCardItemVmWithoutVisual(badgeVariant);

        return new ArticleCardItem
        {
            Id = article.Id,
            Code = article.Code,
            Name = article.Libelle,
            Description = article.Description ?? string.Empty,
            Price = article.GetPrix() ?? 0,
            ImagePublicId = article.ImagePublicId,
            CategoryName = article.Categorie.ToString(),
            Tag = badge
        };
    }

    public IEnumerable<ArticleCardItem> ToArticleCardItems(IEnumerable<Article> articles, BadgeVariant badgeVariant = BadgeVariant.Soft)
    {
        return articles.Select(article => ToArticleCardItem(article, badgeVariant)).ToList();
    }
}
