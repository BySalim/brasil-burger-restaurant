using BrasilBurger.Domain.Entities;
using BrasilBurger.Domain.ValueObjects;

namespace BrasilBurger.Domain.Interfaces;

public interface ICommandeRepository : IRepository<Commande>
{
    Task<Commande?> GetByNumeroAsync(NumeroCommande numero);
    Task<IEnumerable<Commande>> GetByClientTelephoneAsync(Telephone telephone);
    Task<IEnumerable<Commande>> GetByEtatAsync(EtatCommande etat);
    Task<IEnumerable<Commande>> GetCommandesDuJourAsync();
}