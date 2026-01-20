using BrasilBurger.Client.Domain.Entities;

namespace BrasilBurger.Client.Application.Abstractions.Persistence;

public interface ICommandeRepository : IRepository<Commande>
{
    Task<Commande?> GetByNumeroAsync(string numCmd, CancellationToken ct = default);

    /// <summary>Affichage : commande + client + panier(lignes+article) + infoLivraison(+zone/quartier) + paiement.</summary>
    Task<Commande?> GetWithDetailsAsync(int commandeId, CancellationToken ct = default);

    Task<IReadOnlyList<Commande>> ListByClientAsync(int clientId, CancellationToken ct = default);
    Task<IReadOnlyList<Commande>> ListByClientWithDetailsAsync(int clientId, CancellationToken ct = default);
}
