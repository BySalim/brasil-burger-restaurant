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

    #[\Override] public function getColor(): string
    {
        return match($this) {
            self::EN_COURS => 'blue',
            self::TERMINER => 'green',
        };
    }

    #[\Override] public function getIcon(): string
    {
        return match($this) {
            self::EN_COURS => 'local_shipping',
            self::TERMINER => 'check_circle',
        };
    }

    #[\Override] public function getIconBg(): string
    {
        return match($this) {
            self::EN_COURS => 'bg-blue-50 dark:bg-blue-900/10 text-blue-600',
            self:: TERMINER => 'bg-green-50 dark:bg-green-900/10 text-green-600',
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
