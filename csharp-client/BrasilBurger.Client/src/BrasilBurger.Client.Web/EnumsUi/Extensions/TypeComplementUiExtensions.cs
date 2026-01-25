using BrasilBurger.Client.Domain.Enums;
using BrasilBurger.Client.Web.EnumsUi.Options;
using BrasilBurger.Client.Web.EnumsUi.Helpers;

namespace BrasilBurger.Client.Web.EnumsUi.Extensions;

public static class TypeComplementUiExtensions
{
    public static EnumUiMeta Ui(this TypeComplement v) => v switch
    {
        TypeComplement.BOISSON => new(
            nameof(TypeComplement.BOISSON),
            "Boisson",
            new UiIcon("local_drink"),
            UiColor.Turquoise),

        TypeComplement.FRITES => new(
            nameof(TypeComplement.FRITES),
            "Frites",
            new UiIcon("fastfood"),
            UiColor.Orange),

        _ => throw new ArgumentOutOfRangeException(nameof(v), v, "TypeComplement non mappé (UI).")
    };
    
    private static readonly EnumUiMeta[] Items = EnumUiHelper.All<TypeComplement>(Ui);
    
    public static IEnumerable<EnumUiMeta> AllUi() => Items;
    
    public static TypeComplement ToEnum(string value) => EnumUiHelper.ToEnum<TypeComplement>(value);
    
    public static TypeComplement DefaultSelected() => TypeComplement.BOISSON;
}
