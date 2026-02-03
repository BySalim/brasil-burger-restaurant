using BrasilBurger.Client.Domain.Entities;
using BrasilBurger.Client.Domain.Enums;
using BrasilBurger.Client.Web.EnumsUi.Extensions;
using BrasilBurger.Client.Web.EnumsUi.Options;
using BrasilBurger.Client.Web.ViewModels.Orders;
using BrasilBurger.Client.Web.ViewModels.Shared;

namespace BrasilBurger.Client.Web.Mappers;

public sealed class OrderMapper
{
    public OrderItemVm MapToOrderItemVm(Commande commande)
    {
        return new OrderItemVm
        {
            Id = commande.Id,
            Code = commande.NumCmd,
            CreatedAt = commande.DateDebut,
            TotalAmount = commande.Montant,
            Type = MapTypeBadge(commande.Panier?.CategoriePanier ?? CategoriePanier.MENU),
            Status = MapEtatCommandeBadge(commande.Etat),
            PickupMode = MapModeRecuperationBadge(commande.TypeRecuperation),
            DeliveryStatus = commande.TypeRecuperation == ModeRecuperation.LIVRER && commande.Livraison != null
                ? MapStatutLivraisonBadge(commande.Livraison.Statut)
                : null
        };
    }

    public OrderDetailVm MapToOrderDetailVm(Commande commande)
    {
        var statutLivraison = commande.Livraison?.Statut;

        return new OrderDetailVm
        {
            Id = commande.Id,
            Code = commande.NumCmd,
            CreatedAt = commande.DateDebut,
            Montant = commande.Montant,
            Type = MapTypeBadge(commande.Panier?.CategoriePanier ?? CategoriePanier.BURGER),
            EtatBadge = MapEtatCommandeBadge(commande.Etat),
            ModeRecuperationBadge = MapModeRecuperationBadge(commande.TypeRecuperation),
            Tracking = OrderTrackingVm.Create(commande.Etat, commande.TypeRecuperation, statutLivraison),
            Articles = MapArticles(commande.Panier),
            Paiement = MapPaiement(commande.Paiement),
            Livraison = MapLivraison(commande)
        };
    }

    public BadgeVm MapTypeBadge(string type)
    {
        return new BadgeVm
        {
            Label = type,
            Variant = BadgeVariant.Soft,
            Color = UiColor.Bleu,
            Size = "sm"
        };
    }

    public BadgeVm MapTypeBadge(CategoriePanier type)
    {
        var meta = type.Ui();
        return new BadgeVm
        {
            Label = meta.Label,
            IconName = meta.Visual is UiIcon icon ? icon.IconName : null,
            Variant = BadgeVariant.Soft,
            Color = meta.Color,
            Size = "sm"
        };
    }

    private BadgeVm MapEtatCommandeBadge(EtatCommande etat)
    {
        var meta = etat.Ui();
        return new BadgeVm
        {
            Label = meta.Label,
            IconName = meta.Visual is UiIcon icon ? icon.IconName : null,
            Variant = BadgeVariant.PillSoft,
            Color = meta.Color,
            Size = "xs"
        };
    }

    private BadgeVm MapModeRecuperationBadge(ModeRecuperation mode)
    {
        var meta = mode.Ui();
        return new BadgeVm
        {
            Label = meta.Label,
            IconName = meta.Visual is UiIcon icon ? icon.IconName : null,
            Variant = BadgeVariant.PillSoft,
            Color = meta.Color,
            Size = "sm"
        };
    }

    private BadgeVm MapStatutLivraisonBadge(StatutLivraison statut)
    {
        var meta = statut.Ui();
        return new BadgeVm
        {
            Label = meta.Label,
            IconName = meta.Visual is UiIcon icon ? icon.IconName : null,
            Variant = BadgeVariant.Compact,
            Color = meta.Color,
            Size = "xs"
        };
    }

    private IReadOnlyList<OrderArticleVm> MapArticles(Panier? panier)
    {
        if (panier?.Lignes == null || !panier.Lignes.Any())
            return Array.Empty<OrderArticleVm>();

        return panier.Lignes.Select(ligne => new OrderArticleVm
        {
            Id = ligne.Article?.Id ?? 0,
            Nom = ligne.Article?.Libelle ?? "Article inconnu",
            ImagePublicId = ligne.Article?.ImagePublicId,
            Categorie = MapCategorieBadge(ligne.Article?.Categorie ?? CategorieArticle.BURGER),
            PrixUnitaire = ligne.Article?.GetPrix() ?? 0,
            Quantite = ligne.Quantite
        }).ToList();
    }

    private BadgeVm MapCategorieBadge(CategorieArticle categorie)
    {
        var meta = categorie.Ui();
        return new BadgeVm
        {
            Label = meta.Label ?? "Autre",
            IconName = meta.Visual is UiIcon icon ? icon.IconName : null,
            Variant = BadgeVariant.Soft,
            Color = meta.Color,
            Size = "xs"
        };
    }

    private BadgeVm MapModePaieBadge(ModePaiement mode)
    {
        var meta = mode.Ui();
        return new BadgeVm
        {
            Label = meta.Label ?? "Autre",
            IconName = meta.Visual is UiIcon icon ? icon.IconName : null,
            IconUrl = meta.Visual is UiImage image ? image.Path : null,
            Variant = BadgeVariant.Compact,
            Color = meta.Color,
            Size = "md"
        };
    }

    private OrderPaiementVm? MapPaiement(Paiement? paiement)
    {
        if (paiement == null)
            return null;

        return new OrderPaiementVm
        {
            Montant = paiement.MontantPaie,
            MethodeBadge = MapModePaieBadge(paiement.ModePaie),
            DatePaiement = paiement.DatePaie,
            Reference = paiement.ReferencePaiementExterne
        };
    }

    private OrderLivraisonVm? MapLivraison(Commande commande)
    {
        if (commande.TypeRecuperation != ModeRecuperation.LIVRER)
            return null;

        if (commande.InfoLivraison == null)
            return null;

        return new OrderLivraisonVm
        {
            PrixLivraison = commande.InfoLivraison.PrixLivraison,
            Quartier = commande.InfoLivraison.Quartier?.Nom ?? "Non spécifié",
            Zone = commande.InfoLivraison.Zone?.Nom ?? "Non spécifiée",
            NoteLivraison = commande.InfoLivraison.NoteLivraison,
            StatutBadge = commande.Livraison != null
                ? MapStatutLivraisonBadge(commande.Livraison.Statut)
                : null,
            Livreur = MapLivreur(commande.Livraison?.GroupeLivraison.Livreur)
        };
    }

    private OrderLivreurVm? MapLivreur(Livreur? livreur)
    {
        return new OrderLivreurVm
        {
            Nom = livreur?.Nom ?? "",
            Prenom = livreur?.Prenom ?? "",
            Telephone = livreur?.Telephone ?? ""
        };
    }
}