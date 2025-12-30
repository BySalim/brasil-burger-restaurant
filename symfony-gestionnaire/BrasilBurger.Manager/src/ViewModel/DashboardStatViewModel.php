<?php

namespace App\ViewModel;

class DashboardStatViewModel
{
    public function __construct(
        public string $title,
        public int $value,
        public string $icon,
        public string $iconBg,
        public bool $isPrice = false,
    ) {}
}
