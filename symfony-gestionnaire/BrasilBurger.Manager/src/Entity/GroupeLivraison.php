<?php

namespace App\Entity;

use App\Enum\StatutLivraison;
use App\Repository\GroupeLivraisonRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: GroupeLivraisonRepository::class)]
#[ORM\Table(name: 'groupe_livraison')]
class GroupeLivraison
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\ManyToOne(targetEntity: Livreur::class, inversedBy: 'groupeLivraisons')]
    #[ORM\JoinColumn(name: 'id_livreur', referencedColumnName: 'id', nullable: false, onDelete: 'CASCADE')]
    private ?Livreur $livreur = null;

    #[ORM\Column(length: 20, enumType: StatutLivraison::class, options: ['default' => 'EN_COURS'])]
    private StatutLivraison $statut = StatutLivraison::EN_COURS;

    #[ORM\OneToMany(targetEntity: Livraison::class, mappedBy: 'groupeLivraison', cascade: ['persist', 'remove'])]
    private Collection $livraisons;

    public function __construct()
    {
        $this->livraisons = new ArrayCollection();
    }

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getLivreur(): ?Livreur
    {
        return $this->livreur;
    }

    public function setLivreur(?Livreur $livreur): static
    {
        $this->livreur = $livreur;
        return $this;
    }

    public function getStatut(): StatutLivraison
    {
        return $this->statut;
    }

    public function setStatut(StatutLivraison $statut): static
    {
        $this->statut = $statut;
        return $this;
    }

    /**
     * @return Collection<int, Livraison>
     */
    public function getLivraisons(): Collection
    {
        return $this->livraisons;
    }

    public function addLivraison(Livraison $livraison): static
    {
        if (!$this->livraisons->contains($livraison)) {
            $this->livraisons->add($livraison);
            $livraison->setGroupeLivraison($this);
        }

        return $this;
    }

    public function removeLivraison(Livraison $livraison): static
    {
        if ($this->livraisons->removeElement($livraison)) {
            if ($livraison->getGroupeLivraison() === $this) {
                $livraison->setGroupeLivraison(null);
            }
        }

        return $this;
    }
}
