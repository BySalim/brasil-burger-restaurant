using BrasilBurger.Client.Domain.Common;
using BrasilBurger.Client.Domain.Enums;

namespace BrasilBurger.Client.Domain.Entities;

public class Complement : Article
{
    private Complement() { } // EF

    public Complement(string code, string libelle, string imagePublicId, TypeComplement typeComplement, int? prix)
        : base(code, libelle, imagePublicId)
    {
        TypeComplement = typeComplement;
        Prix = prix;
    }

    public override CategorieArticle Categorie => CategorieArticle.COMPLEMENT;

    public override int? GetPrix() => Prix;

    public void ChangerType(TypeComplement typeComplement) => TypeComplement = typeComplement;

    public void FixerPrix(int? prix)
    {
        if (prix is not null) Guard.Positive(prix.Value, nameof(prix));
        Prix = prix;
    }
}
