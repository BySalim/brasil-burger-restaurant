using BrasilBurger.Domain.Entities;

namespace BrasilBurger.Domain.Interfaces;

public interface IArticleRepository : IRepository<Article>
{
    Task<IEnumerable<Article>> GetByCategorieAsync(CategorieArticle categorie);
    Task<IEnumerable<Article>> GetNonArchivesAsync();
    Task<Article?> GetByCodeAsync(string code);
}