using BrasilBurger.Client.Application.Abstractions.External;
using BrasilBurger.Client.Domain.Enums;
using Microsoft.Extensions.Options;

namespace BrasilBurger.Client.Infrastructure.External.Payments;

internal sealed class WaveRealProvider : IPaymentProvider
{
    public ModePaiement Mode => ModePaiement.WAVE;
    public bool IsFake => false;

    private readonly HttpClient _http;
    private readonly WaveOptions _opt;

    public WaveRealProvider(HttpClient http, IOptions<PaymentOptions> opt)
    {
        _http = http;
        _opt = opt.Value.Wave;
    }

    public Task<PaymentInitResult> InitializeAsync(int montant, CancellationToken ct)
        => throw new NotImplementedException("Brancher l'appel API Wave réel (retourner ProviderReference + PaymentUrl).");

    public Task<PaymentGatewayStatus> GetStatusAsync(string providerReference, CancellationToken ct)
        => Task.FromResult(PaymentGatewayStatus.Unknown);
}
