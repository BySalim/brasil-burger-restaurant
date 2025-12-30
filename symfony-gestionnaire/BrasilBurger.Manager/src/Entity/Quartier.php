<?php

namespace App\Entity;

use App\Repository\QuartierRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: QuartierRepository::class)]
#[ORM\Table(name: 'quartier')]
class Quartier
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\Column(length: 100, unique: true)]
    private ?string $nom = null;

    #[ORM\ManyToOne(targetEntity: Zone::class, inversedBy: 'quartiers')]
    #[ORM\JoinColumn(name: 'id_zone', referencedColumnName: 'id', nullable: false, onDelete: 'CASCADE')]
    private ?Zone $zone = null;

    #[ORM\OneToMany(targetEntity: Utilisateur::class, mappedBy: 'quartierLivraisonDefaut')]
    private Collection $utilisateurs;

    #[ORM\OneToMany(targetEntity: InfoLivraison::class, mappedBy: 'quartier')]
    private Collection $infoLivraisons;

    public function __construct()
    {
        $this->utilisateurs = new ArrayCollection();
        $this->infoLivraisons = new ArrayCollection();
    }

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

    public function getZone(): ?Zone
    {
        return $this->zone;
    }

    public function setZone(?Zone $zone): static
    {
        $this->zone = $zone;
        return $this;
    }

    /**
     * @return Collection<int, Utilisateur>
     */
    public function getUtilisateurs(): Collection
    {
        return $this->utilisateurs;
    }

    public function addUtilisateur(Utilisateur $utilisateur): static
    {
        if (!$this->utilisateurs->contains($utilisateur)) {
            $this->utilisateurs->add($utilisateur);
            $utilisateur->setQuartierLivraisonDefaut($this);
        }

        return $this;
    }

    public function removeUtilisateur(Utilisateur $utilisateur): static
    {
        if ($this->utilisateurs->removeElement($utilisateur)) {
            if ($utilisateur->getQuartierLivraisonDefaut() === $this) {
                $utilisateur->setQuartierLivraisonDefaut(null);
            }
        }

        return $this;
    }

    /**
     * @return Collection<int, InfoLivraison>
     */
    public function getInfoLivraisons(): Collection
    {
        return $this->infoLivraisons;
    }

    public function addInfoLivraison(InfoLivraison $infoLivraison): static
    {
        if (!$this->infoLivraisons->contains($infoLivraison)) {
            $this->infoLivraisons->add($infoLivraison);
            $infoLivraison->setQuartier($this);
        }

        return $this;
    }

    public function removeInfoLivraison(InfoLivraison $infoLivraison): static
    {
        if ($this->infoLivraisons->removeElement($infoLivraison)) {
            if ($infoLivraison->getQuartier() === $this) {
                $infoLivraison->setQuartier(null);
            }
        }

        return $this;
    }
}
