<?php

namespace App\Controller;

use App\Factory\DeliveriesViewFactory;
use App\Factory\OrdersViewFactory;
use App\Form\OrderFilterType;
use App\Repository\LivraisonRepository;
use App\ViewModel\DeliveryInfos;
use Knp\Component\Pager\PaginatorInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
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

    #[Route('/', name: 'app_deliveries')]
    public function index(Request $request, PaginatorInterface $paginator): Response
    {

//        $form = $this->createForm(OrderFilterType::class);
//        $form->handleRequest($request);
//
//        $filters = $form->getData();
//
//        $queryBuilder = $this->commandeRepository->findAllWithFiltersQB(
//            search: $filters['search'] ?? null,
//            date: $filters['date'] ?? null,
//            type: $filters['type'] ?? null,
//            status: $filters['status'] ?? null
//        );
//
//        $per_page = $filters['per_page'] ?? $this->params->get('app.pagination.default_per_page');
//        $pagination = $paginator->paginate(
//            $queryBuilder,
//            $request->query->getInt('page', 1),
//            $per_page
//        );
//        $totalItems = $pagination->getTotalItemCount();
//
//        $cmd_items = $pagination->getItems();
//
//        $orders = $this->ordersViewFactory->createOrderRows($cmd_items);

        return $this->render('deliveries/index.html.twig', [
        ]);
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
    #[Route('/orders/', name: 'app_deliveries_orders')]
    public function listOrdersToDelivered(): Response
    {
        return $this->render('deliveries/orders.html.twig', []);
    }



}
