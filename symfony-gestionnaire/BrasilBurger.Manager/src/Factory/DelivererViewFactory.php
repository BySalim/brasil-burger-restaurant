<?php

namespace App\Factory;

use App\Entity\Livreur;
use App\Service\PhoneNumberService;
use App\ViewModel\DelivererRowViewModel;

readonly class DelivererViewFactory
{
    public function __construct(
        private PhoneNumberService $phoneNumberService,
    ){}

    public function createDelivererRow(Livreur $livreur): DelivererRowViewModel
    {
        $delivererName = $livreur->getNom() . ' ' . $livreur->getPrenom();
        $delivererPhone = $livreur->getTelephone();

        return new DelivererRowViewModel(
            id: $livreur->getId(),
            name: $delivererName,
            phone: $this->phoneNumberService->formatSn($delivererPhone),
            estDisponible: $livreur->isEstDisponible()
        );
    }

    /**
     * @param iterable<Livreur> $livreurs
     * @return DelivererRowViewModel[]
     */
    public function createDeliverersTable(array $livreurs): array
    {
        $rows = [];
        foreach ($livreurs as $livreur) {
            $rows[] = $this->createDelivererRow($livreur);
        }
        return $rows;
    }
}
