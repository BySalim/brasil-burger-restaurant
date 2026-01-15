using System.ComponentModel.DataAnnotations;

namespace BrasilBurger.Client.Models.DTOs.Catalog;

public class MenuDto
{
    public int Id { get; set; }
    
    [Required]
    public string Name { get; set; }
    
    [Required]
    public string Description { get; set; }
    
    [Required]
    public decimal Price { get; set; }
    
    public string ImageUrl { get; set; }
    
    public bool Available { get; set; }
}
