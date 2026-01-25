using BrasilBurger.Client.Domain.Enums;

namespace BrasilBurger.Client.Application.Abstractions.Security;

public interface IUserSession
{
    bool IsAuthenticated { get; }

    int? UserId { get; }
    string? Login { get; }
    string? FullName { get; }
    Role? Role { get; }
}
