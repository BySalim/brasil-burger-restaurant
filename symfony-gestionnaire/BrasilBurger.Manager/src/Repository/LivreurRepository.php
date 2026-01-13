<?php

namespace App\Repository;

use App\Entity\Livreur;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\ORM\QueryBuilder;
use Doctrine\Persistence\ManagerRegistry;

class LivreurRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Livreur::class);
    }

    /**
     * Requête de base pour la liste des livreurs avec filtres
     *
     * @param string|null $search Recherche par nom, prénom, téléphone ou ID
     * @param bool|null $disponibilite true = disponible, false = en livraison, null = tous
     */
    public function findAllWithFiltersQB(
        ?string $search = null,
        ?bool $disponibilite = null
    ): QueryBuilder {
        $qb = $this->createQueryBuilder('l')
            ->where('l.estArchiver = :archived')
            ->setParameter('archived', false)
            ->orderBy('l.nom', 'ASC')
            ->addOrderBy('l.prenom', 'ASC');

        // Filtre par recherche
        if ($search) {
            $qb->andWhere('
                LOWER(l.nom) LIKE LOWER(:search)
                OR LOWER(l.prenom) LIKE LOWER(:search)
                OR LOWER(l.telephone) LIKE LOWER(:search)
                OR CAST(l.id AS string) LIKE :search
            ')
                ->setParameter('search', '%' . $search . '%');
        }

        // Filtre par disponibilité
        if ($disponibilite !== null) {
            $qb->andWhere('l.estDisponible = :dispo')
                ->setParameter('dispo', $disponibilite);
        }

        return $qb;
    }

    public function findById(int $livreur_id)
    {
        return $this->findOneBy(['id' => $livreur_id]);
    }
}
