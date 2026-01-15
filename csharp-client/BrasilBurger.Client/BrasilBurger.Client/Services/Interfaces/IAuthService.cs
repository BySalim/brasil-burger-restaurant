using BrasilBurger.Client.Models.DTOs.Auth;

namespace BrasilBurger.Client.Services.Interfaces;

public interface IAuthService
{
    Task<AuthResponseDto> LoginAsync(LoginRequestDto request);
    Task<AuthResponseDto> RegisterAsync(RegisterRequestDto request);
    Task<bool> ValidatePasswordAsync(string password, string hash);
}
