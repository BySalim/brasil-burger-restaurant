<?php

namespace App\ViewModel;

readonly class StatusBadgeViewModel
{
    public function __construct(
        public string $label,
        public string $icon,
        public string $color,
    ) {
    }
}
