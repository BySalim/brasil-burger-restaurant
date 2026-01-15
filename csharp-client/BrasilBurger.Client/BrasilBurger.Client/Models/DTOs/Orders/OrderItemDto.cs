using System.ComponentModel.DataAnnotations;

namespace BrasilBurger.Client.Models.DTOs.Orders;

public class OrderItemDto
{
    [Required]
    public string ItemType { get; set; } // "burger" ou "menu"
    
    [Required]
    public int ItemId { get; set; }
    
    [Required]
    [Range(1, int.MaxValue)]
    public int Quantity { get; set; }
    
    public List<OrderComplementDto> Complements { get; set; } = new();
}
