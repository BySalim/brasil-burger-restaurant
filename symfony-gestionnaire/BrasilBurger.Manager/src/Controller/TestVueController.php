<?php

namespace App\Controller;

use App\Enum\CategoriePanier;
use App\Enum\Color;
use App\Enum\EtatCommande;
use App\Form\OrderFilterType;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Attribute\Route;

final class TestVueController extends AbstractController
{
    # Route défaut en attente de développement
    #[Route('/test', name: 'app_default')]
    public function dafault(): Response
    {
        return $this->render('test_vue/index.html.twig', []);
    }

    #[Route('/test/dashboard', name: 'app_dashboard')]
    public function dashboard(): Response
    {
        return $this->render('dashboard/index.html.twig', []);
    }

    private const ETATS_ACTIONS = [

    ];
    #[Route('/test/orders', name: 'dddd')]
    public function commandes(Request $request): Response
    {
        $form = $this->createForm(OrderFilterType::class);
        $form->handleRequest($request);

        // 2. ✅ Récupérer les données (déjà typées correctement)
        $filters = $form->getData();

        $orders = $this->giveOrders();

        return $this->render('orders/index.html.twig', [
            'form' => $form,
            'filters' => $filters,
            'orders' => $orders
        ]);
    }

    private function giveOrders(): array
    {
        $menu = CategoriePanier::MENU;
        $burger = CategoriePanier:: BURGER;
        $colorTypeMenu = $menu->getColor()->getBadgeClasses();
        $colorTypeBurger = $burger->getColor()->getBadgeClasses();

        $statusEnAttente = EtatCommande::EN_ATTENTE;
        $statusEnPreparation = EtatCommande::EN_PREPARATION;
        $statusAnnuler = EtatCommande:: ANNULER;
        $statusTerminer = EtatCommande:: TERMINER;

        return [
            [
                'id' => 1,
                'date' => '24 Oct 2024',
                'time' => '14:30',
                'code' => '#CMD-8924',
                'type' => $menu->getLabel(),
                'typeColor' => $colorTypeMenu,
                'amount' => '4 500',
                'clientName' => 'Amadou Diallo',
                'clientPhone' => '77 123 45 67',
                'status' => $statusEnPreparation->getLabel(),
                'statusColor' => $statusEnPreparation->getColor()->getBadgeClasses(),
                'edit_status' => $statusEnPreparation->getEditableStatusMap(),
            ],
            [
                'id' => 2,
                'date' => '24 Oct 2024',
                'time' => '14:45',
                'code' => '#CMD-8925',
                'type' => $burger->getLabel(),
                'typeColor' => $colorTypeBurger,
                'amount' => '3 500',
                'clientName' => 'Fatou Diop',
                'clientPhone' => '76 890 12 34',
                'status' => $statusEnAttente->getLabel(),
                'statusColor' => $statusEnAttente->getColor()->getBadgeClasses(),
                'edit_status' => $statusEnAttente->getEditableStatusMap(),
            ],
            [
                'id' => 3,
                'date' => '24 Oct 2024',
                'time' => '13:15',
                'code' => '#CMD-8920',
                'type' => $menu->getLabel(),
                'typeColor' => $colorTypeMenu,
                'amount' => '9 000',
                'clientName' => 'Oumar Sow',
                'clientPhone' => '70 654 98 70',
                'status' => $statusTerminer->getLabel(),
                'statusColor' => $statusTerminer->getColor()->getBadgeClasses(),
                'edit_status' => $statusTerminer->getEditableStatusMap(),
            ],
            [
                'id' => 4,
                'date' => '24 Oct 2024',
                'time' => '11:50',
                'code' => '#CMD-8912',
                'type' => $burger->getLabel(),
                'typeColor' => $colorTypeBurger,
                'amount' => '3 000',
                'clientName' => 'Amina Fall',
                'clientPhone' => '77 333 22 11',
                'status' => $statusAnnuler->getLabel(),
                'statusColor' => $statusAnnuler->getColor()->getBadgeClasses(),
                'edit_status' => $statusAnnuler->getEditableStatusMap(),
            ],
            [
                'id' => 5,
                'date' => '24 Oct 2024',
                'time' => '15:10',
                'code' => '#CMD-8926',
                'type' => $menu->getLabel(),
                'typeColor' => $colorTypeMenu,
                'amount' => '6 000',
                'clientName' => 'Ndèye Gueye',
                'clientPhone' => '78 555 44 33',
                'status' => $statusTerminer->getLabel(),
                'statusColor' => $statusTerminer->getColor()->getBadgeClasses(),
                'edit_status' => $statusTerminer->getEditableStatusMap(),
            ],
            [
                'id' => 6,
                'date' => '24 Oct 2024',
                'time' => '15:12',
                'code' => '#CMD-8927',
                'type' => $burger->getLabel(),
                'typeColor' => $colorTypeBurger,
                'amount' => '3 500',
                'clientName' => 'Cheikh Ba',
                'clientPhone' => '77 999 88 77',
                'status' => $statusEnAttente->getLabel(),
                'statusColor' => $statusEnAttente->getColor()->getBadgeClasses(),
                'edit_status' => $statusEnAttente->getEditableStatusMap(),
            ],
            [
                'id' => 7,
                'date' => '23 Oct 2024',
                'time' => '19:05',
                'code' => '#CMD-8901',
                'type' => $menu->getLabel(),
                'typeColor' => $colorTypeMenu,
                'amount' => '5 000',
                'clientName' => 'Mariama Camara',
                'clientPhone' => '76 111 22 33',
                'status' => $statusAnnuler->getLabel(),
                'statusColor' => $statusAnnuler->getColor()->getBadgeClasses(),
                'edit_status' => $statusAnnuler->getEditableStatusMap(),
            ],
            [
                'id' => 8,
                'date' => '23 Oct 2024',
                'time' => '20:30',
                'code' => '#CMD-8905',
                'type' => $menu->getLabel(),
                'typeColor' => $colorTypeMenu,
                'amount' => '4 500',
                'clientName' => 'Ibrahim Ndiaye',
                'clientPhone' => '70 777 66 55',
                'status' => $statusEnPreparation->getLabel(),
                'statusColor' => $statusEnPreparation->getColor()->getBadgeClasses(),
                'edit_status' => $statusEnPreparation->getEditableStatusMap(),
            ]
        ];
    }

    #[Route('/test/orders/code', name: 'app_details_order')]
    public function showCommande(): Response
    {
        return $this->render('orders/show.html.twig', []);
    }

    #[Route('/test/deliveries', name: 'app_orders_to_delivered')]
    public function showOrdersToDelivered(): Response
    {
        return $this->render('deliveries/orders.html.twig', []);
    }

    #[Route('/test/delivery_info', name: 'app_delivery_info')]
    public function showDelivredInfos(): Response
    {
        return $this->render('delivery_info/show.html.twig', []);
    }

    #[Route('/test/deliveries/list', name: 'app_deliveries')]
    public function deliveries(): Response
    {
        return $this->render('deliveries/list.html.twig', []);
    }



}
