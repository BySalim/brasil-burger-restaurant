<?php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Attribute\Route;

final class TestVueController extends AbstractController
{
    #[Route('/test/vue', name: 'app_login')]
    public function index(): Response
    {
        return $this->render('security/login.html.twig', [
            "error" => false,
            "last_username" => null
        ]);
    }
    
}
