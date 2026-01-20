namespace BrasilBurger.Client.Application.Abstractions.Persistence;

public interface IUnitOfWork
{
    IArticleRepository Articles { get; }
    IZoneRepository Zones { get; }
    IQuartierRepository Quartiers { get; }

    IUtilisateurRepository Utilisateurs { get; }

    IArticleQuantifierRepository ArticleQuantifiers { get; }
    IPanierRepository Paniers { get; }
    IInfoLivraisonRepository InfoLivraisons { get; }
    ICommandeRepository Commandes { get; }
    IPaiementRepository Paiements { get; }

    Task<int> SaveChangesAsync(CancellationToken ct = default);
}
