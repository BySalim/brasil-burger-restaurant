<?php

namespace App\DTO;

class DashboardStatDTO
{
    public function __construct(
        public int $commandesEnAttente,
        public int $commandesEnCours,
        public int $commandesTermine,
        public int $commandesAnnule,
        public int $recettes,
    ) {}
}
