<?php

namespace App\Controller;

use App\Entity\Livraison;
use App\Factory\DeliveriesViewFactory;
use App\Factory\OrdersViewFactory;
use App\Form\DeliveryFilterType;
use App\Form\OrderFilterType;
use App\Repository\LivraisonRepository;
use App\Service\LivraisonService;
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
        private readonly LivraisonService $livraisonService
    )
    {
    }

    #[Route('/', name: 'app_deliveries')]
    public function index(Request $request, PaginatorInterface $paginator): Response
    {

        $form = $this->createForm(DeliveryFilterType::class);
        $form->handleRequest($request);

        $filters = $form->getData();
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
            'form' => $form,
        ]);
    }

    /**
     * Terminer UNE livraison individuelle
     */
    #[Route('/{id}/complete', name: 'app_deliveries_complete', methods: ['POST'])]
    public function completeDelivery(
        Livraison $livraison,
        Request $request
    ): Response {

        $token = $request->request->get('_token');
        if (!$this->isCsrfTokenValid('complete-delivery-' . $livraison->getId(), $token)) {
            $this->addFlash('error', 'Token de sécurité invalide');
            return $this->redirectToRoute('app_deliveries_index');
        }

        try {
            $this->livraisonService->complete($livraison);

            $this->addFlash('success', sprintf(
                'Livraison #%d terminée avec succès',
                $livraison->getId()
            ));

        } catch (\LogicException $e) {
            $this->addFlash('error', $e->getMessage());
        } catch (\Exception $e) {
            $this->addFlash('error', 'Une erreur est survenue lors de la finalisation');
        }

        return $this->redirectToRoute('app_deliveries_index');
    }

    /**
     * Terminer PLUSIEURS livraisons (bulk action)
     */
    #[Route('/bulk/complete', name: 'app_deliveries_bulk_complete', methods: ['POST'])]
    public function bulkCompleteDeliveries(
        Request $request
    ): Response {
        // Vérifier le token CSRF
        $token = $request->request->get('_token');
        if (!$this->isCsrfTokenValid('bulk-complete-deliveries', $token)) {
            $this->addFlash('error', 'Token de sécurité invalide');
            return $this->redirectToRoute('app_deliveries_index');
        }

        // Récupérer les IDs
        $deliveryIdsString = $request->request->get('delivery_ids', '');

        if (empty($deliveryIdsString)) {
            $this->addFlash('warning', 'Aucune livraison sélectionnée');
            return $this->redirectToRoute('app_deliveries_index');
        }

        // Convertir en tableau d'entiers
        $deliveryIds = array_map('intval', explode(',', $deliveryIdsString));

        try {
            $result = $this->livraisonService->bulkComplete($deliveryIds);

            if ($result['success'] > 0) {
                $this->addFlash('success', sprintf(
                    '%d livraison(s) terminée(s) avec succès',
                    $result['success']
                ));
            }

            if ($result['skipped'] > 0) {
                $this->addFlash('warning', sprintf(
                    '%d livraison(s) ignorée(s) (déjà terminées)',
                    $result['skipped']
                ));
            }

            foreach ($result['errors'] as $error) {
                $this->addFlash('error', $error);
            }

        } catch (\Exception $e) {
            $this->addFlash('error', 'Une erreur est survenue :  ' . $e->getMessage());
        }

        return $this->redirectToRoute('app_deliveries_index');
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
