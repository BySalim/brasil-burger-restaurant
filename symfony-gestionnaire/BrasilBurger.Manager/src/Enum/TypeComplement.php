<?php

namespace App\Enum;

use App\Base\DisplayEnumInterface;

enum TypeComplement: string implements DisplayEnumInterface
{
    case BOISSON = 'BOISSON';
    case FRITES = 'FRITES';

    #[\Override] public function getLabel(): string
    {
        return match($this) {
            self::BOISSON => throw new \Exception('To be implemented'),
            self::FRITES => throw new \Exception('To be implemented'),
        };
    }

    #[\Override] public function getColor(): string
    {
        return match($this) {
            self::BOISSON => throw new \Exception('To be implemented'),
            self::FRITES => throw new \Exception('To be implemented'),
        };
    }

    #[\Override] public function getIcon(): string
    {
        return match($this) {
            self::BOISSON => throw new \Exception('To be implemented'),
            self::FRITES => throw new \Exception('To be implemented'),
        };
    }

    #[\Override] public function getIconBg(): string
    {
        return match($this) {
            self::BOISSON => throw new \Exception('To be implemented'),
            self::FRITES => throw new \Exception('To be implemented'),
        };
    }

    #[\Override] public function getCardTitle(): string
    {
        return match($this) {
            self::BOISSON => throw new \Exception('To be implemented'),
            self::FRITES => throw new \Exception('To be implemented'),
        };
    }
}
