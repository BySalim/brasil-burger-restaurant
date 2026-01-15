using BrasilBurger.Client.Models.Enums;

namespace BrasilBurger.Client.Models.Entities;

public class Order
{
    public int Id { get; set; }
    public int UserId { get; set; }
    public string Reference { get; set; }
    public decimal TotalPrice { get; set; }
    public OrderStatus Status { get; set; }
    public int PaymentMethodId { get; set; }
    public DateTime CreatedAt { get; set; }
    
    // Navigation properties
    public User User { get; set; }
    public PaymentMethod PaymentMethod { get; set; }
    public ICollection<OrderItem> OrderItems { get; set; } = new List<OrderItem>();
}
