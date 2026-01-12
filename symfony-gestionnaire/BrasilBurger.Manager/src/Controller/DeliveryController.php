<?php

namespace App\Controller;

use App\Factory\DeliveriesViewFactory;
use App\Factory\OrdersViewFactory;
use App\Repository\LivraisonRepository;
use App\ViewModel\DeliveryInfos;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Attribute\Route;

#[Route('/deliveries')]
class DeliveryController extends AbstractController
{
    public function __construct(
        private readonly LivraisonRepository $livraisonRepository,
        private readonly DeliveriesViewFactory $deliveriesViewFactory,
        private readonly OrdersViewFactory $ordersViewFactory,
    )
    {
    }


    #[Route('/{id}', name: 'app_deliveries_show', requirements: ['id' => '\d+'])]
    public function show(int $id): Response
    {
        $livraison = $this->livraisonRepository->findOneBy(['id' => $id]);
        if (!$livraison) {
            throw $this->createNotFoundException('Livraison introuvable');
        }
        $cmd_associe = $livraison->getCommande();
        $info_livr = $cmd_associe->getInfoLivraison();
        $livreur_associe = $livraison->getGroupeLivraison()->getLivreur();

        $deliveryLocation = $this->deliveriesViewFactory->createDeliveryLocationNote($info_livr);
        $deliveryClient = $this->ordersViewFactory->createCardClient($cmd_associe->getClient());
        $deliveryLivreur = $this->deliveriesViewFactory->createLivreurCard($livreur_associe);
        $deliveryHeader = $this->deliveriesViewFactory->createDeliveryHeader($livraison);
        $infos = new DeliveryInfos(
            deliveryId : $livraison->getId(),
            priceDelivery: $info_livr->getPrixLivraison(),
            orderCode: $cmd_associe->getNumCmd(),
            orderId: $cmd_associe->getId(),
        );

        return $this->render('deliveries/show.html.twig', [
            'deliveryHeader' => $deliveryHeader,
            'deliveryLocation' => $deliveryLocation,
            'deliveryClient' => $deliveryClient,
            'deliveryLivreur' => $deliveryLivreur,
            'infos' => $infos,
        ]);
    }
}
