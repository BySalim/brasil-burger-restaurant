using CloudinaryDotNet;
using CloudinaryDotNet.Actions;
using BrasilBurger.Domain.Interfaces;
using BrasilBurger.Domain.ValueObjects;
using BrasilBurger.Domain.Exceptions;

namespace BrasilBurger.Infrastructure.ImageStorage;

public class CloudinaryImageStorageService : IImageStorageService
{
    private readonly Cloudinary _cloudinary;

    public CloudinaryImageStorageService(Cloudinary cloudinary)
    {
        _cloudinary = cloudinary;
    }

    public async Task<ImageInfo> UploadAsync(Stream imageStream, string fileName)
    {
        var uploadParams = new ImageUploadParams
        {
            File = new FileDescription(fileName, imageStream),
            Folder = "brasil-burger"
        };

        var uploadResult = await _cloudinary.UploadAsync(uploadParams);
        if (uploadResult.Error != null)
            throw new ImageUploadException($"Erreur upload image: {uploadResult.Error.Message}");

        return ImageInfo.From(uploadResult.PublicId, uploadResult.SecureUrl.AbsoluteUri);
    }

    public async Task DeleteAsync(string publicId)
    {
        var deleteParams = new DeletionParams(publicId);
        var result = await _cloudinary.DestroyAsync(deleteParams);
        if (result.Error != null)
            throw new ImageUploadException($"Erreur suppression image: {result.Error.Message}");
    }

    public async Task<string> GetUrlAsync(string publicId)
    {
        return _cloudinary.Api.UrlImgUp.BuildUrl(publicId);
    }
}