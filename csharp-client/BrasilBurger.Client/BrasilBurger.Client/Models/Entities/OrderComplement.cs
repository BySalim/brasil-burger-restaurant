namespace BrasilBurger.Client.Models.Entities;

public class OrderComplement
{
    public int Id { get; set; }
    public int OrderItemId { get; set; }
    public int ComplementId { get; set; }
    public int Quantity { get; set; }
    public decimal UnitPrice { get; set; }
    
    // Navigation properties
    public OrderItem OrderItem { get; set; }
}
