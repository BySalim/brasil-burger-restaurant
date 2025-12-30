<?php

namespace App\Enum;

use App\Base\DisplayEnumInterface;

enum StatutLivraison: string implements DisplayEnumInterface
{
    case EN_COURS = 'EN_COURS';
    case TERMINER = 'TERMINER';

    public function getLabel(): string
    {
        return match($this) {
            self::EN_COURS => throw new \Exception('To be implemented'),
            self::TERMINER => throw new \Exception('To be implemented'),
        };
    }

    public function getColor(): string
    {
        return match($this) {
            self::EN_COURS => throw new \Exception('To be implemented'),
            self::TERMINER => throw new \Exception('To be implemented'),
        };
    }

    public function getIcon(): string
    {
        return match($this) {
            self::EN_COURS => throw new \Exception('To be implemented'),
            self::TERMINER => throw new \Exception('To be implemented'),
        };
    }

    public function getIconBg(): string
    {
        return match($this) {
            self::EN_COURS => throw new \Exception('To be implemented'),
            self::TERMINER => throw new \Exception('To be implemented'),
        };
    }
}
