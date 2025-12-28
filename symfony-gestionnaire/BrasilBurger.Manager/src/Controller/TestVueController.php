<?php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Attribute\Route;

final class TestVueController extends AbstractController
{
    #[Route('/test/login', name: 'app_login')]
    public function login(): Response
    {
        return $this->render('security/login.html.twig', [
            "error" => false,
            "last_username" => null
        ]);
    }

    #[Route('/test/dashboard', name: 'app_dashboard')]
    public function dashboard(): Response
    {
        return $this->render('dashboard/index.html.twig', [
            "error" => false,
            "last_username" => null
        ]);
    }

    #[Route('/test/orders', name: 'app_orders')]
    public function commandes(): Response
    {
        return $this->render('orders/index.html.twig', [
            "error" => false,
            "last_username" => null
        ]);
    }

    #[Route('/test/deliveries', name: 'app_deliveries')]
    public function livraisons(): Response
    {
        return $this->render('dashboard/index.html.twig', [
            "error" => false,
            "last_username" => null
        ]);
    }

}
