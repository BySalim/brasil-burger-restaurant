using BrasilBurger.Client.Web.ViewModels.Mapper;
using BrasilBurger.Client.Web.FormatsApp;

namespace BrasilBurger.Client.Web;

public static class DependencyInjection
{
    public static IServiceCollection AddWeb(this IServiceCollection services)
    {
        // Mapper singleton :
        services.AddSingleton<ArticleMapperVm>();
        services.AddSingleton<ZoneMapperVm>();
        
        services.AddScoped<IFormatsApp, FormatsApp.FormatsAppImpl>();

        return services;
    }
}
