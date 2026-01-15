using Microsoft.EntityFrameworkCore;
using BrasilBurger.Client.Data;
using BrasilBurger.Client.Models.Entities;
using BrasilBurger.Client.Repositories.Interfaces;

namespace BrasilBurger.Client.Repositories.Implementations;

public class OrderRepository : IOrderRepository
{
    private readonly AppDbContext _context;

    public OrderRepository(AppDbContext context)
    {
        _context = context;
    }

    public async Task<Order> CreateOrderAsync(Order order)
    {
        await _context.Orders.AddAsync(order);
        await _context.SaveChangesAsync();
        return order;
    }

    public async Task<Order> GetOrderByIdAsync(int id)
    {
        return await _context.Orders
            .Include(o => o.User)
            .Include(o => o.PaymentMethod)
            .Include(o => o.OrderItems)
                .ThenInclude(oi => oi.OrderComplements)
            .FirstOrDefaultAsync(o => o.Id == id);
    }

    public async Task<Order> GetOrderByReferenceAsync(string reference)
    {
        return await _context.Orders
            .Include(o => o.User)
            .Include(o => o.PaymentMethod)
            .Include(o => o.OrderItems)
                .ThenInclude(oi => oi.OrderComplements)
            .FirstOrDefaultAsync(o => o.Reference == reference);
    }

    public async Task<List<Order>> GetUserOrdersAsync(int userId)
    {
        return await _context.Orders
            .Where(o => o.UserId == userId)
            .Include(o => o.PaymentMethod)
            .OrderByDescending(o => o.CreatedAt)
            .ToListAsync();
    }

    public async Task SaveChangesAsync()
    {
        await _context.SaveChangesAsync();
    }
}
