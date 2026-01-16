using BrasilBurger.Client.Infrastructure.Diagnostics;
using BrasilBurger.Client.Infrastructure.External.Cloudinary;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Polly;
using Polly.Extensions.Http;

namespace BrasilBurger.Client.Infrastructure;

public static class DependencyInjection
{
    public static IServiceCollection AddInfrastructure(this IServiceCollection services, IConfiguration config)
    {
        // Options
        services.AddOptions<CloudinaryOptions>()
            .Bind(config.GetSection(CloudinaryOptions.SectionName))
            .ValidateDataAnnotations()
            .Validate(o =>
                !string.IsNullOrWhiteSpace(o.CloudName) &&
                !string.IsNullOrWhiteSpace(o.ApiKey) &&
                !string.IsNullOrWhiteSpace(o.ApiSecret),
                "Cloudinary options invalides.");

        // DB probe (Neon)
        var neonCs = config.GetConnectionString("Neon") ?? "";
        services.AddSingleton<IDatabaseProbe>(_ => new NpgsqlDatabaseProbe(neonCs));

        // Cloudinary probe (HTTP + résilience)
        services.AddHttpClient<ICloudinaryProbe, CloudinaryPingProbe>()
            .AddPolicyHandler(HttpPolicyExtensions
                .HandleTransientHttpError()
                .WaitAndRetryAsync(3, retryAttempt => TimeSpan.FromMilliseconds(200 * retryAttempt)));

        return services;
    }
}
