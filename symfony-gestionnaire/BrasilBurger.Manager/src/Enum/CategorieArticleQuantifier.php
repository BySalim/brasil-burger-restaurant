<?php

namespace App\Enum;

enum CategorieArticleQuantifier: string
{
    case MENU = 'MENU';
    case COMMANDE = 'COMMANDE';

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
            self::MENU => throw new \Exception('To be implemented'),
            self::COMMANDE => throw new \Exception('To be implemented'),
        };
    }

    public function getIcon(): string
    {
        return match($this) {
            self::MENU => throw new \Exception('To be implemented'),
            self::COMMANDE => throw new \Exception('To be implemented'),
        };
    }

    public function getIconBg(): string
    {
        return match($this) {
            self::MENU => throw new \Exception('To be implemented'),
            self::COMMANDE => throw new \Exception('To be implemented'),
        };
    }
}
