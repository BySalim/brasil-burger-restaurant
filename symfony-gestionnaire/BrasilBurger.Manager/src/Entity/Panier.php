<?php

namespace App\Entity;

use App\Enum\CategoriePanier;
use App\Repository\PanierRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: PanierRepository::class)]
#[ORM\Table(name: 'panier')]
class Panier
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\Column(name: 'montant_total', options: ['default' => 0])]
    private int $montantTotal = 0;

    #[ORM\Column(name: 'categorie_panier', length: 20, enumType: CategoriePanier::class)]
    private ?CategoriePanier $categoriePanier = null;

    #[ORM\OneToOne(targetEntity: Commande::class, mappedBy: 'panier')]
    private ?Commande $commande = null;

    #[ORM\OneToMany(targetEntity: ArticleQuantifier::class, mappedBy: 'panier', cascade: ['persist', 'remove'])]
    private Collection $articleQuantifiers;

    public function __construct()
    {
        $this->articleQuantifiers = new ArrayCollection();
    }

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getMontantTotal(): int
    {
        return $this->montantTotal;
    }

    public function setMontantTotal(int $montantTotal): static
    {
        $this->montantTotal = $montantTotal;
        return $this;
    }

    public function getCategoriePanier(): ?CategoriePanier
    {
        return $this->categoriePanier;
    }

    public function setCategoriePanier(CategoriePanier $categoriePanier): static
    {
        $this->categoriePanier = $categoriePanier;
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
            $this->commande->setPanier(null);
        }

        // set the owning side of the relation if necessary
        if ($commande !== null && $commande->getPanier() !== $this) {
            $commande->setPanier($this);
        }

        $this->commande = $commande;

        return $this;
    }

    /**
     * @return Collection<int, ArticleQuantifier>
     */
    public function getArticleQuantifiers(): Collection
    {
        return $this->articleQuantifiers;
    }

    public function addArticleQuantifier(ArticleQuantifier $articleQuantifier): static
    {
        if (!$this->articleQuantifiers->contains($articleQuantifier)) {
            $this->articleQuantifiers->add($articleQuantifier);
            $articleQuantifier->setPanier($this);
        }

        return $this;
    }

    public function removeArticleQuantifier(ArticleQuantifier $articleQuantifier): static
    {
        if ($this->articleQuantifiers->removeElement($articleQuantifier)) {
            if ($articleQuantifier->getPanier() === $this) {
                $articleQuantifier->setPanier(null);
            }
        }

        return $this;
    }
}
