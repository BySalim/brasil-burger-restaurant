using BrasilBurger.Domain.Entities;

namespace BrasilBurger.Application.Interfaces;

public interface IAuthenticationService
{
    string GenerateToken(User user);
    bool VerifyPassword(string password, string hashedPassword);
    string HashPassword(string password);
}