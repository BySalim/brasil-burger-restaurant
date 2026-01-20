using BrasilBurger.Client.Application.Abstractions.Persistence;

namespace BrasilBurger.Client.Infrastructure.Persistence;

public sealed class UnitOfWork : IUnitOfWork
{
    private readonly BrasilBurgerDbContext _db;

    public UnitOfWork(
        BrasilBurgerDbContext db,
        IArticleRepository articles,
        IZoneRepository zones,
        IQuartierRepository quartiers,
        IUtilisateurRepository utilisateurs,
        IArticleQuantifierRepository articleQuantifiers,
        IPanierRepository paniers,
        IInfoLivraisonRepository infoLivraisons,
        ICommandeRepository commandes,
        IPaiementRepository paiements)
    {
        _db = db;

        Articles = articles;
        Zones = zones;
        Quartiers = quartiers;
        Utilisateurs = utilisateurs;

        ArticleQuantifiers = articleQuantifiers;
        Paniers = paniers;
        InfoLivraisons = infoLivraisons;
        Commandes = commandes;
        Paiements = paiements;
    }

    public IArticleRepository Articles { get; }
    public IZoneRepository Zones { get; }
    public IQuartierRepository Quartiers { get; }
    public IUtilisateurRepository Utilisateurs { get; }

    public IArticleQuantifierRepository ArticleQuantifiers { get; }
    public IPanierRepository Paniers { get; }
    public IInfoLivraisonRepository InfoLivraisons { get; }
    public ICommandeRepository Commandes { get; }
    public IPaiementRepository Paiements { get; }

    public Task<int> SaveChangesAsync(CancellationToken ct = default)
        => _db.SaveChangesAsync(ct);
}
