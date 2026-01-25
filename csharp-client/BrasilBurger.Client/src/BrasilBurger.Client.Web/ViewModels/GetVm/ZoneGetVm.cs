
namespace BrasilBurger.Client.Web.ViewModels.GetVm;

public sealed record ZoneGetVm(
    int Id,
    string Libelle,
    int PrixLivraison,
    IReadOnlyList<QuartierGetVm> Quartiers
);
