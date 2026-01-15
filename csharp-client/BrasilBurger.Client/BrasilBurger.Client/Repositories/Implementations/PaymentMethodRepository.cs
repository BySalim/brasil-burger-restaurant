using Microsoft.EntityFrameworkCore;
using BrasilBurger.Client.Data;
using BrasilBurger.Client.Models.Entities;
using BrasilBurger.Client.Repositories.Interfaces;

namespace BrasilBurger.Client.Repositories.Implementations;

public class PaymentMethodRepository : IPaymentMethodRepository
{
    private readonly AppDbContext _context;

    public PaymentMethodRepository(AppDbContext context)
    {
        _context = context;
    }

    public async Task<List<PaymentMethod>> GetPaymentMethodsAsync()
    {
        return await _context.PaymentMethods.Where(p => p.Enabled).ToListAsync();
    }

    public async Task<PaymentMethod> GetPaymentMethodByIdAsync(int id)
    {
        return await _context.PaymentMethods.FirstOrDefaultAsync(p => p.Id == id);
    }

    public async Task<bool> IsPaymentMethodEnabledAsync(int id)
    {
        return await _context.PaymentMethods.AnyAsync(p => p.Id == id && p.Enabled);
    }
}
