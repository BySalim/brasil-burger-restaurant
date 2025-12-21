using BrasilBurger.Domain.Entities;
using BrasilBurger.Domain.Interfaces;
using BrasilBurger.Domain.ValueObjects;

namespace BrasilBurger.Infrastructure.Services;

public class CalculateurPrixService : ICalculateurPrixService
{
    public Montant CalculerPrixMenu(IEnumerable<Article> articles)
    {
        int total = articles.Sum(a => a.Prix ?? 0);
        return Montant.From(total);
    }

    public Montant CalculerPrixPanier(Panier panier)
    {
        // Logique simplifiée
        return Montant.From(panier.MontantTotal);
    }
}