using Microsoft.AspNetCore.Mvc;
using BrasilBurger.Client.Models.Enums;
using BrasilBurger.Client.Services.Interfaces;

namespace BrasilBurger.Client.Controllers;

[ApiController]
[Route("api/[controller]")]
public class CatalogController : ControllerBase
{
    private readonly ICatalogService _catalogService;

    public CatalogController(ICatalogService catalogService)
    {
        _catalogService = catalogService;
    }

    [HttpGet]
    public async Task<IActionResult> GetCatalog([FromQuery] CatalogType? type = CatalogType.All)
    {
        var items = await _catalogService.GetCatalogAsync(type ?? CatalogType.All);
        return Ok(items);
    }

    [HttpGet("burger/{id}")]
    public async Task<IActionResult> GetBurgerDetails(int id)
    {
        try
        {
            var burger = await _catalogService.GetBurgerDetailsAsync(id);
            return Ok(burger);
        }
        catch (KeyNotFoundException ex)
        {
            return NotFound(new { message = ex.Message });
        }
    }

    [HttpGet("menu/{id}")]
    public async Task<IActionResult> GetMenuDetails(int id)
    {
        try
        {
            var menu = await _catalogService.GetMenuDetailsAsync(id);
            return Ok(menu);
        }
        catch (KeyNotFoundException ex)
        {
            return NotFound(new { message = ex.Message });
        }
    }

    [HttpGet("complements")]
    public async Task<IActionResult> GetComplements()
    {
        var complements = await _catalogService.GetComplementsAsync();
        return Ok(complements);
    }
}
