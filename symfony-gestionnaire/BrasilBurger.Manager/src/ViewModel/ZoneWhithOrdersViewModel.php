<?php

namespace App\ViewModel;

final readonly class ZoneWhithOrdersViewModel
{
    public function __construct(
        public int $id,
        public string $name,
        /** @var OrderToDeliveredRowViewModel[] */
        public array $ordersToDelivered,
    )
    {}
}
