<?php

namespace App\Service;

use App\Entity\GroupeLivraison;
use App\Entity\Livraison;
use App\Enum\EtatCommande;
use App\Enum\ModeRecuperation;
use App\Enum\StatutLivraison;
use App\Repository\CommandeRepository;
use App\Repository\GroupeLivraisonRepository;
use App\Repository\LivraisonRepository;
use App\Repository\LivreurRepository;
use Doctrine\ORM\EntityManagerInterface;
use LogicException;

readonly class LivraisonService
{
    public function __construct(
        private LivraisonRepository    $livraisonRepository,
        private GroupeLivraisonService $groupeLivraisonService,
        private GroupeLivraisonRepository $groupeLivraisonRepository,
        private CommandeRepository $commandeRepository,
        private LivreurRepository $livreurRepository,
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
        return $this->canChangeStatus($livraison->getStatut(), StatutLivraison::TERMINER);
    }

    /**
     * Termine une livraison et met à jour le groupe si nécessaire
     *
     * @throws LogicException si la livraison ne peut pas être terminée
     */
    public function complete(Livraison $livraison): void
    {
        if (!$this->canComplete($livraison)) {
            var_dump("test ok");
            throw new LogicException(sprintf(
                'La livraison %d est déjà terminée',
                $livraison->getId()
            ));
        }

        $livraison->setStatut(StatutLivraison::TERMINER);
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
            $livraison = $this->livraisonRepository->findById($livraisonId);

            if ($livraison === null) {
                $result['errors'][] = sprintf('Livraison #%d introuvable', $livraisonId);
                continue;
            }

            $livraisons[] = $livraison;
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
                $this->livraisonRepository->save($livraison);

                // Mémoriser le groupe
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

        // Flush une seule fois
        $this->livraisonRepository->flush();

        foreach ($groupesToCheck as $groupe) {
            $this->groupeLivraisonService->checkAndUpdateStatus($groupe);
        }

        return $result;
    }

    /**
     * Affecter des commandes à un livreur
     *
     * @param int[] $commandeIds
     * @param int $livreurId
     * @return array{assigned: int, skipped: int, livreurName: string}
     * @throws LogicException
     */
    public function assignOrdersToDeliverer(array $commandeIds, int $livreurId): array
    {
        // Récupérer le livreur
        $livreur = $this->livreurRepository->findById($livreurId);
        if (!$livreur) {
            throw new LogicException('Livreur introuvable');
        }

        if (!$livreur->isEstDisponible()) {
            throw new LogicException(sprintf(
                '%s %s est déjà en livraison',
                $livreur->getPrenom(),
                $livreur->getNom()
            ));
        }

        // Récupérer les commandes en une seule requête
        $commandes = $this->commandeRepository->findByIds($commandeIds);

        if (empty($commandes)) {
            throw new LogicException('Aucune commande valide trouvée');
        }

        // Créer le groupe de livraison
        $groupe = new GroupeLivraison();
        $groupe->setLivreur($livreur);

        $this->groupeLivraisonRepository->save($groupe);

        // Créer les livraisons
        $result = ['assigned' => 0, 'skipped' => 0, 'livreurName' => $livreur->getPrenom() . ' ' . $livreur->getNom()];

        foreach ($commandes as $commande) {
            // Vérifier que la commande est éligible
            if (
                $commande->getEtat() !== EtatCommande::TERMINER ||
                $commande->getTypeRecuperation() !== ModeRecuperation::LIVRER ||
                $commande->getLivraison() !== null
            ) {
                $result['skipped']++;
                continue;
            }

            $livraison = new Livraison();
            $livraison->setCommande($commande);
            $livraison->setGroupeLivraison($groupe);

            $this->livraisonRepository->save($livraison);
            $commande->setLivraison($livraison);

            $result['assigned']++;
        }

        if ($result['assigned'] === 0) {
            throw new LogicException('Aucune commande éligible à la livraison');
        }

        $livreur->setEstDisponible(false);
        $this->livreurRepository->flush();

        return $result;
    }
}
