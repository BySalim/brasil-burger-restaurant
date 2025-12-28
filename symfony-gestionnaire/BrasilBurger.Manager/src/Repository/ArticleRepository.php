<?php

namespace App\Repository;

use App\Entity\Article;
use App\Enum\CategorieArticle;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

/**
 * @extends ServiceEntityRepository<Article>
 */
class ArticleRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Article::class);
    }

    /**
     * Récupère tous les articles non archivés
     *
     * @return Article[]
     */
    public function findAllNonArchives(): array
    {
        return $this->createQueryBuilder('a')
            ->andWhere('a.estArchiver = :archiver')
            ->setParameter('archiver', false)
            ->orderBy('a.libelle', 'ASC')
            ->getQuery()
            ->getResult();
    }

    /**
     * Récupère tous les articles non archivés d'une catégorie donnée
     *
     * @return Article[]
     */
    public function findAllNonArchivesByCategorie(CategorieArticle $categorie): array
    {
        return $this->createQueryBuilder('a')
            ->andWhere('a INSTANCE OF :type')
            ->andWhere('a.estArchiver = :archiver')
            ->setParameter('type', $categorie->getEntityClass())
            ->setParameter('archiver', false)
            ->orderBy('a.libelle', 'ASC')
            ->getQuery()
            ->getResult();
    }
}
