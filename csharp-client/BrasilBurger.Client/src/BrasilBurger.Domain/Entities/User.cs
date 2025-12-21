using BrasilBurger.Domain.Entities;
using BrasilBurger.Domain.ValueObjects;

namespace BrasilBurger.Domain.Entities;

public class User : Entity
{
    public string Nom { get; private set; }
    public string Prenom { get; private set; }
    public string Login { get; private set; }
    public string MotDePasse { get; private set; }
    public Role Role { get; private set; }
    public Telephone? Telephone { get; private set; }
    public int? IdQuartierLivraisonDefaut { get; private set; }
    public ModePaiement? ModePaiementDefaut { get; private set; }
    public ModeRecuperation? ModeRecuperationDefaut { get; private set; }

    protected User() { }

    public User(string nom, string prenom, string login, string motDePasse, Role role, Telephone? telephone = null)
    {
        Nom = nom ?? throw new ArgumentNullException(nameof(nom));
        Prenom = prenom ?? throw new ArgumentNullException(nameof(prenom));
        Login = login ?? throw new ArgumentNullException(nameof(login));
        MotDePasse = motDePasse ?? throw new ArgumentNullException(nameof(motDePasse));
        Role = role;
        Telephone = telephone;

        if (role == Role.Client && telephone == null)
            throw new DomainException("Le téléphone est obligatoire pour un client.");
    }

    public void UpdateContactInfo(Telephone telephone, int? quartierId, ModePaiement? paiement, ModeRecuperation? recuperation)
    {
        Telephone = telephone;
        IdQuartierLivraisonDefaut = quartierId;
        ModePaiementDefaut = paiement;
        ModeRecuperationDefaut = recuperation;
    }
}