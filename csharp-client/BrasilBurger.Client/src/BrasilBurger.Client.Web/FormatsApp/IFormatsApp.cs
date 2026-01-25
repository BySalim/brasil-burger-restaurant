using BrasilBurger.Client.Domain.Entities;
using BrasilBurger.Client.Web.ViewModels.Shared;

namespace BrasilBurger.Client.Web.FormatsApp;

/// <summary>
/// Service de formatage pour l'affichage
/// </summary>
public interface IFormatsApp
{
    /// <summary>
    /// Formate un prix en devise locale
    /// Ex: 5000 → "5 000 F CFA"
    /// </summary>
    string FormatPrice(int priceInCents);


    /// <summary>
    /// Formate un quartier avec template modulaire
    /// </summary>
    public string FormatQuartier(Quartier quartier, string template);

    /// <summary>
    /// Formate un quartier avec un template et retourne une option
    /// </summary>
    public SelectOptionVm FormatQuartierOption(Quartier quartier, string template);
    
    /// <summary>
    /// Formate des quartiers et retourne les options
    /// </summary>
    public IReadOnlyList<SelectOptionVm> FormatQuartiers(IEnumerable<Quartier> quartiers, string template);

    /// <summary>
    /// Formate un numéro de téléphone sénégalais
    /// Ex: 771234567 → "+221 77 123 45 67"
    /// </summary>
    string FormatPhoneNumber(string phone);

    /// <summary>
    /// Obtient le nom de l'application
    /// </summary>
    string GetAppName();

    /// <summary>
    /// Obtient la devise
    /// </summary>
    string GetCurrency();
}
