using BrasilBurger.Client.Application.Abstractions.External;
using BrasilBurger.Client.Domain.Enums;
using Microsoft.Extensions.Options;

namespace BrasilBurger.Client.Infrastructure.External.Payments;

internal sealed class OrangeMoneyRealProvider : IPaymentProvider
{
    public ModePaiement Mode => ModePaiement.OM;
    public bool IsFake => false;

    private readonly HttpClient _http;
    private readonly OrangeMoneyOptions _opt;

    public OrangeMoneyRealProvider(HttpClient http, IOptions<PaymentOptions> opt)
    {
        _http = http;
        _opt = opt.Value.OrangeMoney;
    }

    public Task<PaymentInitResult> InitializeAsync(int montant, CancellationToken ct)
        => throw new NotImplementedException("Brancher l'appel API OM réel (retourner ProviderReference + PaymentUrl).");

    public Task<PaymentGatewayStatus> GetStatusAsync(string providerReference, CancellationToken ct)
        => Task.FromResult(PaymentGatewayStatus.Unknown);
}
