using BrasilBurger.Client.Domain.Entities;

namespace BrasilBurger.Client.Application.Abstractions.Persistence;

public interface IInfoLivraisonRepository : IRepository<InfoLivraison>
{
    Task<InfoLivraison?> GetWithZoneQuartierAsync(int infoLivraisonId, CancellationToken ct = default);
}
