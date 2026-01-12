<?php

namespace App\Repository;

use App\Entity\Livraison;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

class LivraisonRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Livraison::class);
    }

    public function save(Livraison $livraison, bool $flush = false): void
    {
        $em = $this->getEntityManager();
        $em->persist($livraison);

        if ($flush) {
            $em->flush();
        }
    }

    public function findById(int $id): ?Livraison
    {
        return $this->findOneBy(['id' => $id]);
    }
}
