using System.Security.Cryptography;
using System.Text;
using BrasilBurger.Client.Infrastructure.Diagnostics;
using BrasilBurger.Client.Web.ViewModels.Diagnostics;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;

namespace BrasilBurger.Client.Web.Controllers;

[Route("diagnostics")]
public sealed class DiagnosticsController : Controller
{
    private const string TokenQueryKey = "token";
    private const string TokenHeaderKey = "X-Diagnostics-Token";

    private readonly IDatabaseProbe _db;
    private readonly ICloudinaryProbe _cloudinary;
    private readonly IConfiguration _config;

    public DiagnosticsController(IDatabaseProbe db, ICloudinaryProbe cloudinary, IConfiguration config)
    {
        _db = db;
        _cloudinary = cloudinary;
        _config = config;
    }

    [HttpGet("")]
    public async Task<IActionResult> Index(CancellationToken ct)
    {
        // Si pas de token configuré => on désactive l'endpoint.
        // En prod, ça évite d'exposer /diagnostics par erreur.
        var expectedToken = _config["Diagnostics:Token"];
        if (!IsAuthorized(Request, expectedToken))
            return NotFound(); // on masque l’existence de l’endpoint

        var dbTask = _db.TestAsync(ct);
        var clTask = _cloudinary.TestAsync(ct);

        await Task.WhenAll(dbTask, clTask);

        var vm = new DiagnosticsVm
        {
            DbOk = dbTask.Result.Ok,
            DbMessage = dbTask.Result.Message,
            CloudinaryOk = clTask.Result.Ok,
            CloudinaryMessage = clTask.Result.Message
        };

        return View(vm);
    }

    private static bool IsAuthorized(HttpRequest request, string? expectedToken)
    {
        if (string.IsNullOrWhiteSpace(expectedToken))
            return false;

        var provided = request.Query[TokenQueryKey].ToString();
        if (string.IsNullOrWhiteSpace(provided))
            provided = request.Headers[TokenHeaderKey].ToString();

        if (string.IsNullOrWhiteSpace(provided))
            return false;

        return FixedTimeEquals(provided, expectedToken);
    }

    private static bool FixedTimeEquals(string a, string b)
    {
        var ba = Encoding.UTF8.GetBytes(a);
        var bb = Encoding.UTF8.GetBytes(b);

        return ba.Length == bb.Length && CryptographicOperations.FixedTimeEquals(ba, bb);
    }
}
