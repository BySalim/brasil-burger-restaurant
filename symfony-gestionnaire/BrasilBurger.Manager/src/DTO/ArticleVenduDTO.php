<?php

namespace App\DTO;

use App\Entity\Article;

class ArticleVenduDTO
{
    public function __construct(
        public Article $article,
        public int $quantiteTotal
    ) {}
}
