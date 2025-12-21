using BrasilBurger.Domain.ValueObjects;

namespace BrasilBurger.Domain.Interfaces;

public interface ICalculateurPrixService
{
    Montant CalculerPrixMenu(IEnumerable<Article> articles);
    Montant CalculerPrixPanier(Panier panier);
}