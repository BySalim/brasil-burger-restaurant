<?php

namespace App\DTO;

/**
 * DTO pour afficher une liste d'articles
 */
final class ArticleListDTO
{
    public function __construct(
        public readonly int     $id,
        public readonly string  $code,
        public readonly string  $libelle,
        public readonly string  $imageUrl,
        public readonly string  $categorie,
        public readonly ?string $description = null,
        public readonly ?int    $prix = null,
    ) {}

    public function getId(): int
    {
        return $this->id;
    }

    public function getCode(): string
    {
        return $this->code;
    }

    public function getLibelle(): string
    {
        return $this->libelle;
    }

    public function getImageUrl(): string
    {
        return $this->imageUrl;
    }

    public function getDescription(): ?string
    {
        return $this->description;
    }

    public function getPrix(): ?int
    {
        return $this->prix;
    }

    public function getCategorie(): string
    {
        return $this->categorie;
    }
}
