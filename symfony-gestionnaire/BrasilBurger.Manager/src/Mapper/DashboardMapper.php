<?php

namespace App\Mapper;

use App\DTO\DashboardStatDTO;
use App\Enum\EtatCommande;

class DashboardMapper
{
    /**
     * @param array<string, int> $dataBase Données brutes.
     * Structure attendue :
     * + Clés dynamiques : correspond aux valeurs de l'enum EtatCommande (ex: 'EN_ATTENTE' => 10)
     * + Clé spéciale : 'total_revenu' (int) pour la recette
     * @return DashboardStatDTO[]
     */
    public function createStats(array $dataBase): array
    {
        $stats = [];

        // LES ÉTATS A AFFICHER
        $etatsAffiche = [
            EtatCommande::EN_ATTENTE,
            EtatCommande::EN_PREPARATION,
            EtatCommande::TERMINER,
            EtatCommande::ANNULER,
        ];

        foreach ($etatsAffiche as $etat) {
            $valeur = $this->extractValueForState($dataBase, $etat);

            $stats[] = new DashboardStatDTO(
                title: $etat->getCardTitle(),
                value: $valeur,
                icon: $etat->getIcon(),
                iconBg: $etat->getIconBg(),
            );
        }

        $stats[] = new DashboardStatDTO(
            title: 'Recettes Journalières',
            value: $dataBase['total_revenue'] ?? 0,
            icon: 'paid',
            iconBg: 'bg-emerald-50 dark:bg-emerald-900/10 text-emerald-600 dark:text-emerald-500',
            isPrice: true
        );

        return $stats;
    }

    // Petit helper pour trouver la bonne donnée dans le tableau $dataBase
    private function extractValueForState(array $data, EtatCommande $etat): int
    {
        return $data[$etat->value] ?? 0;
    }
}
