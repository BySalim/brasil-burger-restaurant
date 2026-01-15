using BrasilBurger.Client.Models.DTOs.Auth;
using BrasilBurger.Client.Models.Entities;
using BrasilBurger.Client.Repositories.Interfaces;
using BrasilBurger.Client.Services.Interfaces;
using BrasilBurger.Client.Helpers;

namespace BrasilBurger.Client.Services.Implementations;

public class AuthService : IAuthService
{
    private readonly IAuthRepository _authRepository;
    private readonly ITokenService _tokenService;
    private readonly PasswordHasher _passwordHasher;

    public AuthService(IAuthRepository authRepository, ITokenService tokenService, PasswordHasher passwordHasher)
    {
        _authRepository = authRepository;
        _tokenService = tokenService;
        _passwordHasher = passwordHasher;
    }

    public async Task<AuthResponseDto> LoginAsync(LoginRequestDto request)
    {
        var user = await _authRepository.GetUserByEmailAsync(request.Email);

        if (user == null || !await ValidatePasswordAsync(request.Password, user.PasswordHash))
        {
            return new AuthResponseDto
            {
                Success = false,
                Message = "Email ou mot de passe invalide"
            };
        }

        var token = _tokenService.GenerateToken(user.Id, user.Email);

        return new AuthResponseDto
        {
            Success = true,
            Message = "Connexion réussie",
            Token = token,
            User = new UserDto
            {
                Id = user.Id,
                Email = user.Email,
                CreatedAt = user.CreatedAt
            }
        };
    }

    public async Task<AuthResponseDto> RegisterAsync(RegisterRequestDto request)
    {
        if (await _authRepository.UserExistsByEmailAsync(request.Email))
        {
            return new AuthResponseDto
            {
                Success = false,
                Message = "Cet email est déjà utilisé"
            };
        }

        var passwordHash = _passwordHasher.HashPassword(request.Password);
        var user = new User
        {
            Email = request.Email,
            PasswordHash = passwordHash,
            CreatedAt = DateTime.UtcNow
        };

        var createdUser = await _authRepository.CreateUserAsync(user);
        var token = _tokenService.GenerateToken(createdUser.Id, createdUser.Email);

        return new AuthResponseDto
        {
            Success = true,
            Message = "Inscription réussie",
            Token = token,
            User = new UserDto
            {
                Id = createdUser.Id,
                Email = createdUser.Email,
                CreatedAt = createdUser.CreatedAt
            }
        };
    }

    public async Task<bool> ValidatePasswordAsync(string password, string hash)
    {
        return await Task.FromResult(_passwordHasher.VerifyPassword(password, hash));
    }
}
