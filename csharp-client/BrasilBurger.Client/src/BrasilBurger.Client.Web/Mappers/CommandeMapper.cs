using BrasilBurger.Client.Application.Dtos;
using BrasilBurger.Client.Web.ViewModels.Pages;

namespace BrasilBurger.Client.web.Mappers;

public static class CommandeMapper
{
    public static CommandeDto ToDto(CatalogVmFormVm vm, int idClient)
    {
        if (vm is null) throw new ArgumentNullException(nameof(vm));

        return new CommandeDto
        {
            IdClient = idClient,
            ModeRecuperation = vm.RetrievalMethod,
            ModePaiement = vm.PaymentMethod,
            IdQuartier = vm.DeliveryQuartierId,
            PrixLivraison = vm.DeliveryPrice,
            NoteLivraison = vm.NoteLivraison,
            ArticleQuantifiers = vm.ArticleQuantifiers
                .Select(a => new CommandeArticleQuantifierDto
                {
                    IdArticle = a.ArticleId,
                    Quantite = a.Quantite
                })
                .ToList()
        };
    }
}
