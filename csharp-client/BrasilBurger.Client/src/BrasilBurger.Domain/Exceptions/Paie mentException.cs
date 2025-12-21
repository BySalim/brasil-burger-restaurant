using BrasilBurger.Domain.Exceptions;

namespace BrasilBurger.Domain.Exceptions;

public class PaiementException : DomainException
{
    public PaiementException(string message) : base(message) { }
}