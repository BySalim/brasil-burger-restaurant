<?php

namespace App\Controller;

use App\DTO\DashboardStatDTO;
use App\DTO\TopProductDTO;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Attribute\Route;

class HomeController extends AbstractController
{
    private const ITEMS_TOP_PRODUCTS_DEFAULT = 3;
    private const ADD_ITEMS_TOP_PRODUCTS_DEFAULT = 3;

    #[Route('/dashboard', name: 'app_home')]
    public function index(Request $request): Response
    {
        $selectedDate = $request->query->get('date', new \DateTime()->format('Y-m-d'));

        // Si pas de limite, on affiche 5 produits par défaut.
        $limit = $request->query->getInt('limit', self::ITEMS_TOP_PRODUCTS_DEFAULT);

        // 2. Récupération des données (Ici je simule, plus tard on appellera un Service/Repository)
        $stats = $this->getDailyStats($selectedDate);
        ['items' => $products, 'total' => $totalProducts] = $this->getTopProducts($selectedDate, $limit);

        // 3. Calcul pour le bouton "Afficher plus"
        $nextLimit = $limit + self::ITEMS_TOP_PRODUCTS_DEFAULT;

        return $this->render('dashboard/index.html.twig', [
            'dailyStats' => $stats,
            'topProducts' => $products,
            'totalProducts' => $totalProducts,
            'selectedDate' => $selectedDate,
            'currentLimit' => $limit,
            'nextLimit' => $nextLimit,
        ]);
    }

    /**
     * Simule la récupération des stats depuis la BDD
     * @param string $date
     * @return DashboardStatDTO
     */
    private function getDailyStats(string $date): DashboardStatDTO
    {
        // TODO: Remplacer par $repository->countOrdersByDate($date)...
        return new DashboardStatDTO(10, 12, 15, 5, 450000000);
    }

    /**
     * Simule la récupération des produits
     * @return array{
     *     items: TopProductDTO[],
     *     total: int
     * }
     */
    private function getTopProducts(string $date, int $limit): array
    {
        // Simulation d'une liste complète en base de données
        $allProducts = [
            new TopProductDTO('Brasil Spécial', 'MENU', 'blue', 4500, 42, 'https://lh3.googleusercontent.com/aida-public/AB6AXuC6kIvrk5WKi10YXEJMnHXQDtkuGml98HGvR3pjKvD_dPnStKOMaDh08MjEg1oIMn4QWr5Oos13k5KkM4vZ1ruBaQF4mOpxD5zvkSYjaAFOsW4J8E-ZcIW9vLD-DZgtktdWDeWnnz--62zkbX0SH3KPEa7hf9d4Rw97bk__VJMrPGg8k5xXfuAt7rig87fhpXY0usZKYgMpUbgXOIHECe6jbl23_GOMzmvsXtnWFlddrvwOIHzEeVNaPGGzh38g3by3MH81vgh6lx4'),
            new TopProductDTO('Cheese & Bacon', 'BURGER', 'yellow', 3500, 28, 'https://lh3.googleusercontent.com/aida-public/AB6AXuC6kIvrk5WKi10YXEJMnHXQDtkuGml98HGvR3pjKvD_dPnStKOMaDh08MjEg1oIMn4QWr5Oos13k5KkM4vZ1ruBaQF4mOpxD5zvkSYjaAFOsW4J8E-ZcIW9vLD-DZgtktdWDeWnnz--62zkbX0SH3KPEa7hf9d4Rw97bk__VJMrPGg8k5xXfuAt7rig87fhpXY0usZKYgMpUbgXOIHECe6jbl23_GOMzmvsXtnWFlddrvwOIHzEeVNaPGGzh38g3by3MH81vgh6lx4'),
            new TopProductDTO('Double Whopper', 'BURGER', 'yellow', 5000, 15, 'https://lh3.googleusercontent.com/aida-public/AB6AXuC6kIvrk5WKi10YXEJMnHXQDtkuGml98HGvR3pjKvD_dPnStKOMaDh08MjEg1oIMn4QWr5Oos13k5KkM4vZ1ruBaQF4mOpxD5zvkSYjaAFOsW4J8E-ZcIW9vLD-DZgtktdWDeWnnz--62zkbX0SH3KPEa7hf9d4Rw97bk__VJMrPGg8k5xXfuAt7rig87fhpXY0usZKYgMpUbgXOIHECe6jbl23_GOMzmvsXtnWFlddrvwOIHzEeVNaPGGzh38g3by3MH81vgh6lx4'),
            new TopProductDTO('Frites Maison', 'MENU.', 'blue', 1500, 50, 'https://lh3.googleusercontent.com/aida-public/AB6AXuC6kIvrk5WKi10YXEJMnHXQDtkuGml98HGvR3pjKvD_dPnStKOMaDh08MjEg1oIMn4QWr5Oos13k5KkM4vZ1ruBaQF4mOpxD5zvkSYjaAFOsW4J8E-ZcIW9vLD-DZgtktdWDeWnnz--62zkbX0SH3KPEa7hf9d4Rw97bk__VJMrPGg8k5xXfuAt7rig87fhpXY0usZKYgMpUbgXOIHECe6jbl23_GOMzmvsXtnWFlddrvwOIHzEeVNaPGGzh38g3by3MH81vgh6lx4'),
            new TopProductDTO('Coca Cola', 'MENU', 'blue', 1000, 60, 'https://lh3.googleusercontent.com/aida-public/AB6AXuC6kIvrk5WKi10YXEJMnHXQDtkuGml98HGvR3pjKvD_dPnStKOMaDh08MjEg1oIMn4QWr5Oos13k5KkM4vZ1ruBaQF4mOpxD5zvkSYjaAFOsW4J8E-ZcIW9vLD-DZgtktdWDeWnnz--62zkbX0SH3KPEa7hf9d4Rw97bk__VJMrPGg8k5xXfuAt7rig87fhpXY0usZKYgMpUbgXOIHECe6jbl23_GOMzmvsXtnWFlddrvwOIHzEeVNaPGGzh38g3by3MH81vgh6lx4'),
            new TopProductDTO('Wrap Poulet', 'MENU', 'blue', 3000, 10, 'https://lh3.googleusercontent.com/aida-public/AB6AXuC6kIvrk5WKi10YXEJMnHXQDtkuGml98HGvR3pjKvD_dPnStKOMaDh08MjEg1oIMn4QWr5Oos13k5KkM4vZ1ruBaQF4mOpxD5zvkSYjaAFOsW4J8E-ZcIW9vLD-DZgtktdWDeWnnz--62zkbX0SH3KPEa7hf9d4Rw97bk__VJMrPGg8k5xXfuAt7rig87fhpXY0usZKYgMpUbgXOIHECe6jbl23_GOMzmvsXtnWFlddrvwOIHzEeVNaPGGzh38g3by3MH81vgh6lx4'),
            new TopProductDTO('Glace Vanille', 'MENU', 'blue', 2000, 12, 'https://lh3.googleusercontent.com/aida-public/AB6AXuC6kIvrk5WKi10YXEJMnHXQDtkuGml98HGvR3pjKvD_dPnStKOMaDh08MjEg1oIMn4QWr5Oos13k5KkM4vZ1ruBaQF4mOpxD5zvkSYjaAFOsW4J8E-ZcIW9vLD-DZgtktdWDeWnnz--62zkbX0SH3KPEa7hf9d4Rw97bk__VJMrPGg8k5xXfuAt7rig87fhpXY0usZKYgMpUbgXOIHECe6jbl23_GOMzmvsXtnWFlddrvwOIHzEeVNaPGGzh38g3by3MH81vgh6lx4'),
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
