<?php

namespace App\Controller;

use App\DTO\DailyStatsDTO;
use App\Enum\CategoriePanier;
use App\Enum\EtatCommande;
use App\Factory\DashboardViewFactory;
use App\Repository\ArticleRepository;
use App\Repository\CommandeRepository;
use App\Service\RevenueCalcService;
use App\ViewModel\TopProductViewModel;
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


    /**
     * Simule la récupération des produits
     * @return array{
     *     items: TopProductViewModel[],
     *     total: int
     * }
     */
    private function getTopProducts(string $date, int $limit): array
    {
        // Simulation d'une liste complète en base de données
        $allProducts = [
            new TopProductViewModel('Brasil Spécial', CategoriePanier::MENU->getLabel(), CategoriePanier::MENU->getColor(), 4500, 42, 'https://lh3.googleusercontent.com/aida-public/AB6AXuC6kIvrk5WKi10YXEJMnHXQDtkuGml98HGvR3pjKvD_dPnStKOMaDh08MjEg1oIMn4QWr5Oos13k5KkM4vZ1ruBaQF4mOpxD5zvkSYjaAFOsW4J8E-ZcIW9vLD-DZgtktdWDeWnnz--62zkbX0SH3KPEa7hf9d4Rw97bk__VJMrPGg8k5xXfuAt7rig87fhpXY0usZKYgMpUbgXOIHECe6jbl23_GOMzmvsXtnWFlddrvwOIHzEeVNaPGGzh38g3by3MH81vgh6lx4'),
            new TopProductViewModel('Cheese & Bacon', CategoriePanier::MENU->getLabel(), CategoriePanier::MENU->getColor(), 3500, 28, 'https://lh3.googleusercontent.com/aida-public/AB6AXuC6kIvrk5WKi10YXEJMnHXQDtkuGml98HGvR3pjKvD_dPnStKOMaDh08MjEg1oIMn4QWr5Oos13k5KkM4vZ1ruBaQF4mOpxD5zvkSYjaAFOsW4J8E-ZcIW9vLD-DZgtktdWDeWnnz--62zkbX0SH3KPEa7hf9d4Rw97bk__VJMrPGg8k5xXfuAt7rig87fhpXY0usZKYgMpUbgXOIHECe6jbl23_GOMzmvsXtnWFlddrvwOIHzEeVNaPGGzh38g3by3MH81vgh6lx4'),
            new TopProductViewModel('Double Whopper', CategoriePanier::MENU->getLabel(), CategoriePanier::MENU->getColor(), 5000, 15, 'https://lh3.googleusercontent.com/aida-public/AB6AXuC6kIvrk5WKi10YXEJMnHXQDtkuGml98HGvR3pjKvD_dPnStKOMaDh08MjEg1oIMn4QWr5Oos13k5KkM4vZ1ruBaQF4mOpxD5zvkSYjaAFOsW4J8E-ZcIW9vLD-DZgtktdWDeWnnz--62zkbX0SH3KPEa7hf9d4Rw97bk__VJMrPGg8k5xXfuAt7rig87fhpXY0usZKYgMpUbgXOIHECe6jbl23_GOMzmvsXtnWFlddrvwOIHzEeVNaPGGzh38g3by3MH81vgh6lx4'),
            new TopProductViewModel('Frites Maison', CategoriePanier::MENU->getLabel(), CategoriePanier::MENU->getColor(), 1500, 50, 'https://lh3.googleusercontent.com/aida-public/AB6AXuC6kIvrk5WKi10YXEJMnHXQDtkuGml98HGvR3pjKvD_dPnStKOMaDh08MjEg1oIMn4QWr5Oos13k5KkM4vZ1ruBaQF4mOpxD5zvkSYjaAFOsW4J8E-ZcIW9vLD-DZgtktdWDeWnnz--62zkbX0SH3KPEa7hf9d4Rw97bk__VJMrPGg8k5xXfuAt7rig87fhpXY0usZKYgMpUbgXOIHECe6jbl23_GOMzmvsXtnWFlddrvwOIHzEeVNaPGGzh38g3by3MH81vgh6lx4'),
            new TopProductViewModel('Coca Cola', CategoriePanier::MENU->getLabel(), CategoriePanier::MENU->getColor(), 1000, 60, 'https://lh3.googleusercontent.com/aida-public/AB6AXuC6kIvrk5WKi10YXEJMnHXQDtkuGml98HGvR3pjKvD_dPnStKOMaDh08MjEg1oIMn4QWr5Oos13k5KkM4vZ1ruBaQF4mOpxD5zvkSYjaAFOsW4J8E-ZcIW9vLD-DZgtktdWDeWnnz--62zkbX0SH3KPEa7hf9d4Rw97bk__VJMrPGg8k5xXfuAt7rig87fhpXY0usZKYgMpUbgXOIHECe6jbl23_GOMzmvsXtnWFlddrvwOIHzEeVNaPGGzh38g3by3MH81vgh6lx4'),
            new TopProductViewModel('Wrap Poulet', CategoriePanier::MENU->getLabel(), CategoriePanier::MENU->getColor(), 3000, 10, 'https://lh3.googleusercontent.com/aida-public/AB6AXuC6kIvrk5WKi10YXEJMnHXQDtkuGml98HGvR3pjKvD_dPnStKOMaDh08MjEg1oIMn4QWr5Oos13k5KkM4vZ1ruBaQF4mOpxD5zvkSYjaAFOsW4J8E-ZcIW9vLD-DZgtktdWDeWnnz--62zkbX0SH3KPEa7hf9d4Rw97bk__VJMrPGg8k5xXfuAt7rig87fhpXY0usZKYgMpUbgXOIHECe6jbl23_GOMzmvsXtnWFlddrvwOIHzEeVNaPGGzh38g3by3MH81vgh6lx4'),
            new TopProductViewModel('Glace Vanille', CategoriePanier::MENU->getLabel(), CategoriePanier::MENU->getColor(), 2000, 12, 'https://lh3.googleusercontent.com/aida-public/AB6AXuC6kIvrk5WKi10YXEJMnHXQDtkuGml98HGvR3pjKvD_dPnStKOMaDh08MjEg1oIMn4QWr5Oos13k5KkM4vZ1ruBaQF4mOpxD5zvkSYjaAFOsW4J8E-ZcIW9vLD-DZgtktdWDeWnnz--62zkbX0SH3KPEa7hf9d4Rw97bk__VJMrPGg8k5xXfuAt7rig87fhpXY0usZKYgMpUbgXOIHECe6jbl23_GOMzmvsXtnWFlddrvwOIHzEeVNaPGGzh38g3by3MH81vgh6lx4'),
        ];

        $total = count($allProducts);

        $items = $limit >= $total
            ? $allProducts
            : array_slice($allProducts, 0, $limit);

        return [
            'items' => $items,
            'total' => $total,
        ];
    }

}
