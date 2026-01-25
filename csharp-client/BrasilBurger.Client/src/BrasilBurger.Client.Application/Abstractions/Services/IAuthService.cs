using BrasilBurger.Client.Application.Common;
using BrasilBurger.Client.Application.Dtos;
using BrasilBurger.Client.Domain.Entities;

namespace BrasilBurger.Client.Application.Abstractions.Services;

/// <summary>
/// Service d'authentification et d'inscription
/// </summary>
public interface IAuthService
{
    Task<Result<Domain.Entities.Client>> RegisterClientAsync(RegisterClientDto dto, CancellationToken ct = default);
    
    Task<Utilisateur?> AuthenticateAsync(string login, string password, CancellationToken ct = default);
}
