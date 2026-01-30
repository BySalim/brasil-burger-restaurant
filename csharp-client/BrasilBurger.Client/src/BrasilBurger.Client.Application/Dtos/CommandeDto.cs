namespace BrasilBurger.Client.Application.Dtos;

public sealed class CommandeDto
{
    public int? IdClient { get; set; }
    public string? ModeRecuperation { get; set; }
    public string? ModePaiement { get; set; }

    public int? IdQuartier { get; set; }
    public int? PrixLivraison { get; set; }
    public string? NoteLivraison { get; set; }

    public List<CommandeArticleQuantifierDto> ArticleQuantifiers { get; set; } = new();
}

public sealed class CommandeArticleQuantifierDto
{
    public int IdArticle { get; set; }
    public int Quantite { get; set; }
}
