using BrasilBurger.Client.Application.Abstractions.Persistence;
using BrasilBurger.Client.Domain.Entities;
using Microsoft.EntityFrameworkCore;

namespace BrasilBurger.Client.Infrastructure.Persistence.Repositories;

public sealed class ArticleQuantifierRepository : EfRepository<ArticleQuantifier>, IArticleQuantifierRepository
{
    public ArticleQuantifierRepository(BrasilBurgerDbContext db) : base(db) { }

    public async Task<IReadOnlyList<ArticleQuantifier>> ListByPanierAsync(int panierId, CancellationToken ct = default)
        => await Db.ArticleQuantifiers.AsNoTracking()
            .Where(aq => aq.PanierId == panierId)
            .Include(aq => aq.Article)
            .ToListAsync(ct);

    public async Task<IReadOnlyList<ArticleQuantifier>> ListByMenuAsync(int menuId, CancellationToken ct = default)
        => await Db.ArticleQuantifiers.AsNoTracking()
            .Where(aq => aq.MenuId == menuId)
            .Include(aq => aq.Article)
            .ToListAsync(ct);

    public Task<ArticleQuantifier?> GetWithArticleAsync(int id, CancellationToken ct = default)
        => Db.ArticleQuantifiers.AsNoTracking()
            .Include(aq => aq.Article)
            .FirstOrDefaultAsync(aq => aq.Id == id, ct);
}
