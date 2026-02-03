namespace BrasilBurger.Client.Web.ViewModels;

public class ErrorViewModel
{
    public string? RequestId { get; set; }
    public int StatusCode { get; set; }
    public string Title { get; set; } = "Une erreur est survenue";
    public string Message { get; set; } = "Une erreur inattendue s'est produite.";
    
    public bool ShowRequestId => !string.IsNullOrEmpty(RequestId);
}