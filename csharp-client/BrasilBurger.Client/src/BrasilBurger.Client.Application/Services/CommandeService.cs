using System.Security.Cryptography;
using BrasilBurger.Client.Application.Abstractions.Persistence;
using BrasilBurger.Client.Application.Abstractions.Services;
using BrasilBurger.Client.Application.Common;
using BrasilBurger.Client.Domain.Enums;
using BrasilBurger.Client.Domain.Entities;
using BrasilBurger.Client.Domain.Common;

namespace BrasilBurger.Client.Application.Services;

public sealed class CommandeService : ICommandeService
{
    private const string CmdPrefix = "CMD-";
    private readonly IUnitOfWork _db;

    public CommandeService(IUnitOfWork unitOfWork)
    {
        _db = unitOfWork ?? throw new ArgumentNullException(nameof(unitOfWork));
    }

    public async Task<Result<Commande>> NewCommande(
        Dtos.CommandeDto commandeDto, CancellationToken ct = default)
    {
        var modeRecuperation = ConvertEnum.FromString<ModeRecuperation>(commandeDto.ModeRecuperation!);

        if (modeRecuperation is null)
        {
            return Result<Commande>.Failure("Mode de récupération invalide. " + commandeDto.ModeRecuperation);
        }

        var client = await _db.Utilisateurs.GetByIdAsync(commandeDto.IdClient!.Value, ct);
        if (client is null || client is not Domain.Entities.Client)
        {
            return Result<Commande>.Failure("Utilisateur invalide ou non trouvé.");
        }

        InfoLivraison? infoLivraison = null;
        if (modeRecuperation == ModeRecuperation.LIVRER)
        {
            if (!commandeDto.IdQuartier.HasValue)
            {
                return Result<Commande>.Failure("Le quartier de livraison doit être spécifié pour une livraison.");
            }

            var infoLivraisonResult = NewLivraisonByIdQuartier(commandeDto.IdQuartier!.Value, commandeDto.NoteLivraison).GetAwaiter().GetResult();
            if (infoLivraisonResult.IsSuccess)
            {
                infoLivraison = infoLivraisonResult.Value;
            }
            else
            {
                return Result<Commande>.Failure(infoLivraisonResult.ErrorMessage ?? "Les informations de livraison sont invalides. Veuillez réessayer.");
            }
        }

        var panierResult = NewPanier(commandeDto.ArticleQuantifiers, ct).GetAwaiter().GetResult();
        if (!panierResult.IsSuccess)
        {
            return Result<Commande>.Failure(panierResult.ErrorMessage ?? "Erreur lors de la création du panier.");
        }

        var panier = panierResult.Value;

        var numeroCommande = await GenerateNumeroCommandeAsync(ct);

        var commande = new Commande(
            numCmd: numeroCommande,
            clientId: client.Id,
            typeRecuperation: modeRecuperation.Value,
            panier: panier,
            infoLivraison: infoLivraison
        );

        return Result<Commande>.Success(commande);
    }

    private async Task<Result<InfoLivraison>> NewLivraisonByIdQuartier(
                        int quartierId, string? noteLivraison, CancellationToken ct = default)
    {
        var quartier = await _db.Quartiers.GetByIdAsync(quartierId, ct);
        if (quartier is null)
        {
            return Result<InfoLivraison>.Failure("Le quartier sélectionné n'est pas valide.");
        }

        var zone = quartier.Zone;
        if (zone is null)
        {
            return Result<InfoLivraison>.Failure("La zone associée à ce quartier est introuvable.");
        }

        var prixLivraison = zone.PrixLivraison;

        var info = new InfoLivraison(
            idZone: zone.Id,
            idQuartier: quartier.Id,
            prixLivraison: prixLivraison,
            note: noteLivraison
        );

        return Result<InfoLivraison>.Success(info);
    }


    private async Task<string> GenerateNumeroCommandeAsync(CancellationToken ct)
    {
        for (var attempt = 0; attempt < 10; attempt++)
        {
            var suffix =
                $"{DateTime.UtcNow:yyyyMMddHHmmssfff}-" +
                $"{RandomNumberGenerator.GetInt32(0, 10000):D4}";

            var numero = CmdPrefix + suffix;
            var exists = await _db.Commandes.GetByNumeroAsync(numero, ct) is not null;
            if (!exists)
                return numero;
        }
        return CmdPrefix + Guid.NewGuid().ToString("N")[..16].ToUpperInvariant();
    }

    private async Task<Result<Panier>> NewPanier(
    List<Dtos.CommandeArticleQuantifierDto> articleQuantifiers,
    CancellationToken ct = default)
    {
        var quantitesByArticleId = new Dictionary<int, int>();
        foreach (var dto in articleQuantifiers)
        {
            if (dto.IdArticle <= 0)
                continue;

            if (dto.Quantite < 1)
                return Result<Panier>.Failure($"Quantité invalide pour l'article {dto.IdArticle}. La quantité doit être >= 1.");

            if (quantitesByArticleId.TryGetValue(dto.IdArticle, out var q))
                quantitesByArticleId[dto.IdArticle] = q + dto.Quantite;
            else
                quantitesByArticleId[dto.IdArticle] = dto.Quantite;
        }

        if (quantitesByArticleId.Count == 0)
            return Result<Panier>.Failure("Aucun article valide n'a été fourni.");

        CategoriePanier? categoriePanier = null;
        var panier = null as Panier;
        var montantTotal = 0;
        foreach (var (articleId, quantite) in quantitesByArticleId)
        {
            // Charger l'article AVEC ses dépendances (menu -> composition -> articles)
            // use GetWithDependenciesAsync which performs AsNoTracking on the query
            Article? article = await _db.Articles.GetWithDependenciesAsync(articleId, ct);

            if (article is null)
                continue;

            if (article.EstArchiver)
                continue;

            if (categoriePanier is null)
            {
                categoriePanier = article.Categorie switch
                {
                    CategorieArticle.BURGER => CategoriePanier.BURGER,
                    CategorieArticle.MENU => CategoriePanier.MENU,
                    _ => null
                };

                if (categoriePanier is null)
                    return Result<Panier>.Failure("Le premier article du panier doit être un BURGER ou un MENU.");

                panier = new Panier(categoriePanier.Value);
            }

            try
            {
                // Build quantifier using a snapshot (id, prix, catégorie) to avoid attaching tracked Article instances
                var ligne = new ArticleQuantifier(article.Id, quantite, article.GetPrix(), article.Categorie);
                panier!.AjouterLigne(ligne);
                montantTotal += ligne.Montant;
            }
            catch (DomainException ex)
            {
                return Result<Panier>.Failure(ex.Message);
            }
        }

        if (panier is null || panier.Lignes.Count == 0)
            return Result<Panier>.Failure($"Aucun article ne correspond. Panier impossible à créer.");
        panier.MontantTotal = montantTotal;
        return Result<Panier>.Success(panier);
    }

}