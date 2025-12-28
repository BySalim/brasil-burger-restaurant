<?php

namespace App\Mapper;

use App\Entity\Article;
use App\DTO\ArticleListDTO;
use App\Infrastructure\Images\ImageStorageInterface;

final readonly class ArticleMapper
{
    public function __construct(
        private readonly ImageStorageInterface $imageStorage,
    ) {}

    /**
     * Map une entité Article vers un DTO ArticleListDTO
     */
    public function mapArticle(Article $article): ArticleListDTO
    {

        return new ArticleListDTO(
            id: $article->getId(),
            code: $article->getCode(),
            libelle: $article->getLibelle(),
            imageUrl: $this->imageStorage->getThumbnailUrl($article->getImagePublicId()),
            categorie: $article->getCategorie()->getEntityClass(),
            description: method_exists($article, 'getDescription') ? $article->getDescription() : null,
            prix: $article->getPrix(),
        );
    }

    /**
     * Map une collection d'articles vers des DTOs
     *
     * @param Article[] $articles
     * @return ArticleListDTO[]
     */
    public function mapArticles(array $articles): array
    {
        return array_map(
            fn (Article $article) => $this->mapArticle($article),
            $articles
        );
    }
}
