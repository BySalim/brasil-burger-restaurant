<?php

namespace App\ViewModel;

final class OrderRowViewModel
{
    public function __construct(
        public readonly int $id,
        public readonly string $date,
        public readonly string $time,
        public readonly string $code,
        public readonly string $type,
        public readonly string $typeColorClasses,
        public readonly string $amount,
        public readonly string $clientName,
        public readonly string $clientPhone,
        public readonly string $status,
        public readonly string $statusColorClasses,

        /**
         * Map pour afficher les 4 boutons d’actions statut
         * @var array<string, array{value: string, allowed: bool, label: string, icon: string, colorSolid: string}>
         */
        public readonly array $editStatus,
    ) {}
}
