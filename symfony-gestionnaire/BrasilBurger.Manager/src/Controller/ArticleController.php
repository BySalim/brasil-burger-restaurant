<?php

namespace App\Controller;
use App\Mapper\ArticleMapper;
use App\Repository\ArticleRepository;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Attribute\Route;

class ArticleController extends AbstractController
{
    #[Route('/articles', name: 'app_articles')]
    public function list(
        ArticleRepository $articleRepository,
        ArticleMapper $mapper,
    ): Response {
        // Récupère les articles de la base
        $articles = $articleRepository->findAllNonArchives();

        // Mappe les entités vers les DTOs
        $articleDTOs = $mapper->mapArticles($articles);

        return $this->render('article/index.html.twig', [
            'articles' => $articleDTOs,
        ]);
    }
}
