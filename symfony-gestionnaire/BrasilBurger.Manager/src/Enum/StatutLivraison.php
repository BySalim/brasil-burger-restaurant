<?php

namespace App\Enum;

use App\Base\DisplayEnumInterface;

enum StatutLivraison: string implements DisplayEnumInterface
{
    case EN_COURS = 'EN_COURS';
    case TERMINER = 'TERMINER';

    #[\Override] public function getLabel(): string
    {
        return match($this) {
            self::EN_COURS => throw new \Exception('To be implemented'),
            self::TERMINER => throw new \Exception('To be implemented'),
        };
    }

    #[\Override] public function getColor(): string
    {
        return match($this) {
            self::EN_COURS => throw new \Exception('To be implemented'),
            self::TERMINER => throw new \Exception('To be implemented'),
        };
    }

    #[\Override] public function getIcon(): string
    {
        return match($this) {
            self::EN_COURS => throw new \Exception('To be implemented'),
            self::TERMINER => throw new \Exception('To be implemented'),
        };
    }

    #[\Override] public function getIconBg(): string
    {
        return match($this) {
            self::EN_COURS => throw new \Exception('To be implemented'),
            self::TERMINER => throw new \Exception('To be implemented'),
        };
    }

    #[\Override] public function getCardTitle(): string
    {
        return match($this) {
            self::EN_COURS => throw new \Exception('To be implemented'),
            self::TERMINER => throw new \Exception('To be implemented'),
        };
    }
}
