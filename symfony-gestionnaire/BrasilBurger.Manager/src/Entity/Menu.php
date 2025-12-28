<?php

namespace App\Entity;

use App\Enum\CategorieArticle;
use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity]
class Menu extends Article
{
    #[ORM\Column(type: Types::TEXT, nullable: true)]
    private ?string $description = null;

    // TODO: Ajouter l'autre élément plus tard

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
}
