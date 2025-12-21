namespace BrasilBurger.Application.Interfaces;

public interface ICurrentUserService
{
    int? UserId { get; }
    string? UserRole { get; }
}