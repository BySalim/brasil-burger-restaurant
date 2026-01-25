using BrasilBurger.Client.Domain.Enums;
using BrasilBurger.Client.Web.EnumsUi.Abstractions;
using BrasilBurger.Client.Web.EnumsUi.ColorUi;

namespace BrasilBurger.Client.Web.EnumsUi.Mappings;

public sealed class EtatCommandeUi : EnumUiBase<EtatCommande>
{
    public EtatCommandeUi() : base(new List<EnumUiItem<EtatCommande>>
    {
        new(EtatCommande.EN_ATTENTE, "En attente", UiColor.Yellow, "schedule"),
        new(EtatCommande.EN_PREPARATION, "En préparation", UiColor.Blue, "restaurant"),
        new(EtatCommande.TERMINER, "Terminée", UiColor.Green, "check_circle"),
        new(EtatCommande.ANNULER, "Annulée", UiColor.Red, "cancel"),
    }.AsReadOnly())
    { }
}
