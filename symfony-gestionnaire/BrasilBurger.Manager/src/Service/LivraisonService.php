<?php

namespace App\Service;

use App\Entity\Livraison;
use App\Enum\StatutLivraison;
use App\Repository\LivraisonRepository;
use Doctrine\ORM\EntityManagerInterface;

readonly class LivraisonService
{
    public function __construct(
        private LivraisonRepository    $livraisonRepository,
        private EntityManagerInterface $entityManager,
        private GroupeLivraisonService $groupeLivraisonService
    ) {
    }

    /**
     * Vérifie si une transition de statut est autorisée
     */
    public function canChangeStatus(StatutLivraison $currentStatut, StatutLivraison $newStatut): bool
    {
        return in_array($newStatut, $currentStatut->getAllowedTransitions(), true);
    }

    /**
     * Vérifie si une livraison peut être terminée
     */
    public function canComplete(Livraison $livraison): bool
    {
        return $this->canChangeStatus($livraison->getStatut(), StatutLivraison::EN_COURS);
    }

    /**
     * Termine une livraison et met à jour le groupe si nécessaire
     *
     * @throws \LogicException si la livraison ne peut pas être terminée
     */
    public function complete(Livraison $livraison): void
    {
        if (!$this->canComplete($livraison)) {
            throw new \LogicException(sprintf(
                'La livraison %d est déjà terminée',
                $livraison->getId()
            ));
        }

        $livraison->setStatut(StatutLivraison:: TERMINER);
        $livraison->setDateFin(new \DateTime());

        $this->livraisonRepository->save($livraison, true);

        $groupe = $livraison->getGroupeLivraison();
        if ($groupe) {
            $this->groupeLivraisonService->checkAndUpdateStatus($groupe);
        }
    }

    /**
     * Termine plusieurs livraisons en une seule transaction
     *
     * @param int[] $livraisonIds
     * @return array{success:  int, skipped: int, errors: string[]}
     */
    public function bulkComplete(array $livraisonIds): array
    {
        $result = [
            'success' => 0,
            'skipped' => 0,
            'errors' => []
        ];

        if (empty($livraisonIds)) {
            return $result;
        }

        $livraisons = [];
        foreach ($livraisonIds as $livraisonId) {
            $livraisons[] = $this->livraisonRepository->find($livraisonId);
        }

        // Garder en mémoire les groupes à vérifier pour éviter les doublons
        $groupesToCheck = [];

        foreach ($livraisons as $livraison) {
            try {
                if (! $this->canComplete($livraison)) {
                    $result['skipped']++;
                    continue;
                }

                $livraison->setStatut(StatutLivraison::TERMINER);
                $livraison->setDateFin(new \DateTime());
                $this->livraisonRepository->save($livraison, false);

                // Mémoriser le groupe (éviter de vérifier 2x le même groupe)
                $groupe = $livraison->getGroupeLivraison();
                if ($groupe && ! isset($groupesToCheck[$groupe->getId()])) {
                    $groupesToCheck[$groupe->getId()] = $groupe;
                }

                $result['success']++;

            } catch (\Exception $e) {
                $result['errors'][] = sprintf(
                    'Livraison #%d : %s',
                    $livraison->getId(),
                    $e->getMessage()
                );
            }
        }

        // Flush une seule fois toutes les livraisons
        $this->entityManager->flush();

        foreach ($groupesToCheck as $groupe) {
            $this->groupeLivraisonService->checkAndUpdateStatus($groupe);
        }

        return $result;
    }
}
