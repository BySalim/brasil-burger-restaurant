using BrasilBurger.Client.Services.Interfaces;

namespace BrasilBurger.Client.Services.Implementations;

public class CloudinaryService : ICloudinaryService
{
    private readonly IConfiguration _configuration;

    public CloudinaryService(IConfiguration configuration)
    {
        _configuration = configuration;
    }

    public string GetImageUrl(string imageIdentifier)
    {
        if (string.IsNullOrEmpty(imageIdentifier))
            return null;

        // Si c'est déjà une URL complète
        if (imageIdentifier.StartsWith("http"))
            return imageIdentifier;

        // Construire l'URL Cloudinary
        var cloudName = _configuration["Cloudinary:CloudName"];
        return $"https://res.cloudinary.com/{cloudName}/image/upload/{imageIdentifier}";
    }

    public async Task<string> UploadImageAsync(Stream imageStream, string fileName)
    {
        // À implémenter selon votre intégration Cloudinary
        // Pour l'instant, retourner un chemin temporaire
        return await Task.FromResult($"uploads/{fileName}");
    }
}
