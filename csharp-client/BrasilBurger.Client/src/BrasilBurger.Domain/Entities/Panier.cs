using BrasilBurger.Domain.Entities;
using BrasilBurger.Domain.ValueObjects;

namespace BrasilBurger.Domain.Entities;

public class Panier : Entity
{
    public Montant MontantTotal { get; private set; }
    public CategoriePanier CategoriePanier { get; private set; }
    public int? IdCommande { get; private set; }

    protected Panier() { }

    public Panier(CategoriePanier categorie)
    {
        CategoriePanier = categorie;
        MontantTotal = Montant.From(0);
    }

    public void AjouterArticle(Article article, int quantite)
    {
        // Logique pour ajouter
    }

    public void CalculerTotal()
    {
        // Calcul
    }
}