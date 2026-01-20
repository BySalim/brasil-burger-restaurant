using System;

namespace BrasilBurger.Client.Domain.Common;

internal static class Guard
{
    public static string NotNullOrWhiteSpace(string? value, string paramName)
    {
        if (string.IsNullOrWhiteSpace(value))
            throw new DomainException($"Le champ '{paramName}' est obligatoire.");
        return value.Trim();
    }

    public static int Positive(int value, string paramName)
    {
        if (value <= 0)
            throw new DomainException($"Le champ '{paramName}' doit être > 0.");
        return value;
    }

    public static int NonNegative(int value, string paramName)
    {
        if (value < 0)
            throw new DomainException($"Le champ '{paramName}' doit être >= 0.");
        return value;
    }
}
