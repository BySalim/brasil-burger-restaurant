<?php

namespace App\Controller;

use App\Entity\Commande;
use App\Enum\EtatCommande;
use App\Factory\OrdersViewFactory;
use App\Form\OrderFilterType;
use App\Repository\CommandeRepository;
use App\Service\CommandeService;
use Knp\Component\Pager\PaginatorInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\DependencyInjection\ParameterBag\ParameterBagInterface;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Attribute\Route;

#[Route('/orders')]
class OrderController extends AbstractController
{
    public function __construct(
        private readonly CommandeRepository    $commandeRepository,
        private readonly CommandeService       $commandeService,
        private readonly ParameterBagInterface $params,
        private readonly OrdersViewFactory     $ordersViewFactory
    )
    {
    }

    /**
     * Page liste des commandes avec filtres et pagination
     */
    #[Route('/', name: 'app_orders')]
    public function index(Request $request, PaginatorInterface $paginator): Response
    {

        $form = $this->createForm(OrderFilterType::class);
        $form->handleRequest($request);

        $filters = $form->getData();

        $queryBuilder = $this->commandeRepository->findAllWithFiltersQB(
            search: $filters['search'] ?? null,
            date: $filters['date'] ?? null,
            type: $filters['type'] ?? null,
            status: $filters['status'] ?? null
        );

        $per_page = $filters['per_page'] ?? $this->params->get('app.pagination.default_per_page');
        $kPagination = $paginator->paginate(
            $queryBuilder,
            $request->query->getInt('page', 1),
            $per_page
        );

        $paginationData = [
            'totalItems' => $kPagination->getTotalItemCount(),
            'currentPage' => $kPagination->getCurrentPageNumber(),
            'itemsPerPage' => $per_page,
        ];

        $cmd_items = $kPagination->getItems();

        $orders = $this->ordersViewFactory->createOrderRows($cmd_items);

        return $this->render('orders/index.html.twig', [
            'form' => $form,
            'orders' => $orders,
            'paginationData' => $paginationData,
        ]);
    }

    /**
     * Changer le statut d'une commande
     */
    #[Route('/change-status/{id}', name: 'app_order_change_status', methods: ['POST'])]
    public function changeStatus(Commande $commande, Request $request): Response
    {
        // Validation CSRF
        $token = $request->request->get('_token');
        if (!$this->isCsrfTokenValid('change-status-' . $commande->getId(), $token)) {
            $this->addFlash('error', 'Token de sécurité invalide');
            return $this->redirectToRoute('app_orders');
        }

        $newEtatValue = $request->request->get('etat');

        try {
            $newEtat = EtatCommande::from($newEtatValue);
            $this->commandeService->changeStatus($commande, $newEtat);

            $this->addFlash('success', sprintf(
                'Commande %s passée en "%s"',
                $commande->getNumCmd(),
                $newEtat->getLabel()
            ));
        } catch (\ValueError $e) {
            $this->addFlash('error', 'État invalide');
        } catch (\LogicException $e) {
            $this->addFlash('error', $e->getMessage());
        }

        return $this->redirectToRoute('app_orders');
    }

    /**
     * Page détail d'une commande
     */
    #[Route('/{id}', name: 'app_orders_show_order', requirements: ['id' => '\d+'])]
    public function show(int $id, Request $request): Response
    {
        $per_page = $this->params->get('app.pagination.default_per_page');
        $limit = $request->query->getInt('limit', $per_page);
        $limit = $limit < $per_page ? $per_page : $limit;

        $commande = $this->commandeRepository->findById($id);

        if (!$commande) {
            throw $this->createNotFoundException('Commande introuvable');
        }

        $orderHeader = $this->ordersViewFactory->createOrderHeader($commande);
        $productsSales = $this->ordersViewFactory->createProductsSales($commande);
        $clientCard = $this->ordersViewFactory->createCardClient($commande->getClient());
        $deliveryCard = $this->ordersViewFactory->createDeliveryCard($commande);
        $paymentCard = $this->ordersViewFactory->createPaymentCard($commande);
        $statusActions = $commande->getEtat()->getEditableStatusMap();

        $nextLimit = $limit + $per_page;

        return $this->render('orders/show.html.twig', [
            'orderHeader' => $orderHeader,
            'productsSales' => $productsSales,
            'clientCard' => $clientCard,
            'deliveryCard' => $deliveryCard,
            'paymentCard' => $paymentCard,
            'statusActions' => $statusActions,
            'limit' => $limit,
            'nextLimit' => $nextLimit,
        ]);
    }


}
