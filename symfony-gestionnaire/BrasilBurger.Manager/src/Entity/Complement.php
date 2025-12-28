<?php

namespace App\Entity;

use App\Enum\CategorieArticle;
use App\Enum\TypeComplement;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity]
class Complement extends Article
{
    #[ORM\Column(length: 20, enumType: TypeComplement::class, nullable: true)]
    private ?TypeComplement $typeComplement = null;

    #[ORM\Column(nullable: true)]
    private ?int $prix = null;

    public function getCategorie(): CategorieArticle
    {
        return CategorieArticle::COMPLEMENT;
    }

    public function getTypeComplement(): ?TypeComplement
    {
        return $this->typeComplement;
    }

    public function setTypeComplement(?TypeComplement $typeComplement): static
    {
        $this->typeComplement = $typeComplement;
        return $this;
    }

    public function getPrix(): ?int
    {
        return $this->prix;
    }

    public function setPrix(?int $prix): static
    {
        $this->prix = $prix;
        return $this;
    }
}
