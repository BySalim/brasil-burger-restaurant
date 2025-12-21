using BrasilBurger.Domain.Entities;

namespace BrasilBurger.Domain.Entities;

public class Article : Entity
{
    public string Code { get; private set; }
    public string Libelle { get; private set; }
    public string ImagePublicId { get; private set; }
    public bool EstArchiver { get; private set; }
    public CategorieArticle Categorie { get; private set; }
    public string? Description { get; private set; }
    public int? Prix { get; private set; }
    public TypeComplement? TypeComplement { get; private set; }

    protected Article() { }

    public Article(string code, string libelle, string imagePublicId, CategorieArticle categorie, string? description, int? prix, TypeComplement? typeComplement)
    {
        Code = code ?? throw new ArgumentNullException(nameof(code));
        Libelle = libelle ?? throw new ArgumentNullException(nameof(libelle));
        ImagePublicId = imagePublicId ?? throw new ArgumentNullException(nameof(imagePublicId));
        Categorie = categorie;
        Description = description;
        Prix = prix;
        TypeComplement = typeComplement;
    }

    public void Archiver() => EstArchiver = true;
}