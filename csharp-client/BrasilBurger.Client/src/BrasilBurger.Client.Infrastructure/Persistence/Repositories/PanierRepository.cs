using BrasilBurger.Client.Application.Abstractions.Persistence;
using BrasilBurger.Client.Domain.Entities;
using Microsoft.EntityFrameworkCore;

namespace BrasilBurger.Client.Infrastructure.Persistence.Repositories;

public sealed class PanierRepository : EfRepository<Panier>, IPanierRepository
{
    public PanierRepository(BrasilBurgerDbContext db) : base(db) { }

    public Task<Panier?> GetWithLignesAsync(int panierId, CancellationToken ct = default)
        => Db.Paniers.AsNoTracking()
            .Include(p => p.Lignes)
                .ThenInclude(l => l.Article)
            .FirstOrDefaultAsync(p => p.Id == panierId, ct);
}
