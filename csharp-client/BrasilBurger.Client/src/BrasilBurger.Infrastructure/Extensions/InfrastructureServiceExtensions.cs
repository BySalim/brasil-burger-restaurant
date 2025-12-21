using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using BrasilBurger.Domain.Interfaces;
using BrasilBurger.Application.Interfaces;
using BrasilBurger.Infrastructure.Persistence;
using BrasilBurger.Infrastructure.Persistence.Repositories;
using BrasilBurger.Infrastructure.ImageStorage;
using BrasilBurger.Infrastructure.Payment;
using BrasilBurger.Infrastructure.Authentication;
using BrasilBurger.Infrastructure.Services;

namespace BrasilBurger.Infrastructure.Extensions;

public static class InfrastructureServiceExtensions
{
    public static IServiceCollection AddInfrastructure(this IServiceCollection services, IConfiguration configuration)
    {
        // DbContext
        services.AddDbContext<BrasilBurgerDbContext>(options =>
            options.UseNpgsql(configuration.GetConnectionString("DefaultConnection")));

        // Repositories
        services.AddScoped<IUserRepository, UserRepository>();
        services.AddScoped<IArticleRepository, ArticleRepository>();
        // Ajouter les autres...

        // Services
        services.AddScoped<IImageStorageService, CloudinaryImageStorageService>();
        services.AddScoped<ICalculateurPrixService, CalculateurPrixService>();
        services.AddScoped<IGenerateurNumeroCommandeService, GenerateurNumeroCommandeService>();

        // Authentication
        services.AddScoped<IAuthenticationService>(sp =>
            new AuthenticationService(configuration["JWT_SECRET_KEY"]));

        // Payment
        services.AddScoped<IPaymentProvider, FakePaymentProvider>(); // Changer selon mode
        services.AddScoped<PaymentService>();

        return services;
    }
}