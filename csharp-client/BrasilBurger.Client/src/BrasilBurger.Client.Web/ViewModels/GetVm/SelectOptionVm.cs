
namespace BrasilBurger.Client.Web.ViewModels.GetVm;

public sealed record SelectOptionVm2(
    int Id,
    string Libelle,
    int PrixLivraison,
    IReadOnlyList<QuartierGetVm> Quartiers
);
