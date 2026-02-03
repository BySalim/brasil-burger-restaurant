using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Diagnostics;
using BrasilBurger.Client.Web.ViewModels;

namespace BrasilBurger.Client.Web.Controllers;

[Route("Error")]
public class ErrorController : Controller
{
    [HttpGet("")]
    [HttpGet("{statusCode?}")]
    public IActionResult Index(int? statusCode = null)
    {
        var statusCodeResult = HttpContext.Features.Get<IStatusCodeReExecuteFeature>();
        
        var code = statusCode 
            ?? statusCodeResult?.StatusCode 
            ?? HttpContext.Response.StatusCode;

        var vm = new ErrorViewModel
        {
            StatusCode = code,
            RequestId = Activity.Current?.Id ?? HttpContext.TraceIdentifier,
            Message = GetErrorMessage(code),
            Title = GetErrorTitle(code)
        };

        Response.StatusCode = code;
        return View("Error", vm);
    }

    [HttpGet("404")]
    public IActionResult NotFound()
    {
        var vm = new ErrorViewModel
        {
            StatusCode = 404,
            RequestId = Activity.Current?.Id ?? HttpContext.TraceIdentifier,
            Title = "Page non trouvée",
            Message = "Désolé, la page que vous recherchez n'existe pas ou a été déplacée."
        };

        Response.StatusCode = 404;
        return View("NotFound", vm);
    }

    [HttpGet("500")]
    public IActionResult ServerError()
    {
        var vm = new ErrorViewModel
        {
            StatusCode = 500,
            RequestId = Activity.Current?.Id ?? HttpContext.TraceIdentifier,
            Title = "Erreur serveur",
            Message = "Une erreur inattendue s'est produite. Nous travaillons à résoudre le problème."
        };

        Response.StatusCode = 500;
        return View("ServerError", vm);
    }

    [HttpGet("403")]
    public IActionResult Forbidden()
    {
        var vm = new ErrorViewModel
        {
            StatusCode = 403,
            RequestId = Activity.Current?.Id ?? HttpContext.TraceIdentifier,
            Title = "Accès refusé",
            Message = "Vous n'avez pas les permissions nécessaires pour accéder à cette ressource."
        };

        Response.StatusCode = 403;
        return View("Forbidden", vm);
    }

    private static string GetErrorTitle(int statusCode) => statusCode switch
    {
        400 => "Requête invalide",
        401 => "Non autorisé",
        403 => "Accès refusé",
        404 => "Page non trouvée",
        408 => "Délai d'attente dépassé",
        429 => "Trop de requêtes",
        500 => "Erreur serveur",
        502 => "Passerelle invalide",
        503 => "Service indisponible",
        _ => "Une erreur est survenue"
    };

    private static string GetErrorMessage(int statusCode) => statusCode switch
    {
        400 => "La requête envoyée n'est pas valide.",
        401 => "Vous devez vous connecter pour accéder à cette page.",
        403 => "Vous n'avez pas les permissions nécessaires pour accéder à cette ressource.",
        404 => "La page que vous recherchez n'existe pas ou a été déplacée.",
        408 => "Le serveur a mis trop de temps à répondre.",
        429 => "Vous avez fait trop de requêtes. Veuillez réessayer dans quelques instants.",
        500 => "Une erreur inattendue s'est produite sur le serveur.",
        502 => "Le serveur a reçu une réponse invalide.",
        503 => "Le service est temporairement indisponible. Veuillez réessayer plus tard.",
        _ => "Une erreur inattendue s'est produite."
    };
}