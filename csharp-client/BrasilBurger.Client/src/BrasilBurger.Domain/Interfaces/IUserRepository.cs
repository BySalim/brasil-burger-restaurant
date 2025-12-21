using BrasilBurger.Domain.Entities;
using BrasilBurger.Domain.ValueObjects;

namespace BrasilBurger.Domain.Interfaces;

public interface IUserRepository : IRepository<User>
{
    Task<User?> GetByLoginAsync(string login);
    Task<User?> GetByTelephoneAsync(Telephone telephone);
    Task<IEnumerable<User>> GetClientsAsync();
    Task<IEnumerable<User>> GetGestionnairesAsync();
}