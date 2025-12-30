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

    #[\Override] public function getColor(): Color
    {
        return match($this) {
            self::BURGER => Color::BLUE,
            self::MENU => Color::YELLOW,
        };
    }

    #[\Override] public function getIcon(): string
    {
        return match($this) {
            self::BURGER => 'lunch_dining',
            self::MENU => 'restaurant_menu',
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
