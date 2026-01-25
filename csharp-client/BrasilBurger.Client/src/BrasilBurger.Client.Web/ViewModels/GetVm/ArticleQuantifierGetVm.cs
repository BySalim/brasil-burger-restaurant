namespace BrasilBurger.Client.Web.ViewModels.GetVm;

public sealed record ArticleQuantifierGetVm(
    ArticleGetVm Article,
    int Quantite,
    int Montant
);
