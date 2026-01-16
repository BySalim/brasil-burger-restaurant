using System.Net.Http.Headers;
using System.Text;

using BrasilBurger.Client.Infrastructure.External.Cloudinary;
using Microsoft.Extensions.Options;

namespace BrasilBurger.Client.Infrastructure.Diagnostics;

public sealed class CloudinaryPingProbe : ICloudinaryProbe
{
    private readonly HttpClient _http;
    private readonly CloudinaryOptions _options;

    public CloudinaryPingProbe(HttpClient http, IOptions<CloudinaryOptions> options)
    {
        _http = http;
        _options = options.Value;
    }

    public async Task<ProbeResult> TestAsync(CancellationToken ct)
    {
        if (string.IsNullOrWhiteSpace(_options.CloudName) ||
            string.IsNullOrWhiteSpace(_options.ApiKey) ||
            string.IsNullOrWhiteSpace(_options.ApiSecret))
        {
            return new ProbeResult(false, "Cloudinary KO: config manquante (Cloudinary:CloudName/ApiKey/ApiSecret).");
        }

        try
        {
            // Endpoint courant : https://api.cloudinary.com/v1_1/{cloud_name}/ping
            var url = $"https://api.cloudinary.com/v1_1/{_options.CloudName}/ping";

            var authRaw = $"{_options.ApiKey}:{_options.ApiSecret}";
            var auth = Convert.ToBase64String(Encoding.UTF8.GetBytes(authRaw));

            using var req = new HttpRequestMessage(HttpMethod.Get, url);
            req.Headers.Authorization = new AuthenticationHeaderValue("Basic", auth);

            using var res = await _http.SendAsync(req, ct);
            var body = await res.Content.ReadAsStringAsync(ct);

            return res.IsSuccessStatusCode
                ? new ProbeResult(true, "Cloudinary OK (ping).")
                : new ProbeResult(false, $"Cloudinary KO: HTTP {(int)res.StatusCode} - {body}");
        }
        catch (Exception ex)
        {
            return new ProbeResult(false, $"Cloudinary KO: {ex.GetType().Name} - {ex.Message}");
        }
    }
}
