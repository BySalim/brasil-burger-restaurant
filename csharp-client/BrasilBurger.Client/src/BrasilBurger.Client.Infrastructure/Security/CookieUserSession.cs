using System.Security.Claims;
using BrasilBurger.Client.Application.Abstractions.Security;
using BrasilBurger.Client.Domain.Enums;
using Microsoft.AspNetCore.Http;

namespace BrasilBurger.Client.Infrastructure.Security;

public sealed class CookieUserSession : IUserSession
{
    private readonly IHttpContextAccessor _http;

    public CookieUserSession(IHttpContextAccessor http) => _http = http;

    private ClaimsPrincipal? User => _http.HttpContext?.User;

    public bool IsAuthenticated => User?.Identity?.IsAuthenticated == true;

    public int? UserId
        => TryGetInt("uid")
           ?? TryGetInt(ClaimTypes.NameIdentifier);

    public string? Login
        => User?.FindFirst("login")?.Value
           ?? User?.Identity?.Name;
    
    public string? FullName
        => User?.FindFirst(ClaimTypes.Name)?.Value;

    public Role? Role
        => TryGetEnum<Role>("role");

    private int? TryGetInt(string claimType)
    {
        var v = User?.FindFirst(claimType)?.Value;
        return int.TryParse(v, out var id) ? id : null;
    }

    private TEnum? TryGetEnum<TEnum>(string claimType) where TEnum : struct, Enum
    {
        var v = User?.FindFirst(claimType)?.Value;
        return Enum.TryParse<TEnum>(v, ignoreCase: true, out var e) ? e : null;
    }
}
