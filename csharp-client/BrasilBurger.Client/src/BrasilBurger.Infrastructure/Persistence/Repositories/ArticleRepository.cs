using BrasilBurger.Domain.Entities;
using BrasilBurger.Domain.Interfaces;
using BrasilBurger.Infrastructure.Persistence.Repositories;

namespace BrasilBurger.Infrastructure.Persistence.Repositories;

public class ArticleRepository : Repository<Article>, IArticleRepository
{
    public ArticleRepository(BrasilBurgerDbContext context) : base(context) { }

    public async Task<IEnumerable<Article>> GetByCategorieAsync(CategorieArticle categorie)
    {
        return await _context.Articles.Where(a => a.Categorie == categorie && !a.EstArchiver).ToListAsync();
    }

    public async Task<IEnumerable<Article>> GetNonArchivesAsync()
    {
        return await _context.Articles.Where(a => !a.EstArchiver).ToListAsync();
    }

    public async Task<Article?> GetByCodeAsync(string code)
    {
        return await _context.Articles.FirstOrDefaultAsync(a => a.Code == code);
    }
}