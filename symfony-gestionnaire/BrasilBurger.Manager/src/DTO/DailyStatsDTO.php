<?php

namespace App\DTO;

use App\Enum\EtatCommande;

class DailyStatsDTO
{
    public function __construct(
        public EtatCommande $etat,
        public int $count,
        public int $totalMontant
    ) {}

    /**
     * @param DailyStatsDTO[] $listeBrute
     * @return array<string, DailyStatsDTO>
     */
    public static function indexByEtat(array $listeBrute): array
    {
        $tableauAssocie = [];

        foreach ($listeBrute as $dto) {
            $tableauAssocie[$dto->etat->value] = $dto;
        }

        return $tableauAssocie;
    }
}
