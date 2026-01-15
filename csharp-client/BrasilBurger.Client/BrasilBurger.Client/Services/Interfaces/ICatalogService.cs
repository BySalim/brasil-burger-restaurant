using BrasilBurger.Client.Models.DTOs.Catalog;
using BrasilBurger.Client.Models.Enums;

namespace BrasilBurger.Client.Services.Interfaces;

public interface ICatalogService
{
    Task<List<CatalogItemDto>> GetCatalogAsync(CatalogType type);
    Task<BurgerDto> GetBurgerDetailsAsync(int id);
    Task<MenuDto> GetMenuDetailsAsync(int id);
    Task<List<ComplementDto>> GetComplementsAsync();
}
