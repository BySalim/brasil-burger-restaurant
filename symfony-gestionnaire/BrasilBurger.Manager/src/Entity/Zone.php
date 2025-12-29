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

    #[ORM\Column]
    private ?int $prixLivraison = null;

    #[ORM\Column(options: ['default' => false])]
    private bool $estArchiver = false;

    #[ORM\OneToMany(targetEntity: Quartier::class, mappedBy: 'zone', orphanRemoval: true)]
    private Collection $quartiers;

    public function __construct()
    {
        $this->quartiers = new ArrayCollection();
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
            // set the owning side to null (unless already changed)
            if ($quartier->getZone() === $this) {
                $quartier->setZone(null);
            }
        }

        return $this;
    }
}
