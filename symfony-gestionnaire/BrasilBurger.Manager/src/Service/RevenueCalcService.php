<?php
// src/Service/RevenueCalculator.php
namespace App\Service;

use App\DTO\DailyStatsDTO;
use App\Enum\EtatCommande;

class RevenueCalcService
{
    /**
     * @param DailyStatsDTO[] $stats
     */
    public function calculateTotalRevenue(array $stats): int
    {
        $total = 0;
        foreach ($stats as $stat) {
            if ($stat->etat !== EtatCommande::ANNULER) {
                $total += $stat->totalMontant;
            }
        }
        return $total;
    }
}
