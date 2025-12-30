<?php

namespace App\Enum;

enum Role: string
{
    case CLIENT = 'CLIENT';
    case GESTIONNAIRE = 'GESTIONNAIRE';

    public function getLabel(): string
    {
        return match($this) {
            self::CLIENT => throw new \Exception('To be implemented'),
            self::GESTIONNAIRE => throw new \Exception('To be implemented'),
        };
    }

    public function getColor(): string
    {
        return match($this) {
            self::CLIENT => throw new \Exception('To be implemented'),
            self::GESTIONNAIRE => throw new \Exception('To be implemented'),
        };
    }

    public function getIcon(): string
    {
        return match($this) {
            self::CLIENT => throw new \Exception('To be implemented'),
            self::GESTIONNAIRE => throw new \Exception('To be implemented'),
        };
    }

    public function getIconBg(): string
    {
        return match($this) {
            self::CLIENT => throw new \Exception('To be implemented'),
            self::GESTIONNAIRE => throw new \Exception('To be implemented'),
        };
    }
}
