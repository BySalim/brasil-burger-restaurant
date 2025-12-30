<?php

namespace App\Repository;

use App\DTO\ArticleVenduDTO;
use App\Entity\Article;
use App\Enum\CategorieArticle;
use App\Enum\EtatCommande;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;


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

    /**
     * Récupére les articles les plus populaires d'une journée
     * @return ArticleVenduDTO[]
     */
    public function findTopArticlesByDate(\DateTimeInterface $date, int $limit): array
    {
        return $this->createQueryBuilder('a')
            ->select(sprintf(
                'NEW %s(a, SUM(aq.quantite))',
                ArticleVenduDTO::class
            ))

            // Les Jointures (Article -> Quantifier -> Panier -> Commande)
            ->join('a.articleQuantifiers', 'aq')
            ->join('aq.panier', 'p')
            ->join('p.commande', 'c')

            ->groupBy('a.id')
            ->orderBy('SUM(aq.quantite)', 'DESC')
            ->setMaxResults($limit)

            ->where('c.dateDebut = :date')
            ->andWhere('c.etat != :etatAnnule')

            ->setParameter('date', $date->format('Y-m-d'))

            ->setParameter('etatAnnule', EtatCommande::ANNULER->value)

            ->getQuery()
            ->getResult();
    }

    /**
     * Compte le nombre d'articles DIFFÉRENTS vendus ce jour-là
     */
    public function countDistinctArticlesSold(\DateTimeInterface $date): int
    {
        return $this->createQueryBuilder('a')
            ->select('COUNT(DISTINCT a.id)')

            ->join('a.articleQuantifiers', 'aq')
            ->join('aq.panier', 'p')
            ->join('p.commande', 'c')

            ->where('c.dateDebut = :date')
            ->andWhere('c.etat != :etatAnnule')

            ->setParameter('date', $date->format('Y-m-d'))
            ->setParameter('etatAnnule', EtatCommande::ANNULER->value)

            ->getQuery()
            ->getSingleScalarResult();
    }
}
