namespace BrasilBurger.Client.Services.Interfaces;

public interface ICloudinaryService
{
    string GetImageUrl(string imageIdentifier);
    Task<string> UploadImageAsync(Stream imageStream, string fileName);
}
