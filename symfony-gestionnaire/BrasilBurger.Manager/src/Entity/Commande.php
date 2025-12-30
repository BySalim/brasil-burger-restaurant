<?php

namespace App\Entity;

use App\Enum\EtatCommande;
use App\Enum\ModeRecuperation;
use App\Repository\CommandeRepository;
use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: CommandeRepository::class)]
#[ORM\Table(name: 'commande')]
class Commande
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\Column(name: 'num_cmd', length: 50, unique: true)]
    private ?string $numCmd = null;

    #[ORM\Column(name: 'date_debut', type: Types::DATETIME_MUTABLE, options: ['default' => 'CURRENT_TIMESTAMP'])]
    private ?\DateTimeInterface $dateDebut = null;

    #[ORM\Column(name: 'date_fin', type: Types::DATETIME_MUTABLE, nullable: true)]
    private ?\DateTimeInterface $dateFin = null;

    #[ORM\Column]
    private ?int $montant = null;

    #[ORM\Column(length: 20, enumType: EtatCommande::class, options: ['default' => 'EN_ATTENTE'])]
    private EtatCommande $etat = EtatCommande::EN_ATTENTE;

    #[ORM\Column(name: 'type_recuperation', length: 20, enumType: ModeRecuperation::class)]
    private ?ModeRecuperation $typeRecuperation = null;

    #[ORM\OneToOne(targetEntity: Panier::class, inversedBy: 'commande')]
    #[ORM\JoinColumn(name: 'id_panier', referencedColumnName: 'id', nullable: true, onDelete: 'SET NULL')]
    private ?Panier $panier = null;

    #[ORM\ManyToOne(targetEntity: Client::class)]
    #[ORM\JoinColumn(name: 'id_client', referencedColumnName: 'id', nullable: false, onDelete: 'CASCADE')]
    private ?Client $client = null;

    #[ORM\OneToOne(targetEntity: InfoLivraison::class, inversedBy: 'commande')]
    #[ORM\JoinColumn(name: 'id_info_livraison', referencedColumnName: 'id', unique: true, nullable: false, onDelete: 'CASCADE')]
    private ?InfoLivraison $infoLivraison = null;

    #[ORM\OneToOne(targetEntity: Livraison::class, cascade: ['persist', 'remove'])]
    #[ORM\JoinColumn(name: 'id_livraison', referencedColumnName: 'id', nullable: true, onDelete: 'SET NULL')]
    private ?Livraison $livraison = null;

    #[ORM\OneToOne(targetEntity: Paiement::class, mappedBy: 'commande', cascade: ['persist', 'remove'])]
    private ?Paiement $paiement = null;

    public function __construct()
    {
        $this->dateDebut = new \DateTime();
    }

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getNumCmd(): ?string
    {
        return $this->numCmd;
    }

    public function setNumCmd(string $numCmd): static
    {
        $this->numCmd = $numCmd;
        return $this;
    }

    public function getDateDebut(): ?\DateTimeInterface
    {
        return $this->dateDebut;
    }

    public function setDateDebut(\DateTimeInterface $dateDebut): static
    {
        $this->dateDebut = $dateDebut;
        return $this;
    }

    public function getDateFin(): ?\DateTimeInterface
    {
        return $this->dateFin;
    }

    public function setDateFin(?\DateTimeInterface $dateFin): static
    {
        $this->dateFin = $dateFin;
        return $this;
    }

    public function getMontant(): ?int
    {
        return $this->montant;
    }

    public function setMontant(int $montant): static
    {
        $this->montant = $montant;
        return $this;
    }

    public function getEtat(): EtatCommande
    {
        return $this->etat;
    }

    public function setEtat(EtatCommande $etat): static
    {
        $this->etat = $etat;
        return $this;
    }

    public function getTypeRecuperation(): ?ModeRecuperation
    {
        return $this->typeRecuperation;
    }

    public function setTypeRecuperation(ModeRecuperation $typeRecuperation): static
    {
        $this->typeRecuperation = $typeRecuperation;
        return $this;
    }

    public function getPanier(): ?Panier
    {
        return $this->panier;
    }

    public function setPanier(?Panier $panier): static
    {
        $this->panier = $panier;
        return $this;
    }

    public function getClient(): ?Client
    {
        return $this->client;
    }

    public function setClient(?Client $client): static
    {
        $this->client = $client;
        return $this;
    }

    public function getInfoLivraison(): ?InfoLivraison
    {
        return $this->infoLivraison;
    }

    public function setInfoLivraison(?InfoLivraison $infoLivraison): static
    {
        $this->infoLivraison = $infoLivraison;
        return $this;
    }

    public function getLivraison(): ?Livraison
    {
        return $this->livraison;
    }

    public function setLivraison(?Livraison $livraison): static
    {
        $this->livraison = $livraison;
        return $this;
    }

    public function getPaiement(): ?Paiement
    {
        return $this->paiement;
    }

    public function setPaiement(?Paiement $paiement): static
    {
        // unset the owning side of the relation if necessary
        if ($paiement === null && $this->paiement !== null) {
            $this->paiement->setCommande(null);
        }

        // set the owning side of the relation if necessary
        if ($paiement !== null && $paiement->getCommande() !== $this) {
            $paiement->setCommande($this);
        }

        $this->paiement = $paiement;

        return $this;
    }
}
