using BrasilBurger.Domain.ValueObjects;
using BrasilBurger.Domain.Exceptions;

namespace BrasilBurger.Infrastructure.Payment;

public class FakePaymentProvider : IPaymentProvider
{
    public async Task<ReferencePaiement> ProcessPaymentAsync(Montant montant, ModePaiement mode)
    {
        // Simulation de paiement réussi en mode test
        await Task.Delay(100); // Simule un appel API
        return ReferencePaiement.From($"FAKE-{Guid.NewGuid()}");
    }
}