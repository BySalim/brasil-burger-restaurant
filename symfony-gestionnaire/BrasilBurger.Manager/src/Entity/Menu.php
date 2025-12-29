<?php

namespace App\Entity;

use App\Enum\CategorieArticle;
use App\Enum\CategorieArticleQuantifier;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity]
class Menu extends Article
{
    #[ORM\Column(type: Types::TEXT, nullable: true)]
    private ?string $description = null;

    #[ORM\OneToMany(targetEntity: ArticleQuantifier::class, mappedBy: 'menu', cascade: ['persist', 'remove'], orphanRemoval: true)]
    private Collection $menuComposition;

    public function __construct()
    {
        parent::__construct();
        $this->menuComposition = new ArrayCollection();
    }

    public function getCategorie(): CategorieArticle
    {
        return CategorieArticle::MENU;
    }

    public function getDescription(): ?string
    {
        return $this->description;
    }

    public function setDescription(?string $description): static
    {
        $this->description = $description;
        return $this;
    }

    /**
     * @return Collection<int, ArticleQuantifier>
     */
    public function getMenuComposition(): Collection
    {
        return $this->menuComposition;
    }

    public function addMenuComposition(ArticleQuantifier $articleQuantifier): static
    {
        if (!$this->menuComposition->contains($articleQuantifier)) {
            $this->menuComposition->add($articleQuantifier);
            $articleQuantifier->setMenu($this);
            $articleQuantifier->setCategorie(CategorieArticleQuantifier::MENU);
        }

        return $this;
    }

    public function removeMenuComposition(ArticleQuantifier $articleQuantifier): static
    {
        if ($this->menuComposition->removeElement($articleQuantifier)) {
            // set the owning side to null (unless already changed)
            if ($articleQuantifier->getMenu() === $this) {
                $articleQuantifier->setMenu(null);
            }
        }

        return $this;
    }

    // Calcul du prix du menu basé sur ses composants
    public function getPrix(): int
    {
        $total = 0;
        foreach ($this->menuComposition as $articleQuantifier) {
            $article = $articleQuantifier->getArticle();
            $quantite = $articleQuantifier->getQuantite();
            $prixUnitaire = $article->getPrix() ?? 0;
            $total += ($prixUnitaire * $quantite);
        }
        return $total;
    }
}
