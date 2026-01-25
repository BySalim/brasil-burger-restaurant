using BrasilBurger.Client.Domain.Enums;
using BrasilBurger.Client.Web.EnumsUi.Abstractions;

namespace BrasilBurger.Client.Web.ViewModels.GetVm;

public sealed record QuartierGetVm(
    int Id,
    string Libelle
);
