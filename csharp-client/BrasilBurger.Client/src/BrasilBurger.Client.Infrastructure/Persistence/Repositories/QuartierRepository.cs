using BrasilBurger.Client.Application.Abstractions.Persistence;
using BrasilBurger.Client.Domain.Entities;
using Microsoft.EntityFrameworkCore;

namespace BrasilBurger.Client.Infrastructure.Persistence.Repositories;

public sealed class QuartierRepository : EfRepository<Quartier>, IQuartierRepository
{
    public QuartierRepository(BrasilBurgerDbContext db) : base(db) { }

    // ✅ Override GetByIdAsync pour inclure automatiquement la Zone
    public override Task<Quartier?> GetByIdAsync(int id, CancellationToken ct = default)
        => Db.Quartiers
            .Include(q => q.Zone)
            .FirstOrDefaultAsync(q => q.Id == id, ct);

    public async Task<IReadOnlyList<Quartier>> ListByZoneAsync(int zoneId, CancellationToken ct = default)
        => await Db.Quartiers
            .AsNoTracking()
            .Include(q => q.Zone)
            .Where(q => q.ZoneId == zoneId)
            .ToListAsync(ct);

    public Task<Quartier?> GetWithZoneAsync(int quartierId, CancellationToken ct = default)
        => Db.Quartiers
            .AsNoTracking()
            .Include(q => q.Zone)
            .FirstOrDefaultAsync(q => q.Id == quartierId, ct);

    public async Task<IReadOnlyList<Quartier>> ListWithZonesAsync(CancellationToken ct = default)
        => await Db.Quartiers
            .AsNoTracking()
            .Include(q => q.Zone)
            .ToListAsync(ct);
}
