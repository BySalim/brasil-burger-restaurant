using BrasilBurger.Client.Application.Abstractions.External;
using BrasilBurger.Client.Domain.Enums;
using Microsoft.Extensions.Options;

namespace BrasilBurger.Client.Infrastructure.External.Payments;

public sealed class PaymentGateway : IPaymentGateway
{
    private readonly PaymentOptions _opt;
    private readonly IReadOnlyList<IPaymentProvider> _providers;

    public PaymentGateway(IOptions<PaymentOptions> opt, IEnumerable<IPaymentProvider> providers)
    {
        _opt = opt.Value;
        _providers = providers.ToList();
    }

    public Task<PaymentInitResult> InitializeAsync(int montant, ModePaiement modePaiement, CancellationToken ct = default)
        => Resolve(modePaiement).InitializeAsync(montant, ct);

    public Task<PaymentGatewayStatus> GetStatusAsync(ModePaiement modePaiement, string providerReference, CancellationToken ct = default)
        => Resolve(modePaiement).GetStatusAsync(providerReference, ct);

    private IPaymentProvider Resolve(ModePaiement mode)
    {
        var isFake = _opt.Fake;

        var provider = _providers.FirstOrDefault(p => p.Mode == mode && p.IsFake == isFake);
        if (provider is null)
            throw new InvalidOperationException($"Aucun provider pour {mode} (Fake={isFake}).");

        return provider;
    }
}
