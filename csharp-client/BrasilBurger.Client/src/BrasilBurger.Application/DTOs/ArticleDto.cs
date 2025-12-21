namespace BrasilBurger.Application.DTOs;

public class ArticleDto
{
    public int Id { get; set; }
    public string Code { get; set; }
    public string Libelle { get; set; }
    public string ImageUrl { get; set; }
    public string Categorie { get; set; }
    public string Description { get; set; }
    public int? Prix { get; set; }
}