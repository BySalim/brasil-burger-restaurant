using BrasilBurger.Client.Domain.Enums;
using BrasilBurger.Client.Web.EnumsUi.Abstractions;
using BrasilBurger.Client.Web.EnumsUi.ColorUi;

namespace BrasilBurger.Client.Web.EnumsUi.Mappings;

public sealed class TypeComplementUi : EnumUiBase<TypeComplement>
{
    public TypeComplementUi() : base(new List<EnumUiItem<TypeComplement>>
    {
        new(TypeComplement.BOISSON, "Boisson", UiColor.Cyan, "local_cafe"),
        new(TypeComplement.FRITES, "Frites", UiColor.Yellow, "fastfood"),
    }.AsReadOnly())
    { }
}
