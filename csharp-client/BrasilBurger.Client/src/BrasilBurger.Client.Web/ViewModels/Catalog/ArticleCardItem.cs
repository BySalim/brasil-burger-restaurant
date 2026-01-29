using BrasilBurger.Client.Web.ViewModels.Shared;

namespace BrasilBurger.Client.Web.ViewModels.Catalog;

public sealed class ArticleCardItem
{
    public int Id { get; set; }
    public string Code { get; set; } = string.Empty;
    public string Name { get; set; } = string.Empty;
    public string Description { get; set; } = string.Empty;
    public int Price { get; set; }
    public string ImagePublicId { get; set; } = string.Empty;
    public string CategoryName { get; set; } = string.Empty;
    public BadgeCardItemVm? Tag { get; set; } = null;
}
