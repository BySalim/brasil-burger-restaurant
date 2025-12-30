<?php

namespace App\Twig;

use Twig\Extension\AbstractExtension;
use Twig\TwigFilter;

class AppExtension extends AbstractExtension
{
    public function getFilters(): array
    {
        return [
            new TwigFilter('app_number_formated', [$this, 'formatNumber']),
        ];
    }

    public function formatNumber($number): string
    {
        if (!is_numeric($number)) {
            return '0';
        }

        return number_format((float)$number, 0, '', ' ');
    }
}
