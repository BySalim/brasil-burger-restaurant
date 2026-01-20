using BrasilBurger.Client.Domain.Common;
using BrasilBurger.Client.Domain.Enums;

namespace BrasilBurger.Client.Domain.Entities;

public abstract class Utilisateur : Entity
{
    protected Utilisateur() { } // EF

    protected Utilisateur(string nom, string prenom, string login, string motDePasse)
    {
        Nom = Guard.NotNullOrWhiteSpace(nom, nameof(nom));
        Prenom = Guard.NotNullOrWhiteSpace(prenom, nameof(prenom));
        Login = Guard.NotNullOrWhiteSpace(login, nameof(login));
        MotDePasse = Guard.NotNullOrWhiteSpace(motDePasse, nameof(motDePasse));

        Telephone = null;
    }

    public string Nom { get; private set; } = default!;
    public string Prenom { get; private set; } = default!;
    public string Login { get; private set; } = default!;

    public string MotDePasse { get; private set; } = default!;

    public int? QuartierLivraisonDefautId { get; private set; }
    public Quartier? QuartierLivraisonDefaut { get; private set; }

    public ModePaiement? ModePaiementDefaut { get; private set; }
    public ModeRecuperation? ModeRecuperationDefaut { get; private set; }

    public string? Telephone { get; protected set; }

    public abstract Role Role { get; }

    public void Renommer(string nom, string prenom)
    {
        Nom = Guard.NotNullOrWhiteSpace(nom, nameof(nom));
        Prenom = Guard.NotNullOrWhiteSpace(prenom, nameof(prenom));
    }

    public void ChangerLogin(string login)
        => Login = Guard.NotNullOrWhiteSpace(login, nameof(login));

    public void ChangerMotDePasse(string motDePasseHash)
        => MotDePasse = Guard.NotNullOrWhiteSpace(motDePasseHash, nameof(motDePasseHash));

    public void DefinirQuartierLivraisonDefaut(Quartier? quartier)
    {
        QuartierLivraisonDefaut = quartier;
        QuartierLivraisonDefautId = quartier?.Id;
    }

    public void DefinirModesDefaut(ModePaiement? modePaiement, ModeRecuperation? modeRecuperation)
    {
        ModePaiementDefaut = modePaiement;
        ModeRecuperationDefaut = modeRecuperation;
    }
}
