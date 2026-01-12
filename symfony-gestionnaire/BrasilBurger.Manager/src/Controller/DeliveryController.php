<?php

namespace App\Controller;

use App\Entity\Livraison;
use App\Enum\StatutLivraison;
use App\Factory\DeliveriesViewFactory;
use App\Factory\OrdersViewFactory;
use App\Form\DeliveryFilterType;
use App\Form\OrderFilterType;
use App\Repository\LivraisonRepository;
use App\Repository\LivreurRepository;
use App\Service\LivraisonService;
use App\ViewModel\DeliveryInfos;
use App\ViewModel\PaginationViewModel;
use Knp\Component\Pager\PaginatorInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\DependencyInjection\ParameterBag\ParameterBagInterface;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Attribute\Route;

#[Route('/deliveries', name: 'app_deliveries_')]
class DeliveryController extends AbstractController
{
    public function __construct(
        private readonly LivraisonRepository $livraisonRepository,
        private readonly LivreurRepository $livreurRepository,
        private readonly DeliveriesViewFactory $deliveriesViewFactory,
        private readonly OrdersViewFactory $ordersViewFactory,
        private readonly LivraisonService $livraisonService,
        private readonly ParameterBagInterface $params,
    )
    {
    }

    /**
     * Page liste des livraisons avec filtres optionnels par livreur
     */
    #[Route('', name: 'index', methods: ['GET'])]
    #[Route('/livreur/{livreur_id}', name: 'by_livreur', requirements: ['livreur_id' => '\d+'], methods: ['GET'])]
    public function index( Request $request, PaginatorInterface $paginator, ? int $livreur_id = null): Response
    {

        $livreur = null;
        if ($livreur_id) {
            $livreur = $this->livreurRepository->findById($livreur_id);

            if (!$livreur) {
                throw $this->createNotFoundException('Livreur introuvable');
            }
        }

        $form = $this->createForm(DeliveryFilterType::class);
        $form->handleRequest($request);

        $filters = $form->getData();

        $queryBuilder = $this->livraisonRepository->findAllWithFiltersQB(
            search: $filters['search'] ?? null,
            date: $filters['date'] ?? null,
            zoneId: $filters['zone'] ?? null,
            status: $filters['status'] ?? null,
            livreur: $livreur
        );

        $perPage = $filters['per_page'] ?? $this->params->get('app.pagination.default_per_page');
        $kPagination = $paginator->paginate(
            $queryBuilder,
            $request->query->getInt('page', 1),
            $perPage
        );

        $paginationData = new PaginationViewModel(
            totalItems: $kPagination->getTotalItemCount(),
            itemsPerPage: $perPage,
            currentPage: $kPagination->getCurrentPageNumber(),
        );

        $livraisons_items = $kPagination->getItems();

        $livraisons = $this->deliveriesViewFactory->createDeleveries($livraisons_items);

        $statusTerminer = $this->deliveriesViewFactory->createStatusBadge(StatutLivraison::TERMINER);

        return $this->render('deliveries/index.html.twig', [
            'form' => $form,
            'livraisons' => $livraisons,
            'statusTerminer' => $statusTerminer,
            'paginationData' => $paginationData
        ]);
    }

    /**
     * Terminer PLUSIEURS livraisons (bulk action)
     */
    #[Route('/bulk/complete', name: 'bulk_complete', methods: ['POST'])]
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


    /**
     * Terminer UNE livraison individuelle
     */
    #[Route('/{id}/complete', name: 'complete', methods: ['POST'])]
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
                'Livraison %d terminée avec succès',
                $livraison->getId()
            ));

        } catch (\LogicException $e) {
            $this->addFlash('error', $e->getMessage());
        } catch (\Exception $e) {
            $this->addFlash('error', 'Une erreur est survenue lors de la finalisation');
        }


        return $this->redirectToRoute('app_deliveries_index');
    }


    #[Route('/{id}', name: 'show', requirements: ['id' => '\d+'])]
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


    #[Route('/orders/', name: 'orders')]
    public function listOrdersToDelivered(): Response
    {
        return $this->render('deliveries/orders.html.twig', []);
    }



}
