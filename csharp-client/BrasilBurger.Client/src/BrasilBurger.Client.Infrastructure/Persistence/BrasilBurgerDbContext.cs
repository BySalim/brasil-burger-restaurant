using BrasilBurger.Client.Domain.Entities;
using Microsoft.EntityFrameworkCore;

namespace BrasilBurger.Client.Infrastructure.Persistence;

public class BrasilBurgerDbContext : DbContext
{
    public BrasilBurgerDbContext(DbContextOptions<BrasilBurgerDbContext> options) : base(options) { }

    public DbSet<Article> Articles => Set<Article>();
    public DbSet<Utilisateur> Utilisateurs => Set<Utilisateur>();

    public DbSet<ArticleQuantifier> ArticleQuantifiers => Set<ArticleQuantifier>();
    public DbSet<Panier> Paniers => Set<Panier>();
    public DbSet<Commande> Commandes => Set<Commande>();
    public DbSet<Paiement> Paiements => Set<Paiement>();

    public DbSet<Zone> Zones => Set<Zone>();
    public DbSet<Quartier> Quartiers => Set<Quartier>();
    public DbSet<InfoLivraison> InfoLivraisons => Set<InfoLivraison>();

    public DbSet<Livreur> Livreurs => Set<Livreur>();
    public DbSet<GroupeLivraison> GroupesLivraison => Set<GroupeLivraison>();
    public DbSet<Livraison> Livraisons => Set<Livraison>();

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        modelBuilder.ApplyConfigurationsFromAssembly(typeof(BrasilBurgerDbContext).Assembly);
        base.OnModelCreating(modelBuilder);
    }
}
