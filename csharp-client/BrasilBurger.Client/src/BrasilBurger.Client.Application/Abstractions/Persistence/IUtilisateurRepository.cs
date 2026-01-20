using BrasilBurger.Client.Domain.Entities;

namespace BrasilBurger.Client.Application.Abstractions.Persistence;

public interface IUtilisateurRepository : IRepository<Utilisateur>
{
    Task<Utilisateur?> GetByLoginAsync(string login, CancellationToken ct = default);

    /// <summary>
    /// Pour l'affichage et/ou la connexion : inclut quartier défaut, etc.
    /// À implémenter côté Infrastructure via Include(...) si EF Core.
    /// </summary>
    Task<Utilisateur?> GetWithDefaultsAsync(int utilisateurId, CancellationToken ct = default);
}
