using BrasilBurger.Client.Application.Abstractions.Persistence;
using BrasilBurger.Client.Domain.Entities;
using Microsoft.EntityFrameworkCore;

namespace BrasilBurger.Client.Infrastructure.Persistence.Repositories;

public sealed class QuartierRepository : EfRepository<Quartier>, IQuartierRepository
{
    public QuartierRepository(BrasilBurgerDbContext db) : base(db) { }

    public async Task<IReadOnlyList<Quartier>> ListByZoneAsync(int zoneId, CancellationToken ct = default)
        => await Db.Quartiers.AsNoTracking()
            .Where(q => q.ZoneId == zoneId)
            .ToListAsync(ct);
}
