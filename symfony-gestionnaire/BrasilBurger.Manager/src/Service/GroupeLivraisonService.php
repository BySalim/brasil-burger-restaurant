<?php

namespace App\Service;

use App\Entity\GroupeLivraison;
use App\Entity\Livraison;
use App\Enum\StatutLivraison;
use App\Repository\GroupeLivraisonRepository;

readonly class GroupeLivraisonService
{
    public function __construct(
        private GroupeLivraisonRepository $groupeLivraisonRepository,
    ) {
    }

    /**
     * Vérifie si un groupe peut être terminé
     */
    public function canComplete(GroupeLivraison $groupe): bool
    {
        return $groupe->getStatut() === StatutLivraison::EN_COURS;
    }

    /**
     * Vérifie si toutes les livraisons du groupe sont terminées
     */
    private function areAllLivraisonsCompleted(GroupeLivraison $groupe): bool
    {
        $livraisons = $groupe->getLivraisons();

        if ($livraisons->isEmpty()) {
            return false;
        }

        return $livraisons->forAll(
            fn ($key, Livraison $livraison) =>
                $livraison->getStatut()->isFinal()
        );

    }

    /**
     * Vérifie si toutes les livraisons sont terminées et met à jour le statut du groupe
     */
    public function checkAndUpdateStatus(GroupeLivraison $groupe): bool
    {
        if ($groupe->getStatut() === StatutLivraison::TERMINER) {
            return false;
        }

        if ($this->areAllLivraisonsCompleted($groupe)) {
            $this->complete($groupe);
            return true;
        }

        return false;
    }

    /**
     * Termine un groupe de livraison
     *
     * @throws \LogicException si le groupe ne peut pas être terminé
     */
    public function complete(GroupeLivraison $groupe): void
    {
        if (!$this->canComplete($groupe)) {
            throw new \LogicException(sprintf(
                'Le groupe de livraison %d est déjà terminé',
                $groupe->getId()
            ));
        }

        $groupe->setStatut(StatutLivraison::TERMINER);
        $this->groupeLivraisonRepository->save($groupe, true);
    }

}
