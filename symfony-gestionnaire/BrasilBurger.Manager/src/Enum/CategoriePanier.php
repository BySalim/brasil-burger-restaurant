<?php

namespace App\Enum;

use App\Base\DisplayEnumInterface;
use App\Base\EnumHelperTrait;

enum CategoriePanier: string implements DisplayEnumInterface
{
    use EnumHelperTrait;

    case BURGER = 'BURGER';
    case MENU = 'MENU';


    #[\Override] public function getLabel(): string
    {
        return match($this) {
            self::BURGER => 'Burger simple',
            self::MENU => 'Menu',
        };
    }

    #[\Override] public function getColor(): string
    {
        return match($this) {
            self::BURGER => 'blue',
            self::MENU => 'yellow',
        };
    }

    #[\Override] public function getIcon(): string
    {
        return match($this) {
            self::BURGER => throw new \Exception('To be implemented'),
            self::MENU => throw new \Exception('To be implemented'),
        };
    }

    #[\Override] public function getIconBg(): string
    {
        return match($this) {
            self::BURGER => throw new \Exception('To be implemented'),
            self::MENU => throw new \Exception('To be implemented'),
        };
    }

    #[\Override] public function getCardTitle(): string
    {
        return match($this) {
            self::BURGER => throw new \Exception('To be implemented'),
            self::MENU => throw new \Exception('To be implemented'),
        };
    }
}
