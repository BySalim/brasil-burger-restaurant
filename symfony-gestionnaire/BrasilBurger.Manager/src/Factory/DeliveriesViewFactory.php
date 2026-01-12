<?php

namespace App\Factory;

use App\Entity\InfoLivraison;
use App\Entity\Livraison;
use App\Entity\Livreur;
use App\Enum\StatutLivraison;
use App\Service\PersonService;
use App\Service\PhoneNumberService;
use App\ViewModel\DeliveryHeaderViewModel;
use App\ViewModel\DeliveryLocationNoteViewModel;
use App\ViewModel\DeliveryRowViewModel;
use App\ViewModel\OrderRowViewModel;
use App\ViewModel\PersonCardViewModel;
use App\ViewModel\StatusBadgeViewModel;

readonly class DeliveriesViewFactory
{
    public function __construct(
        private PhoneNumberService $phoneNumberService,
        private PersonService $personService,
    )
    {}

    public function createLivreurCard(Livreur $livreur): PersonCardViewModel
    {
        $nom = $livreur->getNom();
        $prenom = $livreur->getPrenom();
        $name = $nom . ' ' . $prenom;
        $phone = $livreur->getTelephone();
        $id = $livreur->getId();
        return new PersonCardViewModel(
            name: $name,
            phone: $this->phoneNumberService->formatSn($phone),
            id: $id,
            initials: $this->personService->initials($nom, $prenom)
        );
    }

    public function createDeliveryLocationNote(InfoLivraison $infoLivraison): DeliveryLocationNoteViewModel
    {
        $zone = $infoLivraison->getZone();
        $neighborhood = $infoLivraison->getQuartier();
        $note = $infoLivraison->getNoteLivraison();
        return new DeliveryLocationNoteViewModel(
            zone: $zone->getNom(),
            neighborhood: $neighborhood->getNom(),
            note: $note
        );
    }

    public function createDeliveryHeader(Livraison $livraison): DeliveryHeaderViewModel
    {
        $id = $livraison->getId();

        $statut = $livraison->getStatut();
        $statusLabel = $statut->getLabel();
        $statusColor = $statut->getColor()->getBadgeClasses();
        $statusIcon = $statut->getIcon();
        $isEnCours = ! $statut->isFinal();

        $livAt = $livraison->getDateDebut();
        $date = $livAt ? $livAt->format('d F Y') : '-/-/-';
        $time = $livAt ? $livAt->format('H:i') : '-:-';
        return new DeliveryHeaderViewModel(
            id: $id,
            statusLabel: $statusLabel,
            statusColor: $statusColor,
            statusIcon: $statusIcon,
            date: $date,
            time: $time,
            isEnCours: $isEnCours,
        );
    }

    public function createDeliveryRow(Livraison $livraison): DeliveryRowViewModel
    {
        $debutAt = $livraison->getDateDebut();
        $cmd_associe = $livraison->getCommande();

        $deliverer = $livraison->getGroupeLivraison()->getLivreur();
        $delivererName = $deliverer->getNom() . ' ' . $deliverer->getPrenom();

        $client = $cmd_associe->getClient();
        $clientName = $client->getNom() . ' ' . $client->getPrenom();

        $status = $livraison->getStatut();
        $isCompleted = $status->isFinal();

        return new DeliveryRowViewModel(
            id: $livraison->getId(),
            date: $debutAt->format('d/m/y'),
            time: $debutAt->format('H:i'),
            zone: $cmd_associe->getInfoLivraison()->getZone()->getNom(),
            delivererName: $delivererName,
            delivererPhone: $this->phoneNumberService->formatSn($deliverer->getTelephone()),
            clientName: $clientName,
            clientPhone: $this->phoneNumberService->formatSn($client->getTelephone()),
            statusLabel: $status->getLabel(),
            statusBadgeColor: $status->getColor()->getBadgeClasses(),
            statusIcon: $status->getIcon(),
            isCompleted: $isCompleted,
        );
    }

    /**
     * @param iterable<Livraison> $livraisons
     * @return OrderRowViewModel[]
     */
    public function createDeleveries(iterable $livraisons): array
    {
        $row = [];
        foreach ($livraisons as $livraison) {
            $row[] = $this->createDeliveryRow($livraison);
        }
        return $row;
    }


    public function createStatusBadge(StatutLivraison $statut): StatusBadgeViewModel
    {
        return new StatusBadgeViewModel(
            label: $statut->getLabel(),
            icon: $statut->getIcon(),
            color: $statut->getColor()->getSolidBadgeClasses(),
        );
    }

}
