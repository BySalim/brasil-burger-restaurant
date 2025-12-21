using Microsoft.AspNetCore.Builder;

namespace BrasilBurger.Web.Extensions;

public static class WebApplicationExtensions
{
    public static WebApplication AddInfrastructureServices(this WebApplicationBuilder builder)
    {
        // Ajouter les services infrastructure ici si nécessaire
        return builder.Build();
    }
}