<?php

namespace App\Factory;

use App\Entity\Commande;
use App\Entity\InfoLivraison;
use App\Entity\Livraison;
use App\Entity\Livreur;
use App\Entity\Zone;
use App\Enum\StatutLivraison;
use App\Service\PersonService;
use App\Service\PhoneNumberService;
use App\ViewModel\DeliveryHeaderViewModel;
use App\ViewModel\DeliveryLocationNoteViewModel;
use App\ViewModel\DeliveryRowViewModel;
use App\ViewModel\OrderRowViewModel;
use App\ViewModel\OrderToDeliveredRowViewModel;
use App\ViewModel\PersonCardViewModel;
use App\ViewModel\StatusBadgeViewModel;
use App\ViewModel\ZoneWhithOrdersViewModel;

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

    public function createOrderToDeliveredRow(Commande $commande): OrderToDeliveredRowViewModel
    {
        $client = $commande->getClient();
        $clientCard = new PersonCardViewModel(
            name: $client->getNom() . ' ' . $client->getPrenom(),
            phone: $this->phoneNumberService->formatSn($client->getTelephone()),
        );

        return new OrderToDeliveredRowViewModel(
            id: $commande->getId(),
            code: $commande->getNumCmd(),
            zone: $commande->getInfoLivraison()->getZone()->getNom(),
            client: $clientCard,
        );
    }

    /**
     * @param Commande[] $commandes
     * @param Zone[] $zones
     * @return ZoneWhithOrdersViewModel[]
     */
    public function createZonesWithOrders(array $commandes, array $zones): array
    {
        // 1. Initialiser toutes les zones avec tableau vide
        $grouped = array_combine(
            array_map(fn($zone) => $zone->getId(), $zones),
            array_map(fn($zone) => ['zone' => $zone, 'orders' => []], $zones)
        );

        // 2. Ajouter les commandes dans leurs zones respectives
        array_walk($commandes, function ($commande) use (&$grouped) {
            $zoneId = $commande->getInfoLivraison()->getZone()->getId();
            if (isset($grouped[$zoneId])) {
                $grouped[$zoneId]['orders'][] = $this->createOrderToDeliveredRow($commande);
            }
        });

        // 3. Trier par nombre de commandes (décroissant)
        usort($grouped, fn($a, $b) => count($b['orders']) <=> count($a['orders']));

        // 4. Transformer en ViewModels
        return array_map(
            fn($data) => new ZoneWhithOrdersViewModel(
                id: $data['zone']->getId(),
                name: $data['zone']->getNom(),
                ordersToDelivered:  $data['orders'],
            ),
            $grouped
        );
    }

    /**
     * @param Livreur[] $livreurs
     * @return PersonCardViewModel[] $livreurs
     */
    public function createDeliverersCard(iterable $livreurs): array
    {
        $livreursCard = [];
        foreach ($livreurs as $livreur) {
            $livreursCard[] = new PersonCardViewModel(
                name: $livreur->getNom() . ' ' . $livreur->getPrenom(),
                phone: $this->phoneNumberService->formatSn($livreur->getTelephone()),
                id: $livreur->getId(),
                estDisponible: $livreur->isEstDisponible(),
            );
        }
        return $livreursCard;
    }

}
