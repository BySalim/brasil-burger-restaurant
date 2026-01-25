using BrasilBurger.Client.Domain.Enums;
using BrasilBurger.Client.Web.EnumsUi.Abstractions;

namespace BrasilBurger.Client.Web.ViewModels.GetVm;

public sealed record ArticleGetVm(
    int Id,
    string Libelle,
    string ImageUrl,
    EnumUiItem<CategorieArticle> TypeCategorieUi,
    string? Description,
    EnumUiItem<TypeComplement>? TypeComplementUi,
    int Prix,
    IReadOnlyList<ArticleQuantifierGetVm>? ArticleQuantifiers = null
);
