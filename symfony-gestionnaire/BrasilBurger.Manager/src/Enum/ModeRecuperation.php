<?php

namespace App\Enum;

enum ModeRecuperation: string
{
    case SUR_PLACE = 'SUR_PLACE';
    case EMPORTER = 'EMPORTER';
    case LIVRER = 'LIVRER';
}
