using BrasilBurger.Client.Application.Abstractions.Persistence;
using BrasilBurger.Client.Domain.Entities;
using BrasilBurger.Client.Domain.Enums;
using Microsoft.EntityFrameworkCore;
using System.Globalization;

namespace BrasilBurger.Client.Infrastructure.Persistence.Repositories;

public sealed class ArticleRepository : EfRepository<Article>, IArticleRepository
{
    public ArticleRepository(BrasilBurgerDbContext db) : base(db) { }

    public Task<Article?> GetByCodeAsync(string code, CancellationToken ct = default)
        => Db.Articles.AsNoTracking().FirstOrDefaultAsync(a => a.Code == code, ct);

    public async Task<IReadOnlyList<Article>> ListActifsAsync(CancellationToken ct = default)
    {
        // Burgers actifs
        var burgers = await Db.Articles
            .OfType<Burger>()
            .AsNoTracking()
            .Where(a => !a.EstArchiver)
            .ToListAsync(ct);

        // Menus actifs + composition + article de chaque ligne
        var menus = await Db.Articles
            .OfType<Menu>()
            .AsNoTracking()
            .Where(m => !m.EstArchiver)
            .Include(m => m.MenuComposition)
                .ThenInclude(aq => aq.Article)
            .ToListAsync(ct);

        // Compléments actifs
        var complements = await Db.Articles
            .OfType<Complement>()
            .AsNoTracking()
            .Where(a => !a.EstArchiver)
            .ToListAsync(ct);

        var all = new List<Article>(burgers.Count + menus.Count + complements.Count);
        all.AddRange(burgers);
        all.AddRange(menus);
        all.AddRange(complements);

        // Tri alphabétique global sur le libellé (fr-FR, insensible à la casse)
        var comparer = StringComparer.Create(
            CultureInfo.GetCultureInfo("fr-FR"),
            ignoreCase: true
        );

        return all
            .OrderBy(a => a.Libelle, comparer)
            .ToList();
    }

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

    public async Task<Article?> GetWithDependenciesAsync(int articleId, CancellationToken ct = default)
    {
        var menu = await Db.Set<Menu>()
            .AsNoTracking()
            .Include(m => m.MenuComposition)
                .ThenInclude(aq => aq.Article)
            .FirstOrDefaultAsync(m => m.Id == articleId, ct);

        if (menu is not null)
            return menu;

        return await Db.Articles
            .AsNoTracking()
            .FirstOrDefaultAsync(a => a.Id == articleId, ct);
    }

    public Task<Article?> GetByIdNoTrackingAsync(int id, CancellationToken ct = default)
        => Db.Articles
            .AsNoTracking()
            .FirstOrDefaultAsync(a => a.Id == id, ct);

    public async Task<Article?> GetByIdWithCompositionNoTrackingAsync(int articleId, CancellationToken ct = default)
    {
        var article = await Db.Articles
            .AsNoTracking()
            .FirstOrDefaultAsync(a => a.Id == articleId, ct);

        if (article is Menu menu)
        {
            var menuWithComposition = await Db.Set<Menu>()
                .AsNoTracking()
                .Include(m => m.MenuComposition)
                    .ThenInclude(aq => aq.Article)
                .FirstOrDefaultAsync(m => m.Id == articleId, ct);
            return menuWithComposition;
        }

        return article;
    }
}