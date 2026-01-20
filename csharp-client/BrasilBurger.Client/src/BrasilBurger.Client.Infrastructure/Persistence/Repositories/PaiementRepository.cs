using BrasilBurger.Client.Application.Abstractions.Persistence;
using BrasilBurger.Client.Domain.Entities;
using Microsoft.EntityFrameworkCore;

namespace BrasilBurger.Client.Infrastructure.Persistence.Repositories;

public sealed class PaiementRepository : EfRepository<Paiement>, IPaiementRepository
{
    public PaiementRepository(BrasilBurgerDbContext db) : base(db) { }

    public Task<Paiement?> GetByReferenceExterneAsync(string referenceExterne, CancellationToken ct = default)
        => Db.Paiements.AsNoTracking()
            .FirstOrDefaultAsync(p => p.ReferencePaiementExterne == referenceExterne, ct);

    public Task<Paiement?> GetWithCommandeAsync(int paiementId, CancellationToken ct = default)
        => Db.Paiements.AsNoTracking()
            .Include(p => p.Commande)
            .Include(p => p.Client)
            .FirstOrDefaultAsync(p => p.Id == paiementId, ct);
}
