using BrasilBurger.Client.Models.Entities;

namespace BrasilBurger.Client.Repositories.Interfaces;

public interface IPaymentMethodRepository
{
    Task<List<PaymentMethod>> GetPaymentMethodsAsync();
    Task<PaymentMethod> GetPaymentMethodByIdAsync(int id);
    Task<bool> IsPaymentMethodEnabledAsync(int id);
}
