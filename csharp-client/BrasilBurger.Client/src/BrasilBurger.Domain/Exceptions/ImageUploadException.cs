using BrasilBurger.Domain.Exceptions;

namespace BrasilBurger.Domain.Exceptions;

public class ImageUploadException : DomainException
{
    public ImageUploadException(string message) : base(message) { }
}