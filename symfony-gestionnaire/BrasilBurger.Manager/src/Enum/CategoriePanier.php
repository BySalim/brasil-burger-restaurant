<?php

namespace App\Enum;

use App\Base\DisplayEnumInterface;

enum CategoriePanier: string implements DisplayEnumInterface
{
    case BURGER = 'BURGER';
    case MENU = 'MENU';


    public function getLabel(): string
    {
        return match($this) {
            self::BURGER => 'Burger simple',
            self::MENU => 'Menu',
        };
    }

    public function getColor(): string
    {
        return match($this) {
            self::BURGER => 'blue',
            self::MENU => 'green',
        };
    }

    public function getIcon(): string
    {
        return match($this) {
            self::BURGER => throw new \Exception('To be implemented'),
            self::MENU => throw new \Exception('To be implemented'),
        };
    }

    public function getIconBg(): string
    {
        return match($this) {
            self::BURGER => throw new \Exception('To be implemented'),
            self::MENU => throw new \Exception('To be implemented'),
        };
    }

}
