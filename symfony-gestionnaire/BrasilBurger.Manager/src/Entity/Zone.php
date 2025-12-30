<?php

namespace App\Entity;

use App\Repository\ZoneRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: ZoneRepository::class)]
#[ORM\Table(name: 'zone')]
class Zone
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\Column(length: 100, unique: true)]
    private ?string $nom = null;

    #[ORM\Column(name: 'prix_livraison')]
    private ?int $prixLivraison = null;

    #[ORM\Column(name: 'est_archiver', options: ['default' => false])]
    private bool $estArchiver = false;

    #[ORM\OneToMany(targetEntity: Quartier::class, mappedBy: 'zone', cascade: ['persist', 'remove'])]
    private Collection $quartiers;

    #[ORM\OneToMany(targetEntity: InfoLivraison::class, mappedBy: 'zone')]
    private Collection $infoLivraisons;

    public function __construct()
    {
        $this->quartiers = new ArrayCollection();
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

    public function getPrixLivraison(): ?int
    {
        return $this->prixLivraison;
    }

    public function setPrixLivraison(int $prixLivraison): static
    {
        $this->prixLivraison = $prixLivraison;
        return $this;
    }

    public function isEstArchiver(): bool
    {
        return $this->estArchiver;
    }

    public function setEstArchiver(bool $estArchiver): static
    {
        $this->estArchiver = $estArchiver;
        return $this;
    }

    /**
     * @return Collection<int, Quartier>
     */
    public function getQuartiers(): Collection
    {
        return $this->quartiers;
    }

    public function addQuartier(Quartier $quartier): static
    {
        if (!$this->quartiers->contains($quartier)) {
            $this->quartiers->add($quartier);
            $quartier->setZone($this);
        }

        return $this;
    }

    public function removeQuartier(Quartier $quartier): static
    {
        if ($this->quartiers->removeElement($quartier)) {
            if ($quartier->getZone() === $this) {
                $quartier->setZone(null);
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
            $infoLivraison->setZone($this);
        }

        return $this;
    }

    public function removeInfoLivraison(InfoLivraison $infoLivraison): static
    {
        if ($this->infoLivraisons->removeElement($infoLivraison)) {
            if ($infoLivraison->getZone() === $this) {
                $infoLivraison->setZone(null);
            }
        }

        return $this;
    }
}
