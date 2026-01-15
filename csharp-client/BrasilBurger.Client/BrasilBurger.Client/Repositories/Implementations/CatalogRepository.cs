using Microsoft.EntityFrameworkCore;
using BrasilBurger.Client.Data;
using BrasilBurger.Client.Models.Entities;
using BrasilBurger.Client.Models.Enums;
using BrasilBurger.Client.Repositories.Interfaces;

namespace BrasilBurger.Client.Repositories.Implementations;

public class CatalogRepository : ICatalogRepository
{
    private readonly AppDbContext _context;

    public CatalogRepository(AppDbContext context)
    {
        _context = context;
    }

    public async Task<List<Burger>> GetBurgersAsync()
    {
        return await _context.Burgers.Where(b => b.Available).ToListAsync();
    }

    public async Task<List<Menu>> GetMenusAsync()
    {
        return await _context.Menus.Where(m => m.Available).ToListAsync();
    }

    public async Task<List<CatalogItemDto>> GetCatalogItemsAsync(CatalogType type)
    {
        var items = new List<CatalogItemDto>();

        if (type == CatalogType.Burger || type == CatalogType.All)
        {
            var burgers = await _context.Burgers
                .Where(b => b.Available)
                .Select(b => new CatalogItemDto
                {
                    Id = b.Id,
                    Name = b.Name,
                    Description = b.Description,
                    Price = b.Price,
                    ImageUrl = b.ImageUrl,
                    Available = b.Available,
                    Type = "burger"
                })
                .ToListAsync();

            items.AddRange(burgers);
        }

        if (type == CatalogType.Menu || type == CatalogType.All)
        {
            var menus = await _context.Menus
                .Where(m => m.Available)
                .Select(m => new CatalogItemDto
                {
                    Id = m.Id,
                    Name = m.Name,
                    Description = m.Description,
                    Price = m.Price,
                    ImageUrl = m.ImageUrl,
                    Available = m.Available,
                    Type = "menu"
                })
                .ToListAsync();

            items.AddRange(menus);
        }

        return items;
    }

    public async Task<Burger> GetBurgerByIdAsync(int id)
    {
        return await _context.Burgers.FirstOrDefaultAsync(b => b.Id == id);
    }

    public async Task<Menu> GetMenuByIdAsync(int id)
    {
        return await _context.Menus.FirstOrDefaultAsync(m => m.Id == id);
    }

    public async Task<Complement> GetComplementByIdAsync(int id)
    {
        return await _context.Complements.FirstOrDefaultAsync(c => c.Id == id);
    }

    public async Task<List<Complement>> GetComplementsAsync()
    {
        return await _context.Complements.Where(c => c.Available).ToListAsync();
    }
}
