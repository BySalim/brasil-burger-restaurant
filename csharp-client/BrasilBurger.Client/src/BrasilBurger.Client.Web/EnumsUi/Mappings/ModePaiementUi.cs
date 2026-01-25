using BrasilBurger.Client.Domain.Enums;
using BrasilBurger.Client.Web.EnumsUi.Abstractions;
using BrasilBurger.Client.Web.EnumsUi.ColorUi;

namespace BrasilBurger.Client.Web.EnumsUi.Mappings;

public sealed class ModePaiementUi : EnumUiBase<ModePaiement>
{
    public ModePaiementUi() : base(new List<EnumUiItem<ModePaiement>>
    {
        new(ModePaiement.WAVE, "Wave", UiColor.Blue, "account_balance_wallet"),
        new(ModePaiement.OM, "Orange Money", UiColor.Black, "phone_android"),
    }.AsReadOnly())
    { }
}
