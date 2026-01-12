<?php

namespace App\ViewModel;

final readonly class DeliveryCardViewModel
{
    public function __construct(
        public string          $label,
        public string          $icon,
        public string          $color,
        public int|null        $deliveryPrice = null,
        public int|string|null $deliveryId = null,
        public int|string|null $deliveryInfoId = null,
        public bool            $isDelivered = false,
    ) {}
}
