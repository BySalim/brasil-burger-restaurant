<?php

namespace App\ViewModel;

final readonly class PaymentCardViewModel
{
    public function __construct(
        public int|string $amount,
        public string $date,
        public string $time,
        public string $reference,

        public string $methodLabel,
        public string $methodBadgeClass,
        public ?string $methodImageUrl = null,
        public ?string $methodIcon = null,
    ) {}
}
