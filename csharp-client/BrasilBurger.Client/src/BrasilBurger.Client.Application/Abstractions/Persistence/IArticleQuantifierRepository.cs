using BrasilBurger.Client.Domain.Entities;

namespace BrasilBurger.Client.Application.Abstractions.Persistence;

public interface IArticleQuantifierRepository : IRepository<ArticleQuantifier>
{
    Task<IReadOnlyList<ArticleQuantifier>> ListByPanierAsync(int panierId, CancellationToken ct = default);
    Task<IReadOnlyList<ArticleQuantifier>> ListByMenuAsync(int menuId, CancellationToken ct = default);

    Task<ArticleQuantifier?> GetWithArticleAsync(int id, CancellationToken ct = default);
}
