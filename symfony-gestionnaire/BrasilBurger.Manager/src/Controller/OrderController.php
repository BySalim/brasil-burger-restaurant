<?php

namespace App\Controller;

use App\Entity\Commande;
use App\Enum\EtatCommande;
use App\Form\OrderFilterType;
use App\Repository\CommandeRepository;
use App\Service\CommandeService;
use Knp\Component\Pager\PaginatorInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Attribute\Route;

#[Route('/orders')]
class OrderController extends AbstractController
{
    public function __construct(
        private readonly CommandeRepository $commandeRepository,
        private readonly CommandeService    $commandeService
    )
    {
    }

    /**
     * Page liste des commandes avec filtres et pagination
     */
    #[Route('/orders/', name: 'app_orders')]
    public function index(Request $request, PaginatorInterface $paginator): Response
    {
        // 1. ✅ Créer et traiter le formulaire
        $form = $this->createForm(OrderFilterType::class);
        $form->handleRequest($request);

        // 2. ✅ Récupérer les données (déjà typées correctement)
        $filters = $form->getData();

        // $filters = [
        //     'search' => 'CMD-123',
        //     'date' => \DateTime object ou null,
        //     'type' => CategoriePanier object ou null,
        //     'status' => EtatCommande object ou null,
        //     'per_page' => 8
        // ]

        // Construire la requête avec filtres
//        $queryBuilder = $this->commandeRepository->findAllWithFiltersQB(
//            search: $filters['search'] ?? null,
//            date: $filters['date'] ?? null,
//            type: $filters['type'] ?? null,
//            status: $filters['status'] ?? null
//        );

        // Pagination
        $pagination = $paginator->paginate(
            $queryBuilder,
            $request->query->getInt('page', 1),
            $filters['per_page'] ?? 8
        );

        // Transformation des entités en tableaux pour la vue
        $orders = array_map(function (Commande $commande) {
            $client = $commande->getClient();
            $panier = $commande->getPanier();

            return [
                'id' => $commande->getId(),
                'date' => $commande->getDateDebut()->format('d M Y'),
                'time' => $commande->getDateDebut()->format('H:i'),
                'code' => $commande->getNumCmd(),
                'type' => $panier?->getCategoriePanier()?->getLabel() ?? '--',
                'typeColor' => $panier?->getCategoriePanier()?->getColor() ?? 'gray',
                'amount' => number_format($commande->getMontant(), 0, '', ' '),
                'clientName' => $client->getNom() . ' ' . $client->getPrenom(),
                'clientPhone' => $client->getTelephone() ?? '--',
                'status' => strtolower($commande->getEtat()->value),
                'statusLabel' => $commande->getEtat()->value,
                'statusColor' => $commande->getEtat()->getColor(),
            ];
        }, iterator_to_array($pagination->getItems()));

        return $this->render('orders/index.html.twig', [
            'form' => $form,
            'orders' => $orders,
            'pagination' => $pagination,
        ]);
    }



    /**
     * Page détail d'une commande
     */
    #[Route('/{id}', name: 'app_order_show', requirements: ['id' => '\d+'])]
    public function show(int $id): Response
    {
        $commande = $this->commandeRepository->findOneWithFullDetails($id);

        if (!$commande) {
            throw $this->createNotFoundException('Commande introuvable');
        }

        $allowedStatuses = $this->commandeService->getAllowedNextStatuses($commande);

        return $this->render('orders/show.html.twig', [
            'commande' => $commande,
            'allowedStatuses' => $allowedStatuses,
        ]);
    }

    /**
     * Changer le statut d'une commande (POST)
     */
    #[Route('/{id}/change-status', name: 'app_order_change_status', methods: ['POST'])]
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
            $newEtat = EtatCommande:: from($newEtatValue);
//            $this->commandeService->changeStatus($commande, $newEtat);

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
     * Action :  Annuler une commande
     */
    #[Route('/{id}/cancel', name: 'app_order_cancel', methods: ['POST'])]
    public function cancel(Commande $commande, Request $request): Response
    {
        return $this->redirectToRoute('app_order_show', ['id' => $commande->getId()]);
    }
}
