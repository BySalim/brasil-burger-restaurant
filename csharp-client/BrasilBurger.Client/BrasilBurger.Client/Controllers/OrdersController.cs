using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using BrasilBurger.Client.Models.DTOs.Orders;
using BrasilBurger.Client.Services.Interfaces;

namespace BrasilBurger.Client.Controllers;

[ApiController]
[Route("api/[controller]")]
[Authorize]
public class OrdersController : ControllerBase
{
    private readonly IOrderService _orderService;

    public OrdersController(IOrderService orderService)
    {
        _orderService = orderService;
    }

    private int GetUserId()
    {
        var userIdClaim = User.FindFirst("http://schemas.xmlsoap.org/ws/2005/05/identity/claims/nameidentifier");
        return int.Parse(userIdClaim?.Value ?? "0");
    }

    [HttpPost]
    public async Task<IActionResult> CreateOrder([FromBody] CreateOrderDto request)
    {
        if (!ModelState.IsValid)
            return BadRequest(ModelState);

        try
        {
            var userId = GetUserId();
            if (userId == 0)
                return Unauthorized();

            var order = await _orderService.CreateOrderAsync(request, userId);
            return CreatedAtAction(nameof(GetOrder), new { id = order.Id }, order);
        }
        catch (KeyNotFoundException ex)
        {
            return BadRequest(new { message = ex.Message });
        }
        catch (InvalidOperationException ex)
        {
            return BadRequest(new { message = ex.Message });
        }
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetOrder(int id)
    {
        try
        {
            var userId = GetUserId();
            if (userId == 0)
                return Unauthorized();

            var order = await _orderService.GetOrderAsync(id, userId);
            return Ok(order);
        }
        catch (UnauthorizedAccessException)
        {
            return Forbid();
        }
    }

    [HttpGet]
    public async Task<IActionResult> GetMyOrders()
    {
        var userId = GetUserId();
        if (userId == 0)
            return Unauthorized();

        var orders = await _orderService.GetUserOrdersAsync(userId);
        return Ok(orders);
    }

    [HttpGet("{id}/details")]
    public async Task<IActionResult> GetOrderDetails(int id)
    {
        try
        {
            var userId = GetUserId();
            if (userId == 0)
                return Unauthorized();

            var orderDetails = await _orderService.GetOrderDetailsAsync(id, userId);
            return Ok(orderDetails);
        }
        catch (UnauthorizedAccessException)
        {
            return Forbid();
        }
    }
}
