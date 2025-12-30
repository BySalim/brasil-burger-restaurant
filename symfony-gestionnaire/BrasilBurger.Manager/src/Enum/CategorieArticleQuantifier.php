<?php

namespace App\Enum;

use App\Base\DisplayEnumInterface;

enum CategorieArticleQuantifier: string  implements DisplayEnumInterface
{
    case MENU = 'MENU';
    case COMMANDE = 'COMMANDE';

    public function getLabel(): string
    {
        return match($this) {
            self::MENU => 'Menu',
            self::COMMANDE => 'Commande',
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
