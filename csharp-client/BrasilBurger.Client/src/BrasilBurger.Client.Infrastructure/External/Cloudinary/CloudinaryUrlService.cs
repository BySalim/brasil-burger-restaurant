using BrasilBurger.Client.Application.Abstractions.External;
using Microsoft.Extensions.Options;

namespace BrasilBurger.Client.Infrastructure.External.Cloudinary;

public sealed class CloudinaryUrlService : ICloudinaryUrlService
{
    private readonly CloudinaryOptions _opt;

    public CloudinaryUrlService(IOptions<CloudinaryOptions> opt) => _opt = opt.Value;

    public string GetPublicUrl(string publicId)
    {
        if (string.IsNullOrWhiteSpace(publicId))
            return string.Empty;
            
        return $"https://res.cloudinary.com/{_opt.CloudName}/image/upload/{publicId}";
    }
}
