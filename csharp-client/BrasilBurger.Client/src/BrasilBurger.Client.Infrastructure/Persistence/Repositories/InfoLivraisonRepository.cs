using BrasilBurger.Client.Application.Abstractions.Persistence;
using BrasilBurger.Client.Domain.Entities;
using Microsoft.EntityFrameworkCore;

namespace BrasilBurger.Client.Infrastructure.Persistence.Repositories;

public sealed class InfoLivraisonRepository : EfRepository<InfoLivraison>, IInfoLivraisonRepository
{
    public InfoLivraisonRepository(BrasilBurgerDbContext db) : base(db) { }

    public Task<InfoLivraison?> GetWithZoneQuartierAsync(int infoLivraisonId, CancellationToken ct = default)
        => Db.InfoLivraisons.AsNoTracking()
            .Include(i => i.Zone)
            .Include(i => i.Quartier)
            .FirstOrDefaultAsync(i => i.Id == infoLivraisonId, ct);
}
