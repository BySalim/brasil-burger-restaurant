using BrasilBurger.Client.Web.ViewModels.MapperVm;
using BrasilBurger.Client.Web.FormatsApp;
using BrasilBurger.Client.Web.Mappers;

namespace BrasilBurger.Client.Web;

public static class DependencyInjection
{
    public static IServiceCollection AddWeb(this IServiceCollection services)
    {
        // Mapper singleton :
        services.AddSingleton<ArticleMapperVm>();
        services.AddSingleton<ArticleCardItemVmMapper>();
        
        services.AddScoped<IFormatsApp, FormatsApp.FormatsAppImpl>();

        return services;
    }
}
