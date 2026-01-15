namespace BrasilBurger.Client.Models.DTOs.Orders;

public class PaymentMethodDto
{
    public int Id { get; set; }
    public string Name { get; set; }
    public bool Enabled { get; set; }
}
