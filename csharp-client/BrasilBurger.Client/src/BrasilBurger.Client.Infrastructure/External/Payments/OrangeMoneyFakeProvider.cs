using BrasilBurger.Client.Application.Abstractions.External;
using BrasilBurger.Client.Domain.Enums;

namespace BrasilBurger.Client.Infrastructure.External.Payments;

internal sealed class OrangeMoneyFakeProvider : IPaymentProvider
{
    public ModePaiement Mode => ModePaiement.OM;
    public bool IsFake => true;

    public Task<PaymentInitResult> InitializeAsync(int montant, CancellationToken ct)
    {
        var providerRef = "OM_" + Guid.NewGuid().ToString("N");
        return Task.FromResult(new PaymentInitResult(providerRef, null));
    }

    public Task<PaymentGatewayStatus> GetStatusAsync(string providerReference, CancellationToken ct)
        => Task.FromResult(PaymentGatewayStatus.Succeeded);
}
