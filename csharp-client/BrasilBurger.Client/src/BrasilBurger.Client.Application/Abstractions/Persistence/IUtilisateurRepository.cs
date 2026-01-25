using BrasilBurger.Client.Domain.Entities;

namespace BrasilBurger.Client.Application.Abstractions.Persistence;

public interface IUtilisateurRepository : IRepository<Utilisateur>
{
    Task<Utilisateur?> GetByLoginAsync(string login, CancellationToken ct = default);
    
    Task<Utilisateur?> GetByTelephoneAsync(string telephone, CancellationToken ct = default);
    
    Task<Utilisateur?> GetWithDefaultsAsync(int utilisateurId, CancellationToken ct = default);
}
