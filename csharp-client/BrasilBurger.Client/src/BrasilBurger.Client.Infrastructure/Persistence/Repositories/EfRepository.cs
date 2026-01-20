using BrasilBurger.Client.Application.Abstractions.Persistence;
using BrasilBurger.Client.Domain.Common;
using Microsoft.EntityFrameworkCore;

namespace BrasilBurger.Client.Infrastructure.Persistence.Repositories;

public abstract class EfRepository<TEntity> : IRepository<TEntity>
    where TEntity : Entity
{
    protected readonly BrasilBurgerDbContext Db;
    protected readonly DbSet<TEntity> Set;

    protected EfRepository(BrasilBurgerDbContext db)
    {
        Db = db;
        Set = db.Set<TEntity>();
    }

    public virtual Task<TEntity?> GetByIdAsync(int id, CancellationToken ct = default)
        => Set.FirstOrDefaultAsync(e => e.Id == id, ct);

    public virtual async Task<IReadOnlyList<TEntity>> ListAsync(CancellationToken ct = default)
        => await Set.AsNoTracking().ToListAsync(ct);

    public virtual Task AddAsync(TEntity entity, CancellationToken ct = default)
        => Set.AddAsync(entity, ct).AsTask();

    public virtual void Remove(TEntity entity) => Set.Remove(entity);
}
