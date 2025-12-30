<?php

namespace App\Entity;

use App\Repository\InfoLivraisonRepository;
use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: InfoLivraisonRepository::class)]
#[ORM\Table(name: 'info_livraison')]
class InfoLivraison
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\ManyToOne(targetEntity: Zone::class, inversedBy: 'infoLivraisons')]
    #[ORM\JoinColumn(name: 'id_zone', referencedColumnName: 'id', nullable: false, onDelete: 'CASCADE')]
    private ?Zone $zone = null;

    #[ORM\ManyToOne(targetEntity: Quartier::class, inversedBy: 'infoLivraisons')]
    #[ORM\JoinColumn(name: 'id_quartier', referencedColumnName: 'id', nullable: false, onDelete: 'CASCADE')]
    private ?Quartier $quartier = null;

    #[ORM\Column(name: 'note_livraison', type: Types::TEXT, nullable: true)]
    private ?string $noteLivraison = null;

    #[ORM\Column(name: 'prix_livraison')]
    private ?int $prixLivraison = null;

    #[ORM\OneToOne(targetEntity: Commande::class, mappedBy: 'infoLivraison')]
    private ?Commande $commande = null;

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getZone(): ?Zone
    {
        return $this->zone;
    }

    public function setZone(?Zone $zone): static
    {
        $this->zone = $zone;
        return $this;
    }

    public function getQuartier(): ?Quartier
    {
        return $this->quartier;
    }

    public function setQuartier(?Quartier $quartier): static
    {
        $this->quartier = $quartier;
        return $this;
    }

    public function getNoteLivraison(): ?string
    {
        return $this->noteLivraison;
    }

    public function setNoteLivraison(?string $noteLivraison): static
    {
        $this->noteLivraison = $noteLivraison;
        return $this;
    }

    public function getPrixLivraison(): ?int
    {
        return $this->prixLivraison;
    }

    public function setPrixLivraison(int $prixLivraison): static
    {
        $this->prixLivraison = $prixLivraison;
        return $this;
    }

    public function getCommande(): ?Commande
    {
        return $this->commande;
    }

    public function setCommande(?Commande $commande): static
    {
        // unset the owning side of the relation if necessary
        if ($commande === null && $this->commande !== null) {
            $this->commande->setInfoLivraison(null);
        }

        // set the owning side of the relation if necessary
        if ($commande !== null && $commande->getInfoLivraison() !== $this) {
            $commande->setInfoLivraison($this);
        }

        $this->commande = $commande;

        return $this;
    }
}
