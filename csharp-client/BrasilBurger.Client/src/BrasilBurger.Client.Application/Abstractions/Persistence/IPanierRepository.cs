using BrasilBurger.Client.Domain.Entities;

namespace BrasilBurger.Client.Application.Abstractions.Persistence;

public interface IPanierRepository : IRepository<Panier>
{
    Task<Panier?> GetWithLignesAsync(int panierId, CancellationToken ct = default);
}
