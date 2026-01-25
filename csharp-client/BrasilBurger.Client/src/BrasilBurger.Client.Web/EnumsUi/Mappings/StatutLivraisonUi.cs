using BrasilBurger.Client.Domain.Enums;
using BrasilBurger.Client.Web.EnumsUi.Abstractions;
using BrasilBurger.Client.Web.EnumsUi.ColorUi;

namespace BrasilBurger.Client.Web.EnumsUi.Mappings;

public sealed class StatutLivraisonUi : EnumUiBase<StatutLivraison>
{
    public StatutLivraisonUi() : base(new List<EnumUiItem<StatutLivraison>>
    {
        new(StatutLivraison.EN_COURS, "En cours", UiColor.Blue, "local_shipping"),
        new(StatutLivraison.TERMINER, "Terminée", UiColor.Green, "check_circle"),
    }.AsReadOnly())
    { }
}
