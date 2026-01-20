using BrasilBurger.Client.Domain.Entities;

namespace BrasilBurger.Client.Application.Abstractions.Persistence;

public interface IPaiementRepository : IRepository<Paiement>
{
    Task<Paiement?> GetByReferenceExterneAsync(string referenceExterne, CancellationToken ct = default);

    /// <summary>Affichage : paiement + commande (+ client si présent).</summary>
    Task<Paiement?> GetWithCommandeAsync(int paiementId, CancellationToken ct = default);
}
