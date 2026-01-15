using BrasilBurger.Client.Models.Entities;
using BrasilBurger.Client.Models.Enums;

namespace BrasilBurger.Client.Repositories.Interfaces;

public interface ICatalogRepository
{
    Task<List<Burger>> GetBurgersAsync();
    Task<List<Menu>> GetMenusAsync();
    Task<List<CatalogItemDto>> GetCatalogItemsAsync(CatalogType type);
    Task<Burger> GetBurgerByIdAsync(int id);
    Task<Menu> GetMenuByIdAsync(int id);
    Task<Complement> GetComplementByIdAsync(int id);
    Task<List<Complement>> GetComplementsAsync();
}

public class CatalogItemDto
{
    public int Id { get; set; }
    public string Name { get; set; }
    public string Description { get; set; }
    public decimal Price { get; set; }
    public string ImageUrl { get; set; }
    public bool Available { get; set; }
    public string Type { get; set; }
}
