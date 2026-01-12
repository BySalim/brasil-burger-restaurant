<?php

namespace App\ViewModel;

final class DeliveryLocationNoteViewModel
{
    public function __construct(
        public ?string $zone = null,
        public ?string $neighborhood = null,
        public ?string $note = null,
    ) {}
}
