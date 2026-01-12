<?php

namespace App\Repository;

use App\Entity\GroupeLivraison;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

class GroupeLivraisonRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, GroupeLivraison::class);
    }

    public function save(GroupeLivraison $groupeLivraison, bool $flush = false): void
    {
        $em = $this->getEntityManager();
        $em->persist($groupeLivraison);

        if ($flush) {
            $em->flush();
        }
    }
}
