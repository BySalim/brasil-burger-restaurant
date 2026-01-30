<?php

namespace App\Controller;

use App\DTO\DailyStatsDTO;
use App\Factory\DashboardViewFactory;
use App\Repository\ArticleRepository;
use App\Repository\CommandeRepository;
use App\Service\RevenueCalcService;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\DependencyInjection\ParameterBag\ParameterBagInterface;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Attribute\Route;

class HomeController extends AbstractController
{
    public function __construct(
        private readonly CommandeRepository $commandeRepository,
        private readonly ArticleRepository  $articleRepository,
        private readonly ParameterBagInterface $params,
    )
    {
    }


    #[Route(['/', '/dashboard'], name: 'app_home')]
    public function index(Request $request, DashboardViewFactory $dashboardView, RevenueCalcService $revunueCalc): Response
    {
        $dateString = $request->query->get('date', (new \DateTime())->format('Y-m-d'));

        try {
            // Transformation en Objet
            $selectedDate = new \DateTime($dateString);
        } catch (\Exception $e) {
            // Sécurité : Si le format est invalide, on force la date du jour
            $selectedDate = new \DateTime();
        }

        $per_page = $this->params->get('app.pagination.default_per_page') - 1;
        $limit = $request->query->getInt('limit', $per_page);
        $limit = $limit < $per_page ? $per_page : $limit;

        $dailyStats = $this->commandeRepository->getDailyStatsByDate($selectedDate);

        $stats = $dashboardView->createStatsView(
            DailyStatsDTO::indexByEtat($dailyStats),
            $revunueCalc->calculateTotalRevenue($dailyStats)
        );

        $articlesVendu = $this->articleRepository->findTopArticlesByDate($selectedDate, $limit);
        $totalProducts = $this->articleRepository->countDistinctArticlesSold($selectedDate);
        $products = $dashboardView->createTopProductsView($articlesVendu);

        // Calcul la suite
        $nextLimit = $limit + $per_page;

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
