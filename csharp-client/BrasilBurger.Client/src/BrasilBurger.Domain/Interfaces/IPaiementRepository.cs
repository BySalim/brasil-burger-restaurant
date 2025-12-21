using BrasilBurger.Domain.Entities;

namespace BrasilBurger.Domain.Interfaces;

public interface IPaiementRepository : IRepository<Paiement>
{
    Task<Paiement?> GetByCommandeIdAsync(int commandeId);
    Task<IEnumerable<Paiement>> GetPaiementsDuJourAsync();
}