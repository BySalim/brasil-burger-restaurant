using BrasilBurger.Client.Domain.Entities;

namespace BrasilBurger.Client.Application.Abstractions.Persistence;

public interface IZoneRepository : IRepository<Zone>
{
    Task<IReadOnlyList<Zone>> ListActivesAsync(CancellationToken ct = default);

    /// <summary>Affichage : zone + quartiers.</summary>
    Task<Zone?> GetWithQuartiersAsync(int zoneId, CancellationToken ct = default);
}
