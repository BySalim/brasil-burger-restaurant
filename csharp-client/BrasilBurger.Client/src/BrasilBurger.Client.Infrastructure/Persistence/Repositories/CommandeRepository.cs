using BrasilBurger.Client.Application.Abstractions.Persistence;
using BrasilBurger.Client.Domain.Entities;
using Microsoft.EntityFrameworkCore;

namespace BrasilBurger.Client.Infrastructure.Persistence.Repositories;

public sealed class CommandeRepository : EfRepository<Commande>, ICommandeRepository
{
    public CommandeRepository(BrasilBurgerDbContext db) : base(db) { }

    public Task<Commande?> GetByNumeroAsync(string numCmd, CancellationToken ct = default)
        => Db.Commandes.AsNoTracking()
            .FirstOrDefaultAsync(c => c.NumCmd == numCmd, ct);

    public Task<Commande?> GetWithDetailsAsync(int commandeId, CancellationToken ct = default)
        => Db.Commandes.AsNoTracking()
            .Include(c => c.Client)
            .Include(c => c.Panier)
                .ThenInclude(p => p!.Lignes)
                    .ThenInclude(l => l.Article)
            .Include(c => c.InfoLivraison)
                .ThenInclude(i => i!.Zone)
            .Include(c => c.InfoLivraison)
                .ThenInclude(i => i!.Quartier)
            .Include(c => c.Paiement)
            .Include(c => c.Livraison)
            .FirstOrDefaultAsync(c => c.Id == commandeId, ct);

    public async Task<IReadOnlyList<Commande>> ListByClientAsync(int clientId, CancellationToken ct = default)
        => await Db.Commandes.AsNoTracking()
            .Where(c => c.ClientId == clientId)
            .OrderByDescending(c => c.DateDebut)
            .ToListAsync(ct);

    public async Task<IReadOnlyList<Commande>> ListByClientWithDetailsAsync(int clientId, CancellationToken ct = default)
        => await Db.Commandes.AsNoTracking()
            .Where(c => c.ClientId == clientId)
            .Include(c => c.Panier)
                .ThenInclude(p => p!.Lignes)
                    .ThenInclude(l => l.Article)
            .Include(c => c.InfoLivraison)
                .ThenInclude(i => i!.Zone)
            .Include(c => c.InfoLivraison)
                .ThenInclude(i => i!.Quartier)
            .Include(c => c.Paiement)
            .OrderByDescending(c => c.DateDebut)
            .ToListAsync(ct);

}
