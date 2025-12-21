using BrasilBurger.Domain.Entities;

namespace BrasilBurger.Domain.Interfaces;

public interface IZoneRepository : IRepository<Zone>
{
    Task<Zone?> GetByNomAsync(string nom);
}