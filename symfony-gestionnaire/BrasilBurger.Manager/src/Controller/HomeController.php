<?php

namespace App\Controller;

use App\DTO\DailyStatsDTO;
use App\Factory\DashboardViewFactory;
use App\Repository\ArticleRepository;
use App\Repository\CommandeRepository;
use App\Service\RevenueCalcService;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Attribute\Route;

class HomeController extends AbstractController
{
    private const ITEMS_TOP_PRODUCTS_DEFAULT = 3;
    private const ADD_ITEMS_TOP_PRODUCTS_DEFAULT = 3;

    public function __construct(private readonly CommandeRepository $commandeRepository, private readonly ArticleRepository $articleRepository)
    {
    }



    #[Route(['/', '/dashboard'], name: 'app_home')]
    public function index(Request $request, DashboardViewFactory $dashboardView, RevenueCalcService $revunueCalc): Response
    {
        $dateString = $request->query->get('date', new \DateTime()->format('Y-m-d'));

        try {
            // Transformation en Objet
            $selectedDate = new \DateTime($dateString);
        } catch (\Exception $e) {
            // Sécurité : Si le format est invalide, on force la date du jour
            $selectedDate = new \DateTime();
        }

        $limit = $request->query->getInt('limit', self::ITEMS_TOP_PRODUCTS_DEFAULT);

        $dailyStats = $this->commandeRepository->getDailyStatsByDate($selectedDate);

        $stats = $dashboardView->createStatsView(
                DailyStatsDTO::indexByEtat($dailyStats),
                $revunueCalc->calculateTotalRevenue($dailyStats)
            );

        $articlesVendu = $this->articleRepository->findTopArticlesByDate($selectedDate, $limit);
        $totalProducts = $this->articleRepository->countDistinctArticlesSold($selectedDate);
        $products = $dashboardView->createTopProductsView($articlesVendu);

        // 3. Calcul pour le bouton "Afficher plus"
        $nextLimit = $limit + self::ADD_ITEMS_TOP_PRODUCTS_DEFAULT;

        return $this->render('dashboard/index.html.twig', [
            'dailyStats' => $stats,
            'topProducts' => $products,
            'totalProducts' => $totalProducts,
            'selectedDate' => $dateString,
            'currentLimit' => $limit,
            'nextLimit' => $nextLimit,
        ]);
    }


}
