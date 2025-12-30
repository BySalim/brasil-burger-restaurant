<?php

namespace App\Enum;

use App\Base\DisplayEnumInterface;

enum TypeComplement: string implements DisplayEnumInterface
{
    case BOISSON = 'BOISSON';
    case FRITES = 'FRITES';

    public function getLabel(): string
    {
        return match($this) {
            self::BOISSON => throw new \Exception('To be implemented'),
            self::FRITES => throw new \Exception('To be implemented'),
        };
    }

    public function getColor(): string
    {
        return match($this) {
            self::BOISSON => throw new \Exception('To be implemented'),
            self::FRITES => throw new \Exception('To be implemented'),
        };
    }

    public function getIcon(): string
    {
        return match($this) {
            self::BOISSON => throw new \Exception('To be implemented'),
            self::FRITES => throw new \Exception('To be implemented'),
        };
    }

    public function getIconBg(): string
    {
        return match($this) {
            self::BOISSON => throw new \Exception('To be implemented'),
            self::FRITES => throw new \Exception('To be implemented'),
        };
    }
}
