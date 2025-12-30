<?php

namespace App\Repository;

use App\DTO\DailyStatsDTO;
use App\Entity\Commande;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

class CommandeRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Commande::class);
    }

    /**
     * Récupère les stats groupées par état pour une date donnée
     * @return DailyStatsDTO[]
     */
    public function getDailyStatsByDate(\DateTimeInterface $date): array
    {
        $startDate = (clone $date)->setTime(0, 0, 0);
        $endDate   = (clone $date)->setTime(23, 59, 59);

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
    }}
