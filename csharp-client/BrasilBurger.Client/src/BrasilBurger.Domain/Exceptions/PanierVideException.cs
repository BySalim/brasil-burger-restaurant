using BrasilBurger.Domain.Exceptions;

namespace BrasilBurger.Domain.Exceptions;

public class PanierVideException : DomainException
{
    public PanierVideException(string message) : base(message) { }
}