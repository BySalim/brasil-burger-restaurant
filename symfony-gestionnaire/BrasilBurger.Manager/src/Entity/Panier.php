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

    #[ORM\Column(name: 'categorie_panier', length: 20, enumType: CategoriePanier::class)]
    private ?CategoriePanier $categoriePanier = null;

    #[ORM\OneToMany(mappedBy: 'panier', targetEntity: ArticleQuantifier::class, orphanRemoval: true)]
    private Collection $articleQuantifiers;

    #[ORM\OneToOne(inversedBy: 'panier', cascade: ['persist', 'remove'])]
    #[ORM\JoinColumn(name: 'id_commande', nullable: true)]
    private ?Commande $commande = null;

    public function __construct()
    {
        $this->articleQuantifiers = new ArrayCollection();
    }

    public function getId(): ?int
    {
        return $this->id;
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
            // set the owning side to null (unless already changed)
            if ($articleQuantifier->getPanier() === $this) {
                $articleQuantifier->setPanier(null);
            }
        }

        return $this;
    }

    public function getCommande(): ?Commande
    {
        return $this->commande;
    }

    public function setCommande(?Commande $commande): static
    {
        $this->commande = $commande;
        return $this;
    }
}
