namespace BrasilBurger.Client.Infrastructure.External.Payments;

public sealed class PaymentOptions
{
    public const string SectionName = "Payments";
    public bool Fake { get; init; } = true;

    public WaveOptions Wave { get; init; } = new();
    public OrangeMoneyOptions OrangeMoney { get; init; } = new();
}

public sealed class WaveOptions
{
    public string? BaseUrl { get; init; }
    public string? ApiKey { get; init; }
    public string? ApiSecret { get; init; }
    public string? CallbackUrl { get; init; }
}

public sealed class OrangeMoneyOptions
{
    public string? BaseUrl { get; init; }
    public string? ClientId { get; init; }
    public string? ClientSecret { get; init; }
    public string? CallbackUrl { get; init; }
}
