<?php

namespace App\ViewModel;

readonly class DeliveryRowViewModel
{
    public function __construct(
        public int    $id,
        public string $date,
        public string $time,
        public string $zone,
        public string $delivererName,
        public string $delivererPhone,
        public string $clientName,
        public string $clientPhone,
        public string $statusLabel,
        public string $statusBadgeColor,
        public string $statusIcon,
        public bool   $isCompleted,
    ) {
    }
}
