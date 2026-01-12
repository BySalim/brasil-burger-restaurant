<?php

namespace App\ViewModel;

final readonly class OrderHeaderViewModel
{
    public function __construct(
        public int $id,
        public string $code,
        public string $date,
        public string $time,

        public string $typeLabel,
        public string $typeClass,

        public int|string $totalAmount,

        public string $statusLabel,
        public string $statusIcon,
        public string $statusClass,
        public string $statusIconBg,
    ) {}
}
