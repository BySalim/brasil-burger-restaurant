using BrasilBurger.Client.Application.Abstractions.Persistence;
using BrasilBurger.Client.Domain.Entities;
using BrasilBurger.Client.Domain.Enums;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Threading;
using System.Threading.Tasks;

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
}
