<?php

namespace App\ViewModel;

readonly class PaginationViewModel
{
    public function __construct(
        public int $totalItems,
        public int $itemsPerPage,
        public int $currentPage,
    ) {
    }
}
