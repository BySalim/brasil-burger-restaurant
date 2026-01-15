using BrasilBurger.Client.Models.Entities;

namespace BrasilBurger.Client.Repositories.Interfaces;

public interface IOrderRepository
{
    Task<Order> CreateOrderAsync(Order order);
    Task<Order> GetOrderByIdAsync(int id);
    Task<Order> GetOrderByReferenceAsync(string reference);
    Task<List<Order>> GetUserOrdersAsync(int userId);
    Task SaveChangesAsync();
}
