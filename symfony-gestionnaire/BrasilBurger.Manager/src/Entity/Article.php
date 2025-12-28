<?php

namespace App\Entity;

use App\Enum\CategorieArticle;
use App\Repository\ArticleRepository;
use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: ArticleRepository::class)]
#[ORM\Table(name: 'article')]
#[ORM\InheritanceType('SINGLE_TABLE')]
#[ORM\DiscriminatorColumn(name: 'categorie', type: 'string', length: 20, enumType: CategorieArticle::class)]
#[ORM\DiscriminatorMap([
    'Burger' => Burger::class,
    'Menu' => Menu::class,
    'Complement' => Complement::class
])]
abstract class Article
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\Column(length: 50, unique: true)]
    private ?string $code = null;

    #[ORM\Column(length: 150)]
    private ?string $libelle = null;

    #[ORM\Column(type: Types::TEXT)]
    private ?string $imagePublicId = null;

    #[ORM\Column(options: ['default' => false])]
    private bool $estArchiver = false;

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getCode(): ?string
    {
        return $this->code;
    }

    public function setCode(string $code): static
    {
        $this->code = $code;
        return $this;
    }

    public function getLibelle(): ?string
    {
        return $this->libelle;
    }

    public function setLibelle(string $libelle): static
    {
        $this->libelle = $libelle;
        return $this;
    }

    public function getImagePublicId(): ?string
    {
        return $this->imagePublicId;
    }

    public function setImagePublicId(string $imagePublicId): static
    {
        $this->imagePublicId = $imagePublicId;
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

    abstract public function getCategorie(): CategorieArticle;
}
