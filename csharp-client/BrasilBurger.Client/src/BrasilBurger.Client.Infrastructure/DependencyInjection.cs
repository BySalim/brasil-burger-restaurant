using BrasilBurger.Client.Application.Abstractions.External;
using BrasilBurger.Client.Application.Abstractions.Persistence;
using BrasilBurger.Client.Application.Abstractions.Security;
using BrasilBurger.Client.Infrastructure.Diagnostics;
using BrasilBurger.Client.Infrastructure.External.Cloudinary;
using BrasilBurger.Client.Infrastructure.External.Payments;
using BrasilBurger.Client.Infrastructure.Persistence;
using BrasilBurger.Client.Infrastructure.Persistence.Repositories;
using BrasilBurger.Client.Infrastructure.Security;
using BrasilBurger.Client.Application.Abstractions.Services;
using BrasilBurger.Client.Application.Services;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Polly.Extensions.Http;
using Polly;



namespace BrasilBurger.Client.Infrastructure;

public static class DependencyInjection
{
    public static IServiceCollection AddInfrastructure(this IServiceCollection services, IConfiguration config)
    {
        
        // ----------------------------
        // Options App
        // ----------------------------
        services.Configure<AppSettings>(config.GetSection(AppSettings.SectionName));
        
        // ----------------------------
        // Options Cloudinary
        // ----------------------------
        services.AddOptions<CloudinaryOptions>()
            .Bind(config.GetSection(CloudinaryOptions.SectionName))
            .ValidateDataAnnotations()
            .Validate(o =>
                    !string.IsNullOrWhiteSpace(o.CloudName) &&
                    !string.IsNullOrWhiteSpace(o.ApiKey) &&
                    !string.IsNullOrWhiteSpace(o.ApiSecret),
                "Cloudinary options invalides.");

        services.AddSingleton<ICloudinaryUrlService, CloudinaryUrlService>();

        // ----------------------------
        // Options Security
        // ----------------------------
        services.AddOptions<SecurityOptions>()
            .Bind(config.GetSection(SecurityOptions.SectionName))
            .ValidateDataAnnotations()
            .Validate(o => !string.IsNullOrWhiteSpace(o.AppSecret), "Security:AppSecret manquant.");

        services.AddHttpContextAccessor();
        services.AddScoped<IUserSession, CookieUserSession>();
        services.AddSingleton<IPasswordHasher, BcryptPasswordHasher>();

        // ----------------------------
        // Options Payments + Gateway
        // ----------------------------
        services.AddOptions<PaymentOptions>()
            .Bind(config.GetSection(PaymentOptions.SectionName));

        // Providers réels (HttpClient)
        services.AddHttpClient<WaveRealProvider>();
        services.AddHttpClient<OrangeMoneyRealProvider>();

        // Providers fake/réels
        services.AddSingleton<IPaymentProvider, WaveFakeProvider>();
        services.AddSingleton<IPaymentProvider, OrangeMoneyFakeProvider>();
        services.AddScoped<IPaymentProvider, WaveRealProvider>();
        services.AddScoped<IPaymentProvider, OrangeMoneyRealProvider>();

        services.AddScoped<IPaymentGateway, PaymentGateway>();

        // ----------------------------
        // Connection Neon + EF Core
        // ----------------------------
        var neonCs = config.GetConnectionString("Neon") ?? "";
        if (string.IsNullOrWhiteSpace(neonCs))
            throw new InvalidOperationException("ConnectionString 'Neon' manquante.");

        services.AddDbContext<BrasilBurgerDbContext>(opt => opt.UseNpgsql(neonCs));

        // ----------------------------
        // Repositories + UnitOfWork
        // ----------------------------
        services.AddScoped<IArticleRepository, ArticleRepository>();
        services.AddScoped<IZoneRepository, ZoneRepository>();
        services.AddScoped<IQuartierRepository, QuartierRepository>();
        services.AddScoped<IUtilisateurRepository, UtilisateurRepository>();
        services.AddScoped<IArticleQuantifierRepository, ArticleQuantifierRepository>();
        services.AddScoped<IPanierRepository, PanierRepository>();
        services.AddScoped<IInfoLivraisonRepository, InfoLivraisonRepository>();
        services.AddScoped<ICommandeRepository, CommandeRepository>();
        services.AddScoped<IPaiementRepository, PaiementRepository>();

        services.AddScoped<IUnitOfWork, UnitOfWork>();
        
        // ----------------------------
        // Application Services
        // ----------------------------
        services.AddScoped<IAuthService, AuthService>();
        services.AddScoped<ICommandeService, CommandeService>();
        services.AddScoped<IPaiementService, PaiementService>();

        // ----------------------------
        // Probes (Diagnostics)
        // ----------------------------
        services.AddSingleton<IDatabaseProbe>(_ => new NpgsqlDatabaseProbe(neonCs));

        services.AddHttpClient<ICloudinaryProbe, CloudinaryPingProbe>()
            .AddPolicyHandler(HttpPolicyExtensions
                .HandleTransientHttpError()
                .WaitAndRetryAsync(3, retryAttempt => TimeSpan.FromMilliseconds(200 * retryAttempt)));

        return services;
    }
}
