<?php

namespace App\Enum;

use App\Base\DisplayEnumInterface;
use App\Base\EnumHelperTrait;

enum EtatCommande: string implements DisplayEnumInterface
{
    use EnumHelperTrait;

    case EN_ATTENTE = 'EN_ATTENTE';
    case EN_PREPARATION = 'EN_PREPARATION';
    case TERMINER = 'TERMINER';
    case ANNULER = 'ANNULER';

    #[\Override] public function getLabel(): string
    {
        return match($this) {
            self::EN_ATTENTE => throw new \Exception('To be implemented'),
            self::EN_PREPARATION => throw new \Exception('To be implemented'),
            self::TERMINER => throw new \Exception('To be implemented'),
            self::ANNULER => throw new \Exception('To be implemented'),
        };
    }

    #[\Override] public function getColor(): string
    {
        return match($this) {
            self::EN_ATTENTE => 'yellow',
            self::EN_PREPARATION => 'blue',
            self::TERMINER => 'green',
            self::ANNULER => 'red',
        };
    }

    #[\Override] public function getIcon(): string
    {
        return match($this) {
            self::EN_ATTENTE => 'schedule',
            self::EN_PREPARATION => 'restaurant',
            self::TERMINER => 'check_circle',
            self::ANNULER => 'cancel',
        };
    }

    #[\Override] public function getCardTitle(): string
    {
        return match($this) {
            self::EN_ATTENTE => 'Commandes en attente',
            self::EN_PREPARATION => 'Commandes en cours',
            self::TERMINER => 'Commandes Terminées',
            self::ANNULER => 'Commandes Annulées',
        };
    }

    #[\Override] public function getIconBg(): string
    {
        return match($this) {
            self::EN_ATTENTE => 'bg-amber-50 dark:bg-amber-900/10 text-amber-600 dark:text-amber-500',
            self::EN_PREPARATION => 'bg-orange-50 dark:bg-orange-900/10 text-orange-600 dark:text-orange-500',
            self::TERMINER => 'bg-green-50 dark:bg-green-900/10 text-green-600 dark:text-green-500',
            self::ANNULER => 'bg-red-50 dark:bg-red-900/10 text-red-600 dark:text-red-500',
        };
    }
}
