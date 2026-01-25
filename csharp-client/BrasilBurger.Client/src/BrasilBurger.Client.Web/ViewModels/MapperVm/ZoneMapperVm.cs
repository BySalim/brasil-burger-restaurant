using BrasilBurger.Client.Domain.Entities;
using BrasilBurger.Client.Web.ViewModels.GetVm;

namespace BrasilBurger.Client.Web.ViewModels.Mapper;

public sealed class ZoneMapperVm
{
    public ZoneGetVm ToGetVm(Zone zone)
    {
        if (zone is null) throw new ArgumentNullException(nameof(zone));

        var quartiers = zone.Quartiers
            .Select(ToGetVm)
            .ToList();

        return new ZoneGetVm(
            Id: zone.Id,
            Libelle: zone.Nom,
            PrixLivraison: zone.PrixLivraison,
            Quartiers: quartiers
        );
    }

    public IReadOnlyList<ZoneGetVm> ToGetVms(IEnumerable<Zone> zones)
    {
        if (zones is null) throw new ArgumentNullException(nameof(zones));
        return zones.Select(ToGetVm).ToList();
    }

    private static QuartierGetVm ToGetVm(Quartier quartier)
    {
        if (quartier is null) throw new ArgumentNullException(nameof(quartier));

        return new QuartierGetVm(
            Id: quartier.Id,
            Libelle: quartier.Nom
        );
    }
}
