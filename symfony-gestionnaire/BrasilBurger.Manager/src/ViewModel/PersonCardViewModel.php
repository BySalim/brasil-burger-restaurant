<?php

namespace App\ViewModel;

final readonly class PersonCardViewModel
{
    public function __construct(
        public string $name,
        public string $phone,
        public ?int $id = null,
        public ?string $initials = null,
        public ?bool $estDisponible = null,
    ) {}
}
