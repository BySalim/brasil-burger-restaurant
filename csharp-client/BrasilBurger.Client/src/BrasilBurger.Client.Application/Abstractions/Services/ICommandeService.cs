using BrasilBurger.Client.Application.Common;
using BrasilBurger.Client.Application.Dtos;

namespace BrasilBurger.Client.Application.Abstractions.Services;


public interface ICommandeService
{
    Task<Result<Domain.Entities.Commande>> NewCommande(CommandeDto commandeDto, CancellationToken ct = default);
}
