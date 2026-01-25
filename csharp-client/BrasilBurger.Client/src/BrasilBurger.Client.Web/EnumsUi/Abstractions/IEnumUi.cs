namespace BrasilBurger.Client.Web.EnumsUi.Abstractions;

public interface IEnumUi<TEnum> where TEnum : struct, Enum
{
    EnumUiItem<TEnum> Get(TEnum value);
    IReadOnlyList<EnumUiItem<TEnum>> All();
}
