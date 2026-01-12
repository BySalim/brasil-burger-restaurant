<?php

namespace App\Repository;

use App\Entity\Zone;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

class ZoneRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Zone::class);
    }

    /**
     * Récupère toutes les zones actives
     */
    public function findActiveZones(): array
    {
        return $this->findBy(['estArchiver' => false], ['nom' => 'ASC']);
    }

    public function findById(int $id): ?Zone
    {
        return $this->findOneBy(['id' => $id]);
    }
}
