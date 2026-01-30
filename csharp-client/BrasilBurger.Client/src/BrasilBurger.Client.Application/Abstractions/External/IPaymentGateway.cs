using BrasilBurger.Client.Domain.Enums;

namespace BrasilBurger.Client.Application.Abstractions.External;

public interface IPaymentGateway
{
    Task<PaymentInitResult> InitializeAsync(
        int montant,
        ModePaiement modePaiement,
        CancellationToken ct = default);

    Task<PaymentGatewayStatus> GetStatusAsync(
        ModePaiement modePaiement,
        string providerReference,
        CancellationToken ct = default);
}

public sealed record PaymentInitResult(string ProviderReference, string? PaymentUrl, bool IsFake);

public enum PaymentGatewayStatus
{
    Unknown = 0,
    Pending = 1,
    Succeeded = 2,
    Failed = 3
}
