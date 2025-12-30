<?php

namespace App\Factory;

use App\Entity\Commande;
use App\ViewModel\OrderRowViewModel;

final class OrdersViewFactory
{
    /**
     * @param iterable<Commande> $commandes
     * @return OrderRowViewModel[]
     */
    public function createOrderRows(iterable $commandes): array
    {
        $rows = [];

        foreach ($commandes as $commande) {
            $rows[] = $this->createOrderRow($commande);
        }

        return $rows;
    }

    private function createOrderRow(Commande $commande): OrderRowViewModel
    {
        $client = $commande->getClient();
        $panier = $commande->getPanier();
        $etat = $commande->getEtat();

        $typeEnum = $panier?->getCategoriePanier();

        $type = $typeEnum?->getLabel() ?? '--';
        $typeColorClasses = $typeEnum?->getColor()->getBadgeClasses() ?? '';
        $amount = number_format((float) $commande->getMontant(), 0, '', ' ');

        $clientName = trim(
            ($client?->getNom() ?? '') . ' ' . ($client?->getPrenom() ?? '')
        );
        $clientPhone = $client?->getTelephone() ?? '--';

        return new OrderRowViewModel(
            id: $commande->getId(),
            date: $commande->getDateDebut()->format('d M Y'),
            time: $commande->getDateDebut()->format('H : i'),
            code: $commande->getNumCmd(),

            type: $type,
            typeColorClasses: $typeColorClasses,

            amount: $amount,
            clientName: $clientName,
            clientPhone: $clientPhone,

            status: $etat->getLabel(),
            statusColorClasses: $etat->getColor()->getBadgeClasses(),

            editStatus: $etat->getEditableStatusMap(),
        );
    }
}
