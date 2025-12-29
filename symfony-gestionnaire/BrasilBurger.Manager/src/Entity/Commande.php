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

    #[ORM\Column(name: 'date_commande', type: Types::DATETIME_MUTABLE)]
    private ?\DateTimeInterface $dateCommande = null;

    #[ORM\Column(length: 20, enumType: EtatCommande::class)]
    private ?EtatCommande $etat = null;

    #[ORM\Column(name: 'type_recuperation', length: 20, enumType: ModeRecuperation::class)]
    private ?ModeRecuperation $typeRecuperation = null;

    #[ORM\ManyToOne]
    #[ORM\JoinColumn(name: 'id_client', nullable: false)]
    private ?Client $client = null;

    #[ORM\OneToOne(mappedBy: 'commande', cascade: ['persist', 'remove'])]
    private ?Panier $panier = null;

    #[ORM\OneToOne(inversedBy: 'commande', cascade: ['persist', 'remove'])]
    #[ORM\JoinColumn(name: 'id_info_livraison', nullable: true)]
    private ?InfoLivraison $infoLivraison = null;

    #[ORM\OneToOne(mappedBy: 'commande', cascade: ['persist', 'remove'])]
    private ?Paiement $paiement = null;

    #[ORM\OneToOne(mappedBy: 'commande', cascade: ['persist', 'remove'])]
    private ?Livraison $livraison = null;

    public function __construct()
    {
        $this->dateCommande = new \DateTime();
        $this->etat = EtatCommande::EN_ATTENTE;
    }

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getDateCommande(): ?\DateTimeInterface
    {
        return $this->dateCommande;
    }

    public function setDateCommande(\DateTimeInterface $dateCommande): static
    {
        $this->dateCommande = $dateCommande;
        return $this;
    }

    public function getEtat(): ?EtatCommande
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

    public function getClient(): ?Client
    {
        return $this->client;
    }

    public function setClient(?Client $client): static
    {
        $this->client = $client;
        return $this;
    }

    public function getPanier(): ?Panier
    {
        return $this->panier;
    }

    public function setPanier(?Panier $panier): static
    {
        // unset the owning side of the relation if necessary
        if ($panier === null && $this->panier !== null) {
            $this->panier->setCommande(null);
        }

        // set the owning side of the relation if necessary
        if ($panier !== null && $panier->getCommande() !== $this) {
            $panier->setCommande($this);
        }

        $this->panier = $panier;
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

    public function getLivraison(): ?Livraison
    {
        return $this->livraison;
    }

    public function setLivraison(Livraison $livraison): static
    {
        // set the owning side of the relation if necessary
        if ($livraison->getCommande() !== $this) {
            $livraison->setCommande($this);
        }

        $this->livraison = $livraison;
        return $this;
    }
}
