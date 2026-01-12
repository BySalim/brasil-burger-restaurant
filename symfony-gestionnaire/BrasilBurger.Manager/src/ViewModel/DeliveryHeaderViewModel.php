<?php

namespace App\ViewModel;

final class DeliveryHeaderViewModel
{
    public function __construct(
        public int $id,
        public ?string $statusLabel = null,
        public ?string $statusColor = null,
        public ?string $statusIcon = null,
        public ?string $date = null,
        public ?string $time = null,
        public bool $isEnCours = false,
    ) {}
}
