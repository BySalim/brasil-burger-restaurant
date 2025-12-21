using BrasilBurger.Domain.ValueObjects;

namespace BrasilBurger.Domain.Interfaces;

public interface IImageStorageService
{
    Task<ImageInfo> UploadAsync(Stream imageStream, string fileName);
    Task DeleteAsync(string publicId);
    Task<string> GetUrlAsync(string publicId);
}