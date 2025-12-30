<?php

namespace App\Factory;

use App\DTO\DailyStatsDTO;
use App\Enum\EtatCommande;
use App\ViewModel\DashboardStatViewModel;

class DashboardViewFactory
{
    /**
     * @param array<string, DailyStatsDTO> $dailyStatsByState
     * @return DashboardStatViewModel[]
     */
    public function createStatsView(array $dailyStatsByState, int $totalRevenue): array
    {
        $stats = [];

        foreach (EtatCommande::cases() as $etat) {
            $stats[] = new DashboardStatViewModel(
                title: $etat->getCardTitle(),
                value: $dailyStatsByState[$etat->value]->count,
                icon: $etat->getIcon(),
                iconBg: $etat->getIconBg(),
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

}
