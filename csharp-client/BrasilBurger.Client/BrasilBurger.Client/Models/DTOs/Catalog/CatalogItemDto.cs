namespace BrasilBurger.Client.Models.DTOs.Catalog;

public class CatalogItemDto
{
    public int Id { get; set; }
    public string Name { get; set; }
    public string Description { get; set; }
    public decimal Price { get; set; }
    public string ImageUrl { get; set; }
    public bool Available { get; set; }
    public string Type { get; set; } // "burger" ou "menu"
}
