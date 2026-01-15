using BrasilBurger.Client.Data;
using BrasilBurger.Client.Repositories.Interfaces;
using BrasilBurger.Client.Repositories.Implementations;
using BrasilBurger.Client.Services.Interfaces;
using BrasilBurger.Client.Services.Implementations;
using BrasilBurger.Client.Helpers;
using BrasilBurger.Client.Middleware;
using Microsoft.EntityFrameworkCore;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using System.Text;
using Microsoft.IdentityModel.Tokens;

namespace BrasilBurger.Client.Extensions;

public static class ServiceExtensions
{
    public static void AddApplicationServices(this IServiceCollection services, IConfiguration configuration)
    {
        // Database
        services.AddDbContext<AppDbContext>(options =>
            options.UseNpgsql(configuration.GetConnectionString("DefaultConnection")));

        // Repositories
        services.AddScoped<IAuthRepository, AuthRepository>();
        services.AddScoped<ICatalogRepository, CatalogRepository>();
        services.AddScoped<IOrderRepository, OrderRepository>();
        services.AddScoped<IPaymentMethodRepository, PaymentMethodRepository>();

        // Services
        services.AddScoped<IAuthService, AuthService>();
        services.AddScoped<ICatalogService, CatalogService>();
        services.AddScoped<IOrderService, OrderService>();
        services.AddScoped<ITokenService, TokenService>();
        services.AddSingleton<ICloudinaryService, CloudinaryService>();

        // Helpers
        services.AddSingleton<PasswordHasher>();

        // AutoMapper
        services.AddAutoMapper(typeof(AutoMapperProfile));

        // CORS
        services.AddCors(options =>
        {
            options.AddPolicy("AllowAll", builder =>
            {
                builder.AllowAnyOrigin()
                    .AllowAnyMethod()
                    .AllowAnyHeader();
            });
        });

        // JWT Authentication
        var jwtSecret = configuration["Jwt:Secret"];
        var key = Encoding.ASCII.GetBytes(jwtSecret);

        services.AddAuthentication(x =>
        {
            x.DefaultAuthenticateScheme = JwtBearerDefaults.AuthenticationScheme;
            x.DefaultChallengeScheme = JwtBearerDefaults.AuthenticationScheme;
        })
        .AddJwtBearer(x =>
        {
            x.RequireHttpsMetadata = false;
            x.SaveToken = true;
            x.TokenValidationParameters = new TokenValidationParameters
            {
                ValidateIssuerSigningKey = true,
                IssuerSigningKey = new SymmetricSecurityKey(key),
                ValidateIssuer = false,
                ValidateAudience = false,
                ValidateLifetime = true,
                ClockSkew = TimeSpan.Zero
            };
        });
    }
}
