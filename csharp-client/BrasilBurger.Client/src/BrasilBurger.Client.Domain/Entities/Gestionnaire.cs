using BrasilBurger.Client.Domain.Enums;

namespace BrasilBurger.Client.Domain.Entities;

public class Gestionnaire : Utilisateur
{
    private Gestionnaire() { } // EF

    public Gestionnaire(string nom, string prenom, string login, string motDePasse)
        : base(nom, prenom, login, motDePasse)
    {
    }

    public override Role Role => Role.GESTIONNAIRE;
}
