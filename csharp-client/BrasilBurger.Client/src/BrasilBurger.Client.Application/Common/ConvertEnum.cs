namespace BrasilBurger.Client.Application.Common;

public static class ConvertEnum
{
    public static TEnum? FromString<TEnum>(string? value, bool ignoreCase = true)
        where TEnum : struct, Enum
    {
        if (string.IsNullOrWhiteSpace(value))
            return null;

        return Enum.TryParse<TEnum>(value.Trim(), ignoreCase, out var parsed)
            ? parsed
            : null;
    }
}
