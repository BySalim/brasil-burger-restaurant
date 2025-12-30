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
            self::BURGER => 'lunch_dining',
            self::MENU => 'restaurant_menu',
        };
    }

    #[\Override] public function getIconBg(): string
    {
        return match($this) {
            self::BURGER => 'bg-blue-50 dark:bg-blue-900/10 text-blue-600',
            self::MENU => 'bg-yellow-50 dark:bg-yellow-900/10 text-yellow-600',
        };
    }

    #[\Override] public function getCardTitle(): string
    {
        return match($this) {
            self::BURGER => 'Burgers simples',
            self::MENU => 'Menus',
        };
    }
}
