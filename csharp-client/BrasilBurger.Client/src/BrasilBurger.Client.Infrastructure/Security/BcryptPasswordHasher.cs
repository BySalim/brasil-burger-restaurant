using BrasilBurger.Client.Application.Abstractions.Security;

namespace BrasilBurger.Client.Infrastructure.Security;

public sealed class BcryptPasswordHasher : IPasswordHasher
{
    public string Hash(string password)
        => BCrypt.Net.BCrypt.HashPassword(password);

    public bool Verify(string hashedPassword, string providedPassword)
    {
        // Compat Symfony/PHP : certains libs .NET n'acceptent pas toujours $2y$
        if (!string.IsNullOrWhiteSpace(hashedPassword) && hashedPassword.StartsWith("$2y$"))
            hashedPassword = "$2a$" + hashedPassword.Substring(4);

        return BCrypt.Net.BCrypt.Verify(providedPassword, hashedPassword);
    }
}
