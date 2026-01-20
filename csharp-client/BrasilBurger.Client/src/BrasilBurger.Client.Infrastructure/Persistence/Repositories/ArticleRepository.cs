using BrasilBurger.Client.Application.Abstractions.Persistence;
using BrasilBurger.Client.Domain.Entities;
using BrasilBurger.Client.Domain.Enums;
using Microsoft.EntityFrameworkCore;

namespace BrasilBurger.Client.Infrastructure.Persistence.Repositories;

public sealed class ArticleRepository : EfRepository<Article>, IArticleRepository
{
    public ArticleRepository(BrasilBurgerDbContext db) : base(db) { }

    public Task<Article?> GetByCodeAsync(string code, CancellationToken ct = default)
        => Db.Articles.AsNoTracking().FirstOrDefaultAsync(a => a.Code == code, ct);

    public async Task<IReadOnlyList<Article>> ListActifsAsync(CancellationToken ct = default)
        => await Db.Articles.AsNoTracking().Where(a => !a.EstArchiver).ToListAsync(ct);

    public async Task<IReadOnlyList<Article>> ListByCategorieAsync(
        CategorieArticle categorie,
        bool includeArchives = false,
        CancellationToken ct = default)
    {
        IQueryable<Article> q = Db.Articles.AsNoTracking();

        q = categorie switch
        {
            CategorieArticle.BURGER => q.OfType<Burger>(),
            CategorieArticle.MENU => q.OfType<Menu>(),
            CategorieArticle.COMPLEMENT => q.OfType<Complement>(),
            _ => q
        };

        if (!includeArchives)
            q = q.Where(a => !a.EstArchiver);

        return await q.ToListAsync(ct);
    }

    public Task<Menu?> GetMenuWithCompositionAsync(int menuId, CancellationToken ct = default)
        => Db.Set<Menu>()
            .AsNoTracking()
            .Include(m => m.MenuComposition)
                .ThenInclude(aq => aq.Article)
            .FirstOrDefaultAsync(m => m.Id == menuId, ct);
}
