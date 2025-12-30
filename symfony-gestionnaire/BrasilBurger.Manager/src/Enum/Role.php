<?php

namespace App\Enum;

use App\Base\DisplayEnumInterface;

enum Role: string implements DisplayEnumInterface
{
    case CLIENT = 'CLIENT';
    case GESTIONNAIRE = 'GESTIONNAIRE';

    #[\Override] public function getLabel(): string
    {
        return match($this) {
            self::CLIENT => throw new \Exception('To be implemented'),
            self::GESTIONNAIRE => throw new \Exception('To be implemented'),
        };
    }

    #[\Override] public function getColor(): string
    {
        return match($this) {
            self::CLIENT => throw new \Exception('To be implemented'),
            self::GESTIONNAIRE => throw new \Exception('To be implemented'),
        };
    }

    #[\Override] public function getIcon(): string
    {
        return match($this) {
            self::CLIENT => throw new \Exception('To be implemented'),
            self::GESTIONNAIRE => throw new \Exception('To be implemented'),
        };
    }

    #[\Override] public function getIconBg(): string
    {
        return match($this) {
            self::CLIENT => throw new \Exception('To be implemented'),
            self::GESTIONNAIRE => throw new \Exception('To be implemented'),
        };
    }

    #[\Override] public function getCardTitle(): string
    {
        return match($this) {
            self::CLIENT => throw new \Exception('To be implemented'),
            self::GESTIONNAIRE => throw new \Exception('To be implemented'),
        };
    }
}
