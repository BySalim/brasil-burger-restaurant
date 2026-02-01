using BrasilBurger.Client.Application.Abstractions.Persistence;
using BrasilBurger.Client.Domain.Entities;
using BrasilBurger.Client.Domain.Enums;
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

        public async Task<(IReadOnlyList<Commande> Items, int TotalCount)> SearchAsync(
        int clientId,
        string? code = null,
        DateTime? date = null,
        EtatCommande? etat = null,
        ModeRecuperation? modeRecuperation = null,
        int skip = 0,
        int take = 10,
        CancellationToken ct = default)
    {
        var query = Db.Commandes.AsNoTracking()
            .Where(c => c.ClientId == clientId);

        if (!string.IsNullOrWhiteSpace(code))
        {
            query = query.Where(c => c.NumCmd.Contains(code));
        }

        if (date.HasValue)
        {
            var dateOnly = date.Value.Date;
            query = query.Where(c => c.DateDebut.Date == dateOnly);
        }

        if (etat.HasValue)
        {
            query = query.Where(c => c.Etat == etat.Value);
        }

        if (modeRecuperation.HasValue)
        {
            query = query.Where(c => c.TypeRecuperation == modeRecuperation.Value);
        }

        var totalCount = await query.CountAsync(ct);

        var items = await query
            .OrderByDescending(c => c.DateDebut)
            .Skip(skip)
            .Take(take)
            .Include(c => c.Livraison)
            .ToListAsync(ct);

        return (items, totalCount);
    }

}
