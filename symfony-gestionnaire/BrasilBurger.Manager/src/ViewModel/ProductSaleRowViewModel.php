<?php

namespace App\ViewModel;

class ProductSaleRowViewModel
{
    public function __construct(
        public string  $name,
        public string  $categoryLabel,
        public string  $categoryClass,
        public int     $unitPrice,
        public int     $quantity,
        public string|null $imageUrl = null,
        public string  $fallbackIcon = 'lunch_dining'
    ) {}
}
