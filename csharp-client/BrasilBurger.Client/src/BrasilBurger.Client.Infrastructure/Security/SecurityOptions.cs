using System.ComponentModel.DataAnnotations;

namespace BrasilBurger.Client.Infrastructure.Security;

public sealed class SecurityOptions
{
    public const string SectionName = "Security";

    [Required]
    public string AppSecret { get; init; } = default!;
}
