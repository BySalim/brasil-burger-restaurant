namespace BrasilBurger.Client.Models.DTOs.Orders;

public class OrderComplementDetailDto
{
    public int Id { get; set; }
    public string ComplementName { get; set; }
    public int Quantity { get; set; }
    public decimal UnitPrice { get; set; }
    public decimal SubTotal => Quantity * UnitPrice;
}
