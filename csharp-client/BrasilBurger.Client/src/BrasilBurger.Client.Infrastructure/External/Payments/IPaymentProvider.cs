using BrasilBurger.Client.Application.Abstractions.External;
using BrasilBurger.Client.Domain.Enums;

namespace BrasilBurger.Client.Infrastructure.External.Payments;

public interface IPaymentProvider
{
    ModePaiement Mode { get; }
    bool IsFake { get; }

    Task<PaymentInitResult> InitializeAsync(int montant, CancellationToken ct);
    Task<PaymentGatewayStatus> GetStatusAsync(string providerReference, CancellationToken ct);
}
