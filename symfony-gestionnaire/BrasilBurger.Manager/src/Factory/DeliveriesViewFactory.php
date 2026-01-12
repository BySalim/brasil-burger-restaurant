<?php

namespace App\Factory;

use App\Entity\Client;
use App\Entity\Commande;
use App\Entity\InfoLivraison;
use App\Entity\Livraison;
use App\Entity\Livreur;
use App\Enum\StatutLivraison;
use App\Service\PersonService;
use App\Service\PhoneNumberService;
use App\ViewModel\DeliveryHeaderViewModel;
use App\ViewModel\DeliveryLocationNoteViewModel;
use App\ViewModel\PersonCardViewModel;

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
        $isEnCours = $statut === StatutLivraison::EN_COURS;

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

}
