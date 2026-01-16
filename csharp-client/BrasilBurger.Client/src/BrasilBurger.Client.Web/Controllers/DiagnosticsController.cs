using BrasilBurger.Client.Infrastructure.Diagnostics;
using BrasilBurger.Client.Web.ViewModels.Diagnostics;
using Microsoft.AspNetCore.Mvc;

namespace BrasilBurger.Client.Web.Controllers;

public sealed class DiagnosticsController : Controller
{
    private readonly IDatabaseProbe _db;
    private readonly ICloudinaryProbe _cloudinary;

    public DiagnosticsController(IDatabaseProbe db, ICloudinaryProbe cloudinary)
    {
        _db = db;
        _cloudinary = cloudinary;
    }

    [HttpGet("/diagnostics")]
    public async Task<IActionResult> Index(CancellationToken ct)
    {
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
}
