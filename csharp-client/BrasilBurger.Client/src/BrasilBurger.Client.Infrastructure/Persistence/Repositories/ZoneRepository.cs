using BrasilBurger.Client.Application.Abstractions.Persistence;
using BrasilBurger.Client.Domain.Entities;
using Microsoft.EntityFrameworkCore;

namespace BrasilBurger.Client.Infrastructure.Persistence.Repositories;

public sealed class ZoneRepository : EfRepository<Zone>, IZoneRepository
{
    public ZoneRepository(BrasilBurgerDbContext db) : base(db) { }

    public async Task<IReadOnlyList<Zone>> ListActivesAsync(CancellationToken ct = default)
        => await Db.Zones.AsNoTracking()
            .Where(z => !z.EstArchiver)
            .ToListAsync(ct);

    public Task<Zone?> GetWithQuartiersAsync(int zoneId, CancellationToken ct = default)
        => Db.Zones.AsNoTracking()
            .Include(z => z.Quartiers)
            .FirstOrDefaultAsync(z => z.Id == zoneId, ct);
}
