<?php

namespace App\ViewModel;

readonly class DelivererRowViewModel
{
    public function __construct(
        public int    $id,
        public string $name,
        public string $phone,
        public ?bool  $estDisponible = false,
    ) {
    }
}
