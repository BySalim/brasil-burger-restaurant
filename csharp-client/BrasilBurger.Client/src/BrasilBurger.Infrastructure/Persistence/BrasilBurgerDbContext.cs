using Microsoft.EntityFrameworkCore;
using BrasilBurger.Domain.Entities;

namespace BrasilBurger.Infrastructure.Persistence;

public class BrasilBurgerDbContext : DbContext
{
    public BrasilBurgerDbContext(DbContextOptions<BrasilBurgerDbContext> options) : base(options) { }

    public DbSet<User> Users { get; set; }
    public DbSet<Article> Articles { get; set; }
    public DbSet<Panier> Paniers { get; set; }
    public DbSet<Commande> Commandes { get; set; }
    public DbSet<Paiement> Paiements { get; set; }
    public DbSet<Zone> Zones { get; set; }
    public DbSet<Quartier> Quartiers { get; set; }
    public DbSet<Livraison> Livraisons { get; set; }
    public DbSet<Livreur> Livreurs { get; set; }

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        modelBuilder.ApplyConfigurationsFromAssembly(typeof(BrasilBurgerDbContext).Assembly);
    }
}