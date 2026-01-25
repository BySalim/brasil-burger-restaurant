using BrasilBurger.Client.Domain.Entities;

namespace BrasilBurger.Client.Application.Abstractions.Persistence;

public interface ICommandeRepository : IRepository<Commande>
{
    Task<Commande?> GetByNumeroAsync(string numCmd, CancellationToken ct = default);

    Task<Commande?> GetWithDetailsAsync(int commandeId, CancellationToken ct = default);

    Task<IReadOnlyList<Commande>> ListByClientAsync(int clientId, CancellationToken ct = default);
    Task<IReadOnlyList<Commande>> ListByClientWithDetailsAsync(int clientId, CancellationToken ct = default);
}
