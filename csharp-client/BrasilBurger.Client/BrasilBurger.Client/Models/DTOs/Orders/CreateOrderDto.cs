using System.ComponentModel.DataAnnotations;

namespace BrasilBurger.Client.Models.DTOs.Orders;

public class CreateOrderDto
{
    [Required]
    public List<OrderItemDto> Items { get; set; } = new();
    
    [Required]
    public int PaymentMethodId { get; set; }
}
