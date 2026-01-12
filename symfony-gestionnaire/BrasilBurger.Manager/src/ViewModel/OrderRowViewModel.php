<?php

namespace App\ViewModel;

final readonly class OrderRowViewModel
{
    public function __construct(
        public int    $id,
        public string $date,
        public string $time,
        public string $code,
        public string $type,
        public string $typeColorClasses,
        public string $amount,
        public string $clientName,
        public string $clientPhone,
        public string $status,
        public string $statusColorClasses,

        /**
         * Map pour afficher les 4 boutons d’actions statut
         * @var array<string, array{value: string, allowed: bool, label: string, icon: string, colorSolid: string}>
         */
        public array  $editStatus,
    ) {}
}
