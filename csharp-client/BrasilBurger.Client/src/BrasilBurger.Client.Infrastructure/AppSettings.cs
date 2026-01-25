
namespace BrasilBurger.Client.Infrastructure;

/// <summary>
/// Configuration globale de l'application
/// </summary>
public sealed class AppSettings
{
    public const string SectionName = "App";
    
    public required string Name { get; init; }
    
    public required string ShortName { get; init; }

    public required string IndicatifPays { get; init; }
    
    public required string Currency { get; init; }
    
    public required string CurrencySymbol { get; init; }

    public required string DefaultLocale { get; init; }

    public required string TimeZone { get; init; }
    
    public required string LogoMain { get; init; }
    public required string LogoBlack { get; init; }
    public required string LogoWhite { get; init; }
    
}
