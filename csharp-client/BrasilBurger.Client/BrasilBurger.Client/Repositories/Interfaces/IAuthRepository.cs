using BrasilBurger.Client.Models.Entities;

namespace BrasilBurger.Client.Repositories.Interfaces;

public interface IAuthRepository
{
    Task<User> GetUserByEmailAsync(string email);
    Task<User> CreateUserAsync(User user);
    Task<bool> UserExistsByEmailAsync(string email);
}
