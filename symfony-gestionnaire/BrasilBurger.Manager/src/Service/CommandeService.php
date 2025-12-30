<?php

namespace App\Service;

use App\Entity\Commande;
use App\Enum\EtatCommande;
use App\Repository\CommandeRepository;

class CommandeService
{

    public function __construct(
        private readonly CommandeRepository $commandeRepository,
    )
    {
    }

    /**
     * Vérifie si une transition d'état est autorisée
     */
    public function canChangeStatus(EtatCommande $currentEtat, EtatCommande $newEtat): bool
    {
        return in_array($newEtat, $currentEtat->getAllowedTransitions(), true);
    }

    /**
     * Change l'état d'une commande
     */
    public function changeStatus(Commande $commande, EtatCommande $newEtat): void
    {
        if (!$this->canChangeStatus($commande->getEtat(), $newEtat)) {
            throw new \LogicException(sprintf(
                'Impossible de passer de "%s" à "%s"',
                $commande->getEtat()->value,
                $newEtat->value
            ));
        }

        $commande->setEtat($newEtat);

        // Si commande finalisée, enregistrer date de fin
        if ($newEtat->isFinal()) {
            $commande->setDateFin(new \DateTime());
        }

        $this->commandeRepository->save($commande, true);

    }

}
