<?php

namespace App\Factory;

use App\Entity\ArticleQuantifier;
use App\Entity\Client;
use App\Entity\Commande;
use App\Enum\ModeRecuperation;
use App\Infrastructure\Images\ImageStorageInterface;
use App\Service\PersonService;
use App\Service\PhoneNumberService;
use App\ViewModel\PersonCardViewModel;
use App\ViewModel\DeliveryCardViewModel;
use App\ViewModel\OrderHeaderViewModel;
use App\ViewModel\OrderRowViewModel;
use App\ViewModel\PaymentCardViewModel;
use App\ViewModel\ProductSaleRowViewModel;
use function PHPUnit\Framework\isNull;

final readonly class OrdersViewFactory
{
    public function __construct(
        private PhoneNumberService $phoneNumberService,
        private ImageStorageInterface $imageStorage,
        private PersonService  $personService,
    )
    {

    }
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
            clientPhone: $this->phoneNumberService->formatSn($clientPhone),

            status: $etat->getLabel(),
            statusColorClasses: $etat->getColor()->getBadgeClasses(),

            editStatus: $etat->getEditableStatusMap(),
        );
    }

    public function createOrderHeader(Commande $commande): OrderHeaderViewModel
    {
        $createdAt = $commande->getDateDebut();
        $date = $createdAt->format('d M Y');
        $time = $createdAt->format('H:i');

        // Type/catégorie
        $categorie = $commande->getPanier()->getCategoriePanier();
        $categorieLabel = $categorie->getLabel() ?? '--';
        $categorieClass = $categorie->getColor()->getBadgeClasses() ?? '';

        // Statut
        $statut = $commande->getEtat();
        $statusLabel = $statut->getLabel() ?? '--';
        $statusIcon  = $statut->getIcon() ?? '';
        $statusClass = $statut->getColor()->getBadgeClasses() ?? '';
        $statusIconBg = $statut->getColor()->getIconClasseBg() ?? '';

        return new OrderHeaderViewModel(
            id: $commande->getId(),
            code: $commande->getNumCmd(),
            date: $date,
            time: $time,
            typeLabel: $categorieLabel,
            typeClass: $categorieClass,
            totalAmount: $commande->getMontant(),
            statusLabel: $statusLabel,
            statusIcon: $statusIcon,
            statusClass: $statusClass,
            statusIconBg: $statusIconBg,
        );
    }

    public function createProductSaleRow(ArticleQuantifier $aq): ProductSaleRowViewModel
    {
        $article = $aq->getArticle();
        $categorie = $article->getCategorie();
        $imgPublicId = $article->getImagePublicId();
        return new ProductSaleRowViewModel(
                    name: $article->getLibelle(),
                    categoryLabel: $categorie->getLabel(),
                    categoryClass: $categorie->getColor()->getBadgeClasses(),
                    unitPrice: $aq->getMontant(),
                    quantity: $aq->getQuantite(),
                    imageUrl: $this->imageStorage->getImageUrl($imgPublicId),
        );
    }

    public function createProductsSales(Commande $commande): array
    {
        $articlesQuantifiers = $commande->getPanier()->getArticleQuantifiers();
        $rows = [];
        foreach ($articlesQuantifiers as $aq) {
            $rows[] = $this->createProductSaleRow($aq);
        }

        return $rows;
    }

    public function createCardClient(Client $client): PersonCardViewModel
    {

        $nom = $client->getNom() ?? '';
        $prenom = $client->getPrenom() ?? '';
        $name = $nom . ' ' . $prenom;
        $phone = $this->phoneNumberService->formatSn($client->getTelephone());

        return new PersonCardViewModel(
            name: $name,
            phone: $phone,
            initials: $this->personService->initials($nom, $prenom),
        );
    }

    public function createDeliveryCard(Commande $commande): DeliveryCardViewModel
    {
        $livraison = $commande->getLivraison();
        $infoLivraison = $commande->getInfoLivraison();

        $mode = $commande->getTypeRecuperation();
        $label = $mode->getLabel() ?? '';
        $icon = $mode->getIcon() ?? '';
        $class = $mode->getColor()->getBadgeClasses() ?? '';
        $isDelivered = $mode === ModeRecuperation::LIVRER;

        return new DeliveryCardViewModel(
            label: $label,
            icon: $icon,
            color: $class,
            deliveryPrice: $isDelivered ? $infoLivraison->getPrixLivraison() : 0,
            deliveryId: $livraison?->getId(),
            deliveryInfoId: $isDelivered ? $infoLivraison->getId() : null,
            isDelivered: $isDelivered,
        );
    }

    public function createPaymentCard(Commande $commande): PaymentCardViewModel
    {

        $paiement = $commande->getPaiement();

        $method = $paiement->getModePaie();

        $labelMethod = $method->getLabel() ?? '';
        $classMethod = $method->getColor()->getBadgeClasses() ?? '';
        $imageUrl = $method->getImageUrl() ?? null;

        $paidAt = $paiement->getDatePaie();
        $date = $paidAt ? $paidAt->format('d/m/y') : '--';
        $time = $paidAt ? $paidAt->format('H:i') : '--';

        return new PaymentCardViewModel(
            amount: $paiement->getMontantPaie(),
            date: $date,
            time: $time,
            reference: $paiement->getReferencePaiementExterne() ?? '--',
            methodLabel: $labelMethod,
            methodBadgeClass: $classMethod,
            methodImageUrl: $imageUrl,
            methodIcon: 'credit_card',
        );
    }

}
