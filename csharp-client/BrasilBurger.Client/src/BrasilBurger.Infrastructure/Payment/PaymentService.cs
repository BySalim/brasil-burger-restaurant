using BrasilBurger.Domain.ValueObjects;
using BrasilBurger.Domain.Exceptions;

namespace BrasilBurger.Infrastructure.Payment;

public class PaymentService
{
    private readonly IPaymentProvider _provider;

    public PaymentService(IPaymentProvider provider)
    {
        _provider = provider;
    }

    public async Task<ReferencePaiement> PayerAsync(Montant montant, ModePaiement mode)
    {
        if (montant.Valeur <= 0)
            throw new PaiementException("Montant invalide.");

        return await _provider.ProcessPaymentAsync(montant, mode);
    }
}