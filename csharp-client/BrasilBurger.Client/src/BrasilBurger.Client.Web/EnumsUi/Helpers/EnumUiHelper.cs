using System;
using BrasilBurger.Client.Web.EnumsUi.Options;

namespace BrasilBurger.Client.Web.EnumsUi.Helpers;

public static class EnumUiHelper
{
    public static EnumUiMeta[] All<TEnum>(Func<TEnum, EnumUiMeta> map)
        where TEnum : struct, Enum
        => Enum.GetValues<TEnum>()
            .Select(map)
            .ToArray();
    
    public static TEnum? ToEnum<TEnum>(string value)
        where TEnum : struct, Enum
        => Enum.TryParse<TEnum>(value, ignoreCase: true, out var result)
            ? result
            : null;
}
