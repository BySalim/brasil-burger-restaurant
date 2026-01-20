using BrasilBurger.Client.Domain.Common;

namespace BrasilBurger.Client.Application.Abstractions.Persistence;

public interface IRepository<TEntity> where TEntity : Entity
{
    Task<TEntity?> GetByIdAsync(int id, CancellationToken ct = default);
    Task<IReadOnlyList<TEntity>> ListAsync(CancellationToken ct = default);

    Task AddAsync(TEntity entity, CancellationToken ct = default);
    void Remove(TEntity entity);
}
