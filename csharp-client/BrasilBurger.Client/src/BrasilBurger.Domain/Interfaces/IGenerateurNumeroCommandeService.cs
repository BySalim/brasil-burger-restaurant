using BrasilBurger.Domain.ValueObjects;

namespace BrasilBurger.Domain.Interfaces;

public interface IGenerateurNumeroCommandeService
{
    NumeroCommande Generer();
}