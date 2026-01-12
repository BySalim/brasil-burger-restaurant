<?php

namespace App\Service;

class PhoneNumberService
{
    public function formatSn(string $phone): string
    {
        $digits = preg_replace('/\D+/', '', $phone);

        if (str_starts_with($digits, '+221')) {
            $digits = substr($digits, 4);
        }

        if (!preg_match('/^(70|76|77|78)\d{7}$/', $digits)) {
            return $phone;
        }

        return substr($digits, 0, 2) . ' '
            . substr($digits, 2, 3) . ' '
            . substr($digits, 5, 2) . ' '
            . substr($digits, 7, 2);
    }
}
