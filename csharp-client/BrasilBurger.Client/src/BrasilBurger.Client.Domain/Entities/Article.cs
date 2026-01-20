using BrasilBurger.Client.Domain.Common;
using BrasilBurger.Client.Domain.Enums;

namespace BrasilBurger.Client.Domain.Entities;

public abstract class Article : Entity
{
    protected Article() { } // EF

    protected Article(string code, string libelle, string imagePublicId)
    {
        Code = Guard.NotNullOrWhiteSpace(code, nameof(code));
        Libelle = Guard.NotNullOrWhiteSpace(libelle, nameof(libelle));
        ImagePublicId = Guard.NotNullOrWhiteSpace(imagePublicId, nameof(imagePublicId));

        Description = null;
        Prix = null;
        TypeComplement = null;
    }

    public string Code { get; private set; } = default!;
    public string Libelle { get; private set; } = default!;
    public string ImagePublicId { get; private set; } = default!;
    public bool EstArchiver { get; private set; }

    public string? Description { get; protected set; }
    public int? Prix { get; protected set; }
    public TypeComplement? TypeComplement { get; protected set; }

    /// <summary>Discriminator logique (TPH), identique à Symfony.</summary>
    public abstract CategorieArticle Categorie { get; }

    public abstract int? GetPrix();

    public void Renommer(string libelle) => Libelle = Guard.NotNullOrWhiteSpace(libelle, nameof(libelle));
    public void ChangerImage(string imagePublicId) => ImagePublicId = Guard.NotNullOrWhiteSpace(imagePublicId, nameof(imagePublicId));

    public void Archiver() => EstArchiver = true;
    public void Desarchiver() => EstArchiver = false;
}
