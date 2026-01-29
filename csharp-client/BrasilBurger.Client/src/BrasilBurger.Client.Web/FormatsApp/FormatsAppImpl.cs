using BrasilBurger.Client.Application.Abstractions.External;
using BrasilBurger.Client.Domain.Entities;
using BrasilBurger.Client.Infrastructure;
using BrasilBurger.Client.Web.ViewModels.Shared;
using Microsoft.Extensions.Options;

namespace BrasilBurger.Client.Web.FormatsApp;

public sealed class FormatsAppImpl : IFormatsApp
{
    private readonly AppSettings _appSettings;
    private readonly ICloudinaryUrlService _cloudinaryUrlService;

    public FormatsAppImpl(IOptions<AppSettings> appSettings, ICloudinaryUrlService cloudinaryUrlService)
    {
        _appSettings = appSettings.Value;
        _cloudinaryUrlService = cloudinaryUrlService;
    }
    
    public string GetPublicUrl(string publicId)
    {
        return _cloudinaryUrlService.GetPublicUrl(publicId);
    }

    public string FormatPrice(int priceInCents)
    {
        return $"{priceInCents:N0} {_appSettings.Currency}".Replace(",", " ");
    }

    public string FormatQuartier(Quartier quartier, string template)
    {
        if (quartier is null) throw new ArgumentNullException(nameof(quartier));
        if (string.IsNullOrWhiteSpace(template)) throw new ArgumentException("Template requis.", nameof(template));

        return template
            .Replace("{quartier}", quartier.Nom)
            .Replace("{zone}", quartier.Zone?.Nom ?? string.Empty)
            .Replace("{price}", quartier.Zone is null ? string.Empty : FormatPrice(quartier.Zone.PrixLivraison));
    }


    public SelectOptionVm FormatQuartierOption(Quartier quartier, string template)
    {
        if (quartier is null) throw new ArgumentNullException(nameof(quartier));

        var text = FormatQuartier(quartier, template);

        return new SelectOptionVm(quartier.Id.ToString(), text);
    }

    public IReadOnlyList<SelectOptionVm> FormatQuartiers(IEnumerable<Quartier> quartiers, string template)
    {
        if (quartiers is null) throw new ArgumentNullException(nameof(quartiers));
    
        return quartiers.Select(q => FormatQuartierOption(q, template)).ToList();
    }



    public string FormatPhoneNumber(string phone)
    {
        if (string.IsNullOrWhiteSpace(phone))
            return string.Empty;

        var clean = new string(phone.Where(char.IsDigit).ToArray());
        
        if (clean.Length == 9)
        {
            return $"{_appSettings.IndicatifPays} {clean.Substring(0, 2)} {clean.Substring(2, 3)} {clean.Substring(5, 2)} {clean.Substring(7, 2)}";
        }

        // Retourner tel quel si format non reconnu
        return phone;
    }

    public string GetAppName() => _appSettings.Name;

    public string GetCurrency() => _appSettings.Currency;
}
