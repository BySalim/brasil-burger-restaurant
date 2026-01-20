using BrasilBurger.Client.Domain.Entities;

namespace BrasilBurger.Client.Application.Abstractions.Persistence;

public interface IInfoLivraisonRepository : IRepository<InfoLivraison>
{
    /// <summary>Affichage : info + zone + quartier.</summary>
    Task<InfoLivraison?> GetWithZoneQuartierAsync(int infoLivraisonId, CancellationToken ct = default);
}
