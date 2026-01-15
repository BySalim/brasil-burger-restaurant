namespace BrasilBurger.Client.Services.Interfaces;

public interface ITokenService
{
    string GenerateToken(int userId, string email);
    (int UserId, string Email) ValidateToken(string token);
}
