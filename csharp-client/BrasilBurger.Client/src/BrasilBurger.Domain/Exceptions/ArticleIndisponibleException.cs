using BrasilBurger.Domain.Exceptions;

namespace BrasilBurger.Domain.Exceptions;

public class ArticleIndisponibleException : DomainException
{
    public ArticleIndisponibleException(string message) : base(message) { }
}