using BrasilBurger.Domain.Entities;

namespace BrasilBurger.Domain.Interfaces;

public interface IPanierRepository : IRepository<Panier>
{
    Task<Panier?> GetByCommandeIdAsync(int commandeId);
}