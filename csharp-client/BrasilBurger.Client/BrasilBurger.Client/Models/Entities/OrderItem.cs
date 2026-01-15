namespace BrasilBurger.Client.Models.Entities;

public class OrderItem
{
    public int Id { get; set; }
    public int OrderId { get; set; }
    public string ItemType { get; set; } // "burger" ou "menu"
    public int ItemId { get; set; }
    public int Quantity { get; set; }
    public decimal UnitPrice { get; set; }
    
    // Navigation properties
    public Order Order { get; set; }
    public ICollection<OrderComplement> OrderComplements { get; set; } = new List<OrderComplement>();
}
