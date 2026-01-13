<?php

namespace App\Repository;

use App\Entity\Livraison;
use App\Entity\Livreur;
use App\Enum\StatutLivraison;
use DateTimeInterface;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\ORM\QueryBuilder;
use Doctrine\Persistence\ManagerRegistry;

class LivraisonRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Livraison::class);
    }

    public function save(Livraison $livraison, bool $flush = false): void
    {
        $this->getEntityManager()->persist($livraison);

        if ($flush) {
            $this->getEntityManager()->flush();
        }
    }

    public function flush(): void
    {
        $this->getEntityManager()->flush();
    }


    public function findById(int $id): ?Livraison
    {
        return $this->findOneBy(['id' => $id]);
    }

    public function findAllWithFiltersQB(
        ?string            $search = null,
        ?DateTimeInterface $date = null,
        ?int               $zoneId = null,
        ?StatutLivraison   $status = null,
        ?Livreur           $livreur = null
    ): QueryBuilder
    {
        $qb = $this->createQueryBuilder('l')
            ->select('l', 'g', 'liv', 'c', 'cl', 'info', 'z', 'q')
            ->join('l.groupeLivraison', 'g')
            ->join('g.livreur', 'liv')
            ->leftJoin('l.commande', 'c')
            ->leftJoin('c.client', 'cl')
            ->leftJoin('c.infoLivraison', 'info')
            ->leftJoin('info.zone', 'z')
            ->leftJoin('info.quartier', 'q')
            ->orderBy('l.dateDebut', 'DESC');

        // Filtre par recherche (code livraison, nom livreur, nom client)
        if ($search) {
            $qb->andWhere('
                LOWER(c.numCmd) LIKE LOWER(:search)
                OR LOWER(liv.nom) LIKE LOWER(:search)
                OR LOWER(liv.prenom) LIKE LOWER(:search)
                OR LOWER(cl.nom) LIKE LOWER(:search)
            ')
                ->setParameter('search', '%' . $search . '%');
        }

        // Filtre par date
        if ($date instanceof \DateTimeInterface) {
            $startOfDay = (clone $date)->setTime(0, 0, 0);
            $endOfDay = (clone $date)->setTime(23, 59, 59);

            $qb->andWhere('l.dateDebut BETWEEN :start AND :end')
                ->setParameter('start', $startOfDay)
                ->setParameter('end', $endOfDay);
        }

        // Filtre par zone
        if ($zoneId) {
            $qb->andWhere('z.id = :zone')
                ->setParameter('zone', $zoneId);
        }

        if ($status) {
            $qb->andWhere('l.statut = :status')
                ->setParameter('status', $status);
        }

        if ($livreur) {
            $qb->andWhere('liv.id = :livreur')
                ->setParameter('livreur', $livreur->getId());
        }

        return $qb;
    }

    /**
     * Récupère plusieurs livraisons par leurs IDs (optimisé pour bulk)
     *
     * @param int[] $ids
     * @return Livraison[]
     */
    public function findByIds(array $ids): array
    {
        if (empty($ids)) {
            return [];
        }

        return $this->createQueryBuilder('l')
            ->select('l', 'g', 'liv')
            ->join('l.groupeLivraison', 'g')
            ->join('g.livreur', 'liv')
            ->where('l.  id IN (:ids)')
            ->setParameter('ids', $ids)
            ->getQuery()
            ->getResult();
    }
}
