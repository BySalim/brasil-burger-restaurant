<?php

namespace App\Enum;

use App\Base\DisplayEnumInterface;

enum ModePaiement: string implements DisplayEnumInterface
{
    case WAVE = 'WAVE';
    case OM = 'OM';

    public function getLabel(): string
    {
        return match($this) {
            self::WAVE => throw new \Exception('To be implemented'),
            self::OM => throw new \Exception('To be implemented'),
        };
    }

    public function getColor(): string
    {
        return match($this) {
            self::WAVE => throw new \Exception('To be implemented'),
            self::OM => throw new \Exception('To be implemented'),
        };
    }

    public function getIcon(): string
    {
        return match($this) {
            self::WAVE => throw new \Exception('To be implemented'),
            self::OM => throw new \Exception('To be implemented'),
        };
    }

    public function getIconBg(): string
    {
        return match($this) {
            self::WAVE => throw new \Exception('To be implemented'),
            self::OM => throw new \Exception('To be implemented'),
        };
    }
}
