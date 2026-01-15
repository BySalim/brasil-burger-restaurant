using BrasilBurger.Client.Models.DTOs.Catalog;
using BrasilBurger.Client.Models.Enums;
using BrasilBurger.Client.Repositories.Interfaces;
using BrasilBurger.Client.Services.Interfaces;

namespace BrasilBurger.Client.Services.Implementations;

public class CatalogService : ICatalogService
{
    private readonly ICatalogRepository _catalogRepository;
    private readonly ICloudinaryService _cloudinaryService;

    public CatalogService(ICatalogRepository catalogRepository, ICloudinaryService cloudinaryService)
    {
        _catalogRepository = catalogRepository;
        _cloudinaryService = cloudinaryService;
    }

    public async Task<List<CatalogItemDto>> GetCatalogAsync(CatalogType type)
    {
        var items = await _catalogRepository.GetCatalogItemsAsync(type);
        
        // Résoudre les URLs des images via Cloudinary
        foreach (var item in items)
        {
            if (!string.IsNullOrEmpty(item.ImageUrl))
            {
                item.ImageUrl = _cloudinaryService.GetImageUrl(item.ImageUrl);
            }
        }

        return items;
    }

    public async Task<BurgerDto> GetBurgerDetailsAsync(int id)
    {
        var burger = await _catalogRepository.GetBurgerByIdAsync(id);

        if (burger == null)
            throw new KeyNotFoundException($"Burger avec id {id} non trouvé");

        var complements = await _catalogRepository.GetComplementsAsync();

        var dto = new BurgerDto
        {
            Id = burger.Id,
            Name = burger.Name,
            Description = burger.Description,
            Price = burger.Price,
            ImageUrl = _cloudinaryService.GetImageUrl(burger.ImageUrl),
            Available = burger.Available,
            Complements = complements.Select(c => new ComplementDto
            {
                Id = c.Id,
                Name = c.Name,
                Price = c.Price,
                ImageUrl = _cloudinaryService.GetImageUrl(c.ImageUrl),
                Available = c.Available
            }).ToList()
        };

        return dto;
    }

    public async Task<MenuDto> GetMenuDetailsAsync(int id)
    {
        var menu = await _catalogRepository.GetMenuByIdAsync(id);

        if (menu == null)
            throw new KeyNotFoundException($"Menu avec id {id} non trouvé");

        return new MenuDto
        {
            Id = menu.Id,
            Name = menu.Name,
            Description = menu.Description,
            Price = menu.Price,
            ImageUrl = _cloudinaryService.GetImageUrl(menu.ImageUrl),
            Available = menu.Available
        };
    }

    public async Task<List<ComplementDto>> GetComplementsAsync()
    {
        var complements = await _catalogRepository.GetComplementsAsync();

        return complements.Select(c => new ComplementDto
        {
            Id = c.Id,
            Name = c.Name,
            Price = c.Price,
            ImageUrl = _cloudinaryService.GetImageUrl(c.ImageUrl),
            Available = c.Available
        }).ToList();
    }
}
