using BrasilBurger.Domain.Entities;
using BrasilBurger.Domain.Interfaces;
using BrasilBurger.Domain.ValueObjects;
using BrasilBurger.Infrastructure.Persistence.Repositories;

namespace BrasilBurger.Infrastructure.Persistence.Repositories;

public class UserRepository : Repository<User>, IUserRepository
{
    public UserRepository(BrasilBurgerDbContext context) : base(context) { }

    public async Task<User?> GetByLoginAsync(string login)
    {
        return await _context.Users.FirstOrDefaultAsync(u => u.Login == login);
    }

    public async Task<User?> GetByTelephoneAsync(Telephone telephone)
    {
        return await _context.Users.FirstOrDefaultAsync(u => u.Telephone == telephone);
    }

    public async Task<IEnumerable<User>> GetClientsAsync()
    {
        return await _context.Users.Where(u => u.Role == Role.Client).ToListAsync();
    }

    public async Task<IEnumerable<User>> GetGestionnairesAsync()
    {
        return await _context.Users.Where(u => u.Role == Role.Gestionnaire).ToListAsync();
    }
}