using BrasilBurger.Client.Domain.Entities;
using BrasilBurger.Client.Domain.Enums;

namespace BrasilBurger.Client.Application.Abstractions.Persistence;

public interface ICommandeRepository : IRepository<Commande>
{
    Task<Commande?> GetByNumeroAsync(string numCmd, CancellationToken ct = default);

    Task<Commande?> GetWithDetailsAsync(int commandeId, CancellationToken ct = default);

    Task<IReadOnlyList<Commande>> ListByClientAsync(int clientId, CancellationToken ct = default);
    Task<IReadOnlyList<Commande>> ListByClientWithDetailsAsync(int clientId, CancellationToken ct = default);

    Task<(IReadOnlyList<Commande> Items, int TotalCount)> SearchAsync(
        int clientId,
        string? code = null,
        DateTime? date = null,
        EtatCommande? etat = null,
        ModeRecuperation? modeRecuperation = null,
        int skip = 0,
        int take = 10,
        CancellationToken ct = default);
}
