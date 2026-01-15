namespace BrasilBurger.Client.Models.DTOs.Orders;

public class OrderDetailDto
{
    public int Id { get; set; }
    public string Reference { get; set; }
    public decimal TotalPrice { get; set; }
    public string Status { get; set; }
    public string PaymentMethod { get; set; }
    public DateTime CreatedAt { get; set; }
    public List<OrderItemDetailDto> Items { get; set; } = new();
}
