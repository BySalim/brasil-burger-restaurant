using BrasilBurger.Client.Domain.Entities;
using BrasilBurger.Client.Web.ViewModels.Shared;

namespace BrasilBurger.Client.Web.FormatsApp;

public interface IFormatsApp
{
    string GetPublicUrl(string publicId);
    
    string FormatPrice(int priceInCents);
    
    public string FormatQuartier(Quartier quartier, string template);

    public SelectOptionVm FormatQuartierOption(Quartier quartier, string template);
    
    public IReadOnlyList<SelectOptionVm> FormatQuartiers(IEnumerable<Quartier> quartiers, string template);
    
    string FormatPhoneNumber(string phone);

    string GetAppName();
    
    string GetCurrency();
}
