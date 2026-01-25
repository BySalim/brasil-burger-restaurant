using Microsoft.AspNetCore.Mvc;

namespace  BrasilBurger.Client.Web.Controllers;

[Route("Orders")]
public sealed class OrdersController : Controller
{
    [HttpGet("Create")]
    public IActionResult Index()
    {
        return View("~/Views/Orders/Index.cshtml");
    }
}
