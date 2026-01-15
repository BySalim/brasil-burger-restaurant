namespace BrasilBurger.Client.Models.Entities;

public class Complement
{
    public int Id { get; set; }
    public string Name { get; set; }
    public decimal Price { get; set; }
    public string ImageUrl { get; set; }
    public bool Available { get; set; }
}
