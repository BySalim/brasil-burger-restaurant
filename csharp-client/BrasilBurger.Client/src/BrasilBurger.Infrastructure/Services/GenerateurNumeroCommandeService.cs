using BrasilBurger.Domain.Interfaces;
using BrasilBurger.Domain.ValueObjects;

namespace BrasilBurger.Infrastructure.Services;

public class GenerateurNumeroCommandeService : IGenerateurNumeroCommandeService
{
    public NumeroCommande Generer()
    {
        string numero = $"CMD-{DateTime.Now:yyyyMMdd}-{Guid.NewGuid().ToString().Substring(0, 8).ToUpper()}";
        return NumeroCommande.From(numero);
    }
}