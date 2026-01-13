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

    public function save(GroupeLivraison $groupe, bool $flush = false): void
    {
        $this->getEntityManager()->persist($groupe);

        if ($flush) {
            $this->getEntityManager()->flush();
        }
    }

    public function flush(): void
    {
        $this->getEntityManager()->flush();
    }
}
