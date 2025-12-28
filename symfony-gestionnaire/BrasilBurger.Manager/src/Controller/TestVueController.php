<?php

namespace App\Controller;

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

    #[Route('/test/login', name: 'app_login')]
    public function login(): Response
    {
        return $this->render('security/login.html.twig', []);
    }

    #[Route('/test/dashboard', name: 'app_dashboard')]
    public function dashboard(): Response
    {
        return $this->render('dashboard/index.html.twig', []);
    }

    #[Route('/test/orders', name: 'app_orders')]
    public function commandes(): Response
    {
        return $this->render('orders/index.html.twig', []);
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
