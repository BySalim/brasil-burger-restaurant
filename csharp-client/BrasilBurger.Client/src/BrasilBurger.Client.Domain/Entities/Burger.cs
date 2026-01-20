using BrasilBurger.Client.Domain.Common;
using BrasilBurger.Client.Domain.Enums;

namespace BrasilBurger.Client.Domain.Entities;

public class Burger : Article
{
    private Burger() { } // EF

    public Burger(string code, string libelle, string imagePublicId, int prix, string? description)
        : base(code, libelle, imagePublicId)
    {
        Prix = Guard.Positive(prix, nameof(prix));
        Description = description;
    }

    public override CategorieArticle Categorie => CategorieArticle.BURGER;

    public override int? GetPrix() => Prix;

    public void ChangerDescription(string? description) => Description = description;

    public void FixerPrix(int? prix)
    {
        if (prix is not null) Guard.Positive(prix.Value, nameof(prix));
        Prix = prix;
    }
}
