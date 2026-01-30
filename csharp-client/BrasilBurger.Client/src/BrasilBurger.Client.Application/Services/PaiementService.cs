using BrasilBurger.Client.Application.Abstractions.Services;
using BrasilBurger.Client.Application.Abstractions.External;
using BrasilBurger.Client.Domain.Common;
using BrasilBurger.Client.Domain.Entities;
using BrasilBurger.Client.Domain.Enums;
using BrasilBurger.Client.Application.Common;

namespace BrasilBurger.Client.Application.Services;

public sealed class PaiementService : IPaiementService
{
    private readonly IPaymentGateway _gateway;

    public PaiementService(IPaymentGateway gateway)
    {
        _gateway = gateway;
    }

    public async Task<Result<Paiement>> NewPaiement(Commande commande, string modePaiement, CancellationToken ct = default)
    {
        if (commande is null || commande.Panier is null || commande.Client is null)
        throw new DomainException("La commande, le panier et le client ne peuvent pas être nuls pour initier un paiement.");

        var modePaie = ConvertEnum.FromString<ModePaiement>(modePaiement!);
        if (modePaie is null)
        {
            return Result<Paiement>.Failure("Mode de paiement invalide.");
        }

        var montantPanier = commande.Panier?.MontantTotal ?? 0;
        if (montantPanier <= 0)
            throw new DomainException("Le montant du panier est invalide (<= 0).");

        var paieInitResult = await _gateway.InitializeAsync(montantPanier, modePaie.Value, ct);
        var reference = paieInitResult.ProviderReference;
        var status = _gateway.GetStatusAsync(modePaie.Value, reference, ct).GetAwaiter().GetResult();
        if(status != PaymentGatewayStatus.Succeeded)
        {
            return Result<Paiement>.Failure("Le paiement n'a pas abouti.");
        }

        bool test = paieInitResult.IsFake;

        var paiement = new Paiement(
            commande: commande,
            montant: montantPanier,
            mode: modePaie.Value,
            referenceExterne: reference,
            test: test,
            client: commande.Client
        );
        commande.AssocierPaiement(paiement);
        return Result<Paiement>.Success(paiement);
    }

    public async Task<Result<Paiement>> SavePaiement(Paiement paiement, CancellationToken ct = default)
    {
        if (paiement is null)
            throw new DomainException("Le paiement ne peut pas être nul pour être sauvegardé.");

        
        return Result<Paiement>.Success(paiement);
    }
}
