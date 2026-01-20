using BrasilBurger.Client.Domain.Common;
using BrasilBurger.Client.Domain.Enums;

namespace BrasilBurger.Client.Domain.Entities;

public class Client : Utilisateur
{
    private Client() { } // EF

    public Client(string nom, string prenom, string login, string motDePasse, string telephone)
        : base(nom, prenom, login, motDePasse)
    {
        Telephone = Guard.NotNullOrWhiteSpace(telephone, nameof(telephone));
    }

    public override Role Role => Role.CLIENT;

    public void ChangerTelephone(string telephone)
        => Telephone = Guard.NotNullOrWhiteSpace(telephone, nameof(telephone));
}
