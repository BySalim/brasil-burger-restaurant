<?php
namespace App\Enum;

use App\Base\DisplayEnumInterface;
use App\Base\EnumHelperTrait;

enum StatutLivraison: string implements DisplayEnumInterface
{
    use EnumHelperTrait;

    case EN_COURS = 'EN_COURS';
    case TERMINER = 'TERMINER';

    #[\Override] public function getLabel(): string
    {
        return match($this) {
            self::EN_COURS => 'En cours',
            self:: TERMINER => 'Terminée',
        };
    }

    #[\Override] public function getColor(): Color
    {
        return match($this) {
            self::EN_COURS => Color::BLUE,
            self::TERMINER => Color::GREEN,
        };
    }

    #[\Override] public function getIcon(): string
    {
        return match($this) {
            self::EN_COURS => 'local_shipping',
            self::TERMINER => 'check_circle',
        };
    }

    #[\Override] public function getCardTitle(): string
    {
        return match($this) {
            self::EN_COURS => 'Livraisons en cours',
            self::TERMINER => 'Livraisons terminées',
        };
    }
}
