<?php

namespace App\DTO;

class TopProductDTO
{
    public function __construct(
        public string $name,
        public string $category,
        public string $categoryColor,
        public int $price,
        public int $sales,
        public ?string $imageUrl = null,
        public string $fallbackIcon = 'lunch_dining'
    ) {}
}
