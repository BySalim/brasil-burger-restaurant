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
    public function findTopArticlesByDate(\DateTimeInterface $date, int $limit = 5): array
    {
        // Créer la plage horaire de la journée (00:00:00 à 23:59:59)
        $startDate = (clone $date)->setTime(0, 0, 0);
        $endDate   = (clone $date)->setTime(23, 59, 59);

        return $this->createQueryBuilder('a')
            // Sélection via le DTO
            ->select(sprintf(
                'NEW %s(a, SUM(aq.quantite))',
                ArticleVenduDTO::class
            ))

            // Les Jointures
            ->join('a.articleQuantifiers', 'aq')
            ->join('aq.panier', 'p')
            ->join('p.commande', 'c')

            ->groupBy('a')

            ->orderBy('SUM(aq.quantite)', 'DESC')
            ->setMaxResults($limit)

            ->where('c.dateDebut BETWEEN :start AND :end')
            ->andWhere('c.etat != :etatAnnule')

            // Paramètres
            ->setParameter('start', $startDate)
            ->setParameter('end', $endDate)
            ->setParameter('etatAnnule', EtatCommande::ANNULER)

            ->getQuery()
            ->getResult();
    }

    /**
     * Compte le nombre d'articles DIFFÉRENTS vendus ce jour-là
     */
    public function countDistinctArticlesSold(\DateTimeInterface $date): int
    {
        // Définir la plage horaire de 00:00:00 à 23:59:59
        $startDate = (clone $date)->setTime(0, 0, 0);
        $endDate   = (clone $date)->setTime(23, 59, 59);

        $result = $this->createQueryBuilder('a')
            ->select('COUNT(DISTINCT a.id)')

            ->join('a.articleQuantifiers', 'aq')
            ->join('aq.panier', 'p')
            ->join('p.commande', 'c')

            ->where('c.dateDebut BETWEEN :start AND :end')
            ->andWhere('c.etat != :etatAnnule')

            ->setParameter('start', $startDate)
            ->setParameter('end', $endDate)
            ->setParameter('etatAnnule', EtatCommande::ANNULER)

            ->getQuery()
            ->getSingleScalarResult();

        return (int) $result;
    }}
