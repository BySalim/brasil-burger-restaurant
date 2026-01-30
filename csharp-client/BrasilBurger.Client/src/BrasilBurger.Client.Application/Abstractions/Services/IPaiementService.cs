using BrasilBurger.Client.Domain.Entities;
using BrasilBurger.Client.Application.Common;

namespace BrasilBurger.Client.Application.Abstractions.Services;

public interface IPaiementService
{
    Task<Result<Paiement>> NewPaiement(Commande commande, string modePaiement, CancellationToken ct = default);
    Task<Result<Paiement>> SavePaiement(Paiement paiement, CancellationToken ct = default);
}
