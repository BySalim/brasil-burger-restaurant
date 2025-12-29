<?php

namespace App\Entity;

use App\Enum\Role;
use App\Enum\ModePaiement;
use App\Enum\ModeRecuperation;
use App\Repository\UtilisateurRepository;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: UtilisateurRepository::class)]
#[ORM\Table(name: 'utilisateur')]
#[ORM\InheritanceType('SINGLE_TABLE')]
#[ORM\DiscriminatorColumn(name: 'role', type: 'string', length: 20, enumType: Role::class)]
#[ORM\DiscriminatorMap([
    'CLIENT' => Client::class,
    'GESTIONNAIRE' => Gestionnaire::class
])]
abstract class Utilisateur
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    protected ?int $id = null;

    #[ORM\Column(length: 100)]
    protected ?string $nom = null;

    #[ORM\Column(length: 100)]
    protected ?string $prenom = null;

    #[ORM\Column(length: 100, unique: true)]
    protected ?string $login = null;

    #[ORM\Column(name: 'mot_de_passe', length: 255)]
    protected ?string $motDePasse = null;

    #[ORM\ManyToOne]
    #[ORM\JoinColumn(name: 'id_quartier_livraison_defaut', referencedColumnName: 'id', nullable: true, onDelete: 'SET NULL')]
    protected ?Quartier $quartierLivraisonDefaut = null;

    #[ORM\Column(name: 'mode_paiement_defaut', length: 20, nullable: true, enumType: ModePaiement::class)]
    protected ?ModePaiement $modePaiementDefaut = null;

    #[ORM\Column(name: 'mode_recuperation_defaut', length: 20, nullable: true, enumType: ModeRecuperation::class)]
    protected ?ModeRecuperation $modeRecuperationDefaut = null;

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getNom(): ?string
    {
        return $this->nom;
    }

    public function setNom(string $nom): static
    {
        $this->nom = $nom;
        return $this;
    }

    public function getPrenom(): ?string
    {
        return $this->prenom;
    }

    public function setPrenom(string $prenom): static
    {
        $this->prenom = $prenom;
        return $this;
    }

    public function getLogin(): ?string
    {
        return $this->login;
    }

    public function setLogin(string $login): static
    {
        $this->login = $login;
        return $this;
    }

    public function getMotDePasse(): ?string
    {
        return $this->motDePasse;
    }

    public function setMotDePasse(string $motDePasse): static
    {
        $this->motDePasse = $motDePasse;
        return $this;
    }

    public function getQuartierLivraisonDefaut(): ?Quartier
    {
        return $this->quartierLivraisonDefaut;
    }

    public function setQuartierLivraisonDefaut(?Quartier $quartierLivraisonDefaut): static
    {
        $this->quartierLivraisonDefaut = $quartierLivraisonDefaut;
        return $this;
    }

    public function getModePaiementDefaut(): ?ModePaiement
    {
        return $this->modePaiementDefaut;
    }

    public function setModePaiementDefaut(?ModePaiement $modePaiementDefaut): static
    {
        $this->modePaiementDefaut = $modePaiementDefaut;
        return $this;
    }

    public function getModeRecuperationDefaut(): ?ModeRecuperation
    {
        return $this->modeRecuperationDefaut;
    }

    public function setModeRecuperationDefaut(?ModeRecuperation $modeRecuperationDefaut): static
    {
        $this->modeRecuperationDefaut = $modeRecuperationDefaut;
        return $this;
    }

    abstract public function getRole(): Role;
}
