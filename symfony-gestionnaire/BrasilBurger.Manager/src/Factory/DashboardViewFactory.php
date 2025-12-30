<?php

namespace App\Factory;

use App\DTO\ArticleVenduDTO;
use App\DTO\DailyStatsDTO;
use App\Enum\EtatCommande;
use App\Infrastructure\Images\ImageStorageInterface;
use App\ViewModel\DashboardStatViewModel;
use App\ViewModel\TopProductViewModel;

readonly class DashboardViewFactory
{
    public function __construct(private ImageStorageInterface $imageStorage)
    {
    }

    /**
     * @param array<string, DailyStatsDTO> $dailyStatsByState
     * @return DashboardStatViewModel[]
     */
    public function createStatsView(array $dailyStatsByState, int $totalRevenue): array
    {
        $stats = [];

        foreach (EtatCommande::cases() as $etat) {
            $value = $dailyStatsByState[$etat->value]->count ?? 0;
            $stats[] = new DashboardStatViewModel(
                title: $etat->getCardTitle(),
                value: $value,
                icon: $etat->getIcon(),
                iconBg: $etat->getColor()->getIconClasseBg(),
            );
        }

        $stats[] = new DashboardStatViewModel(
            title: 'Recettes Journalières',
            value: $totalRevenue,
            icon: 'paid',
            iconBg: 'bg-emerald-50 dark:bg-emerald-900/10 text-emerald-600 dark:text-emerald-500',
            isPrice: true
        );

        return $stats;
    }

    /**
     * @param ArticleVenduDTO[] $articlesVenduDto
     * @return TopProductViewModel[]
     */
    public function createTopProductsView(array $articlesVenduDto): array
    {
        $products = [];

        foreach ($articlesVenduDto as $dto) {
            $sales = $dto->quantiteTotal;
            $a = $dto->article;
            $categorie = $a->getCategorie();
            $imgPublicId = $a->getImagePublicId();
            $products[] = new TopProductViewModel(
                name: $a->getLibelle(),
                category: $categorie->getLabel(),
                categoryColor: $categorie->getColor()->getBadgeClasses(),
                price: $a->getPrix(),
                sales: $sales,
                imageUrl: $this->imageStorage->getImageUrl($imgPublicId),
            );
        }
        return $products;
    }

}
