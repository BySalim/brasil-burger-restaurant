<?php

namespace App\Entity;

use App\Repository\LivreurRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: LivreurRepository::class)]
#[ORM\Table(name: 'livreur')]
class Livreur
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\Column(length: 100)]
    private ?string $nom = null;

    #[ORM\Column(length: 100)]
    private ?string $prenom = null;

    #[ORM\Column(length: 20, unique: true)]
    private ?string $telephone = null;

    #[ORM\Column(name: 'est_archiver', options: ['default' => false])]
    private bool $estArchiver = false;

    #[ORM\Column(name: 'est_disponible', options: ['default' => true])]
    private bool $estDisponible = true;

    #[ORM\OneToMany(targetEntity: GroupeLivraison::class, mappedBy: 'livreur')]
    private Collection $groupeLivraisons;

    public function __construct()
    {
        $this->groupeLivraisons = new ArrayCollection();
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

    public function getPrenom(): ?string
    {
        return $this->prenom;
    }

    public function setPrenom(string $prenom): static
    {
        $this->prenom = $prenom;
        return $this;
    }

    public function getTelephone(): ?string
    {
        return $this->telephone;
    }

    public function setTelephone(string $telephone): static
    {
        $this->telephone = $telephone;
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

    public function isEstDisponible(): bool
    {
        return $this->estDisponible;
    }

    public function setEstDisponible(bool $estDisponible): static
    {
        $this->estDisponible = $estDisponible;
        return $this;
    }

    /**
     * @return Collection<int, GroupeLivraison>
     */
    public function getGroupeLivraisons(): Collection
    {
        return $this->groupeLivraisons;
    }

    public function addGroupeLivraison(GroupeLivraison $groupeLivraison): static
    {
        if (!$this->groupeLivraisons->contains($groupeLivraison)) {
            $this->groupeLivraisons->add($groupeLivraison);
            $groupeLivraison->setLivreur($this);
        }

        return $this;
    }

    public function removeGroupeLivraison(GroupeLivraison $groupeLivraison): static
    {
        if ($this->groupeLivraisons->removeElement($groupeLivraison)) {
            if ($groupeLivraison->getLivreur() === $this) {
                $groupeLivraison->setLivreur(null);
            }
        }

        return $this;
    }
}
