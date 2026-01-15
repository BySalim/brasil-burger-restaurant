namespace BrasilBurger.Client.Models.DTOs.Orders;

public class OrderItemDetailDto
{
    public int Id { get; set; }
    public string ItemName { get; set; }
    public string ItemType { get; set; }
    public int Quantity { get; set; }
    public decimal UnitPrice { get; set; }
    public decimal SubTotal => Quantity * UnitPrice;
    public List<OrderComplementDetailDto> Complements { get; set; } = new();
}
