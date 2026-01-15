using System.ComponentModel.DataAnnotations;

namespace BrasilBurger.Client.Models.DTOs.Orders;

public class OrderComplementDto
{
    [Required]
    public int ComplementId { get; set; }
    
    [Required]
    [Range(1, int.MaxValue)]
    public int Quantity { get; set; }
}
