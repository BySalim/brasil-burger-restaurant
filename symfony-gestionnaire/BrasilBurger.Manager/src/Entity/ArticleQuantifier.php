<?php

namespace App\Entity;

use App\Enum\CategorieArticleQuantifier;
use App\Repository\ArticleQuantifierRepository;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: ArticleQuantifierRepository::class)]
#[ORM\Table(name: 'article_quantifier')]
class ArticleQuantifier
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\Column]
    private ?int $quantite = null;

    #[ORM\Column(name: 'categorie_article_quantifier', length: 20, enumType: CategorieArticleQuantifier::class)]
    private ?CategorieArticleQuantifier $categorie = null;

    #[ORM\ManyToOne(inversedBy: 'articleQuantifiers')]
    #[ORM\JoinColumn(name: 'id_article', nullable: false)]
    private ?Article $article = null;

    #[ORM\ManyToOne(inversedBy: 'articleQuantifiers')]
    #[ORM\JoinColumn(name: 'id_panier', nullable: true, onDelete: 'CASCADE')]
    private ?Panier $panier = null;

    #[ORM\ManyToOne(inversedBy: 'menuComposition')]
    #[ORM\JoinColumn(name: 'id_menu', nullable: true, onDelete: 'CASCADE')]
    private ?Menu $menu = null;

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getQuantite(): ?int
    {
        return $this->quantite;
    }

    public function setQuantite(int $quantite): static
    {
        $this->quantite = $quantite;
        return $this;
    }

    public function getCategorie(): ?CategorieArticleQuantifier
    {
        return $this->categorie;
    }

    public function setCategorie(CategorieArticleQuantifier $categorie): static
    {
        $this->categorie = $categorie;
        return $this;
    }

    public function getArticle(): ?Article
    {
        return $this->article;
    }

    public function setArticle(?Article $article): static
    {
        $this->article = $article;
        return $this;
    }

    public function getPanier(): ?Panier
    {
        return $this->panier;
    }

    public function setPanier(?Panier $panier): static
    {
        $this->panier = $panier;
        return $this;
    }

    public function getMenu(): ?Menu
    {
        return $this->menu;
    }

    public function setMenu(?Menu $menu): static
    {
        $this->menu = $menu;
        return $this;
    }
}
