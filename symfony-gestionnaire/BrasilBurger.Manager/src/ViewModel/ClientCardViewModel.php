<?php

namespace App\ViewModel;

final readonly class ClientCardViewModel
{
    public function __construct(
        public string $name,
        public string $phone,
        public ?string $initials = null,
    ) {}
}
