using BrasilBurger.Client.Domain.Entities;

namespace BrasilBurger.Client.Application.Abstractions.Persistence;

public interface IQuartierRepository : IRepository<Quartier>
{
    Task<IReadOnlyList<Quartier>> ListByZoneAsync(int zoneId, CancellationToken ct = default);
    
    Task<Quartier?> GetWithZoneAsync(int quartierId, CancellationToken ct = default);

    Task<IReadOnlyList<Quartier>> ListWithZonesAsync(CancellationToken ct = default);
}
