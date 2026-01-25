using BrasilBurger.Client.Application.Abstractions.Persistence;
using BrasilBurger.Client.Domain.Entities;
using Microsoft.EntityFrameworkCore;

namespace BrasilBurger.Client.Infrastructure.Persistence.Repositories;

public sealed class UtilisateurRepository : EfRepository<Utilisateur>, IUtilisateurRepository
{
    public UtilisateurRepository(BrasilBurgerDbContext db) : base(db) { }

    public Task<Utilisateur?> GetByLoginAsync(string login, CancellationToken ct = default)
        => Db.Utilisateurs
            .FirstOrDefaultAsync(u => u.Login == login, ct);

    public Task<Utilisateur?> GetByTelephoneAsync(string telephone, CancellationToken ct = default)
        => Db.Utilisateurs
            .FirstOrDefaultAsync(u => u.Telephone == telephone, ct);

    public Task<Utilisateur?> GetWithDefaultsAsync(int utilisateurId, CancellationToken ct = default)
        => Db.Utilisateurs
            .Include(u => u.QuartierLivraisonDefaut)
            .FirstOrDefaultAsync(u => u.Id == utilisateurId, ct);
}
