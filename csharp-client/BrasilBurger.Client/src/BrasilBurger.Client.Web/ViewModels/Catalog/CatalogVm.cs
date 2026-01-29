namespace BrasilBurger.Client.Web.ViewModels.Catalog;

public sealed class CatalogVm
{
    public required IEnumerable<ArticleCardItem> Articles { get; init; }
}
