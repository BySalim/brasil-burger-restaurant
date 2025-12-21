using BrasilBurger.Domain.Exceptions;

namespace BrasilBurger.Domain.Exceptions;

public class CommandeException : DomainException
{
    public CommandeException(string message) : base(message) { }
}