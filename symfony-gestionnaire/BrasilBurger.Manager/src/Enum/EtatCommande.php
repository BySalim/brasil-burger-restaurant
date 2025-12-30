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

    #[\Override] public function getColor(): Color
    {
        return match($this) {
            self::EN_ATTENTE => Color::YELLOW,
            self::EN_PREPARATION => Color::BLUE,
            self::TERMINER => Color::GREEN,
            self::ANNULER => Color::RED,
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

    /*
     * Déterminer si on peut changer l'état
     */
    public function isFinal(): bool
    {
        return in_array($this, [self:: TERMINER, self::ANNULER]);
    }

    /*
     * Définir les règles de conversion de chaque état
     */
    public function getAllowedTransitions(): array
    {
        return match ($this) {
            self::EN_ATTENTE => [self::EN_PREPARATION, self::ANNULER],
            self::EN_PREPARATION => [self:: TERMINER, self::ANNULER],
            self::TERMINER, self::ANNULER => [],
        };
    }
}
