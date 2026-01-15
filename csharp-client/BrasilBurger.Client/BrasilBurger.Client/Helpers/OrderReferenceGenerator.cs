namespace BrasilBurger.Client.Helpers;

public class OrderReferenceGenerator
{
    public static string Generate()
    {
        var date = DateTime.UtcNow.ToString("yyyyMMdd");
        var random = new Random();
        var randomPart = random.Next(1000, 9999);
        return $"BR-{date}-{randomPart}";
    }
}
