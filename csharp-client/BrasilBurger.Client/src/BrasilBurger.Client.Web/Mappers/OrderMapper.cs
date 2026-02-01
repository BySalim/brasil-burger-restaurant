using System.Reflection.Metadata.Ecma335;
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
            Type = MapTypeBadge(commande.Panier?.CategoriePanier ?? CategoriePanier.BURGER),
            Status = MapEtatCommandeBadge(commande.Etat),
            PickupMode = MapModeRecuperationBadge(commande.TypeRecuperation),
            DeliveryStatus = commande.TypeRecuperation == ModeRecuperation.LIVRER && commande.Livraison != null
                ? MapStatutLivraisonBadge(commande.Livraison.Statut)
                : null
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

    private static BadgeVm MapEtatCommandeBadge(EtatCommande etat)
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

    private static BadgeVm MapModeRecuperationBadge(ModeRecuperation mode)
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

    private static BadgeVm MapStatutLivraisonBadge(StatutLivraison statut)
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
}