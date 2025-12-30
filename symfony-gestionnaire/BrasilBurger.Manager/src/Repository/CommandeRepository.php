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
        return $this->createQueryBuilder('c')
            ->select(sprintf(
                'NEW %s(c.etat, COUNT(c.id), SUM(c.montant))',
                DailyStatsDTO::class
            ))
            ->where('c.dateDebut = :date')
            ->setParameter('date', $date->format('Y-m-d'))
            ->groupBy('c.etat')
            ->getQuery()
            ->getResult();
    }
}
