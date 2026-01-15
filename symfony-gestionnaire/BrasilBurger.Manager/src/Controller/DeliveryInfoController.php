<?php

namespace App\Controller;

use App\Entity\InfoLivraison;
use App\Factory\DeliveriesViewFactory;
use App\Factory\OrdersViewFactory;
use App\Repository\InfoLivraisonRepository;
use App\Repository\LivraisonRepository;
use App\ViewModel\DeliveryInfos;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Attribute\Route;

#[Route('/deliveries', name: 'app_deliveries_')]
class DeliveryInfoController extends AbstractController
{
    public function __construct(
        private readonly InfoLivraisonRepository   $livraisonRepository,
        private readonly DeliveriesViewFactory $deliveriesViewFactory,
        private readonly OrdersViewFactory     $ordersViewFactory,
    )
    {
    }


    #[Route('/info/{id}', name: 'info_show', requirements: ['id' => '\d+'])]
    public function show(int $id): Response
    {
        /** @var InfoLivraison  $infoLivraison */
        $infoLivraison = $this->livraisonRepository->findOneBy(['id' => $id]);
        if (!$infoLivraison) {
            throw $this->createNotFoundException('Information de livraison introuvable');
        }

        $cmd_associe = $infoLivraison->getCommande();

        $deliveryLocation = $this->deliveriesViewFactory->createDeliveryLocationNote($infoLivraison);
        $deliveryClient = $this->ordersViewFactory->createCardClient($cmd_associe->getClient());
        $deliveryInfoId = $infoLivraison->getId();

        $infos = new DeliveryInfos(
            deliveryId: $infoLivraison->getId(),
            priceDelivery: $infoLivraison->getPrixLivraison(),
            orderCode: $cmd_associe->getNumCmd(),
            orderId: $cmd_associe->getId(),
        );

        return $this->render('delivery_info/index.html.twig', [
            'deliveryInfoId' => $deliveryInfoId,
            'deliveryLocation' => $deliveryLocation,
            'deliveryClient' => $deliveryClient,
            'infos' => $infos,
        ]);
    }


}
