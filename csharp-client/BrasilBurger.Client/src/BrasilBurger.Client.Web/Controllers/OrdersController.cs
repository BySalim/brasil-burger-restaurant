using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Authorization;

namespace  BrasilBurger.Client.Web.Controllers;

[Route("Orders")]
[Authorize]
public sealed class OrdersController : Controller
{
    [HttpGet("")]
    public IActionResult Index()
    {
        return View("~/Views/Orders/Index.cshtml");
    }
}
