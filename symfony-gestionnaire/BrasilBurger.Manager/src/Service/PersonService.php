<?php

namespace App\Service;

class PersonService
{
    public function initials(string $nom, string $prenom): string
    {
        return strtoupper($nom[0] ?? 'C') . strtoupper($prenom[0] ?? 'L');
    }
}
