<?php

namespace App\ViewModel;

final class DeliveryInfos
{
    public function __construct(
        public int $deliveryId,
        public int $priceDelivery,
        public ?string $orderCode = null,
        public ?int $orderId = null,
    ) {}
}
