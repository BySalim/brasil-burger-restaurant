namespace BrasilBurger.Client.Application.Abstractions.External;

public interface ICloudinaryUrlService
{
    string GetPublicUrl(string publicId);
}
