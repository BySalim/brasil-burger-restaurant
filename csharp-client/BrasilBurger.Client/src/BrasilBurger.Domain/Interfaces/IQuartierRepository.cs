using BrasilBurger.Domain.Entities;

namespace BrasilBurger.Domain.Interfaces;

public interface IQuartierRepository : IRepository<Quartier>
{
    Task<Quartier?> GetByNomAsync(string nom);
    Task<IEnumerable<Quartier>> GetByZoneIdAsync(int zoneId);
}