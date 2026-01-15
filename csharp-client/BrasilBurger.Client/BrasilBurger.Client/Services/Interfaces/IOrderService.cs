using BrasilBurger.Client.Models.DTOs.Orders;

namespace BrasilBurger.Client.Services.Interfaces;

public interface IOrderService
{
    Task<OrderDto> CreateOrderAsync(CreateOrderDto request, int userId);
    Task<OrderDto> GetOrderAsync(int id, int userId);
    Task<List<OrderDto>> GetUserOrdersAsync(int userId);
    Task<OrderDetailDto> GetOrderDetailsAsync(int id, int userId);
}
