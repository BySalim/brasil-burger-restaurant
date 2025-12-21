namespace BrasilBurger.Domain.ValueObjects;

public class ImageInfo
{
    public string PublicId { get; private set; }
    public string Url { get; private set; }

    private ImageInfo(string publicId, string url)
    {
        PublicId = publicId ?? throw new ArgumentNullException(nameof(publicId));
        Url = url ?? throw new ArgumentNullException(nameof(url));
    }

    public static ImageInfo From(string publicId, string url) => new ImageInfo(publicId, url);
}