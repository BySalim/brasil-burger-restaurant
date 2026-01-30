<?php

namespace App\ViewModel;

final readonly class OrderToDeliveredRowViewModel
{
    public function __construct(
        public int $id,
        public int $idLivraison,
        public string $code,
        public string $quartier,
        public PersonCardViewModel $client,
    )
    {}
}
