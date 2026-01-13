<?php

namespace App\Repository;

use App\DTO\DailyStatsDTO;
use App\Entity\Commande;
use App\Enum\CategoriePanier;
use App\Enum\EtatCommande;
use App\Enum\ModeRecuperation;
use DateTimeInterface;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\ORM\QueryBuilder;
use Doctrine\Persistence\ManagerRegistry;


class CommandeRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Commande::class);
    }

    public function save(Commande $commande, bool $flush = false): void
    {
        $em = $this->getEntityManager();

        $em->persist($commande);

        if ($flush) {
            $em->flush();
        }
    }

    /**
     * Récupère les stats groupées par état pour une date donnée
     * @return DailyStatsDTO[]
     */
    public function getDailyStatsByDate(\DateTimeInterface $date): array
    {
        $startDate = (clone $date)->setTime(0, 0, 0);
        $endDate = (clone $date)->setTime(23, 59, 59);

        return $this->createQueryBuilder('c')
            ->select(sprintf(
                'NEW %s(c.etat, COUNT(c.id), SUM(c.montant))',
                DailyStatsDTO::class
            ))
            ->where('c.dateDebut BETWEEN :start AND :end')
            ->setParameter('start', $startDate)
            ->setParameter('end', $endDate)
            ->groupBy('c.etat')
            ->getQuery()
            ->getResult();
    }

    /**
     * Requête de base pour la liste des commandes avec TOUTES les jointures
     * Retourne un QueryBuilder pour permettre la pagination
     */
    public function findAllWithFiltersQB(
        ?string $search = null,
        ?DateTimeInterface $date = null,
        ?CategoriePanier $type = null,
        ?EtatCommande $status = null
    ): QueryBuilder
    {
        $qb = $this->createQueryBuilder('c')
            ->select('c', 'cl', 'p', 'info', 'z', 'q')
            ->join('c.client', 'cl')
            ->leftJoin('c.panier', 'p')
            ->leftJoin('c.infoLivraison', 'info')
            ->leftJoin('info.zone', 'z')
            ->leftJoin('info.quartier', 'q')
            ->orderBy('c.dateDebut', 'DESC');

        if ($search) {
            $qb->andWhere('c.numCmd LIKE :search')
                ->setParameter('search', '%' . $search . '%');
        }

        if ($date instanceof \DateTimeInterface) {
                $startOfDay = (clone $date)->setTime(0, 0, 0);
                $endOfDay = (clone $date)->setTime(23, 59, 59);

                $qb->andWhere('c.dateDebut BETWEEN :start AND :end')
                    ->setParameter('start', $startOfDay)
                    ->setParameter('end', $endOfDay);
        }

        if ($type) {
            $qb->andWhere('p.categoriePanier = :type')
                ->setParameter('type', $type);
        }

        if ($status) {
            $qb->andWhere('c.etat = :status')
                ->setParameter('status', $status);
        }

        return $qb;
    }

    public function findById(int $id): ?Commande
    {
        return $this->findOneBy(['id' => $id]);
    }

    /**
     * ⭐ Compte les commandes terminées à livrer sans livraison associée
     */
    public function countCompletedOrdersToDeliver(): int
    {
        return $this->createQueryBuilder('c')
            ->select('COUNT(c.id)')
            ->where('c.etat = :etat')
            ->andWhere('c.typeRecuperation = :mode')
            ->andWhere('c.livraison IS NULL')
            ->setParameter('etat', EtatCommande::TERMINER)
            ->setParameter('mode', ModeRecuperation::LIVRER)
            ->getQuery()
            ->getSingleScalarResult();
    }
}
