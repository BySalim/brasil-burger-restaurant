using BrasilBurger.Client.Domain.Entities;
using BrasilBurger.Client.Domain.Enums;

namespace BrasilBurger.Client.Application.Abstractions.Persistence;

public interface IArticleRepository : IRepository<Article>
{
    Task<Article?> GetByCodeAsync(string code, CancellationToken ct = default);

    Task<IReadOnlyList<Article>> ListActifsAsync(CancellationToken ct = default);
    Task<IReadOnlyList<Article>> ListByCategorieAsync(
        CategorieArticle categorie,
        bool includeArchives = false,
        CancellationToken ct = default);

    /// <summary>
    /// Pour l’affichage d’un menu avec sa composition (ArticleQuantifier + Article).
    /// </summary>
    Task<Menu?> GetMenuWithCompositionAsync(int menuId, CancellationToken ct = default);
}
