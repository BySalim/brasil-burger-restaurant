<?php

namespace App\Enum;

enum EtatCommande: string
{
    case EN_ATTENTE = 'EN_ATTENTE';
    case EN_PREPARATION = 'EN_PREPARATION';
    case TERMINER = 'TERMINER';
    case ANNULER = 'ANNULER';
}
