<?php

namespace App\Enum;

use App\Base\DisplayEnumInterface;
use App\Base\EnumHelperTrait;

enum CategorieArticleQuantifier: string  implements DisplayEnumInterface
{
    use EnumHelperTrait;

    case MENU = 'MENU';
    case COMMANDE = 'COMMANDE';

    #[\Override] public function getLabel(): string
    {
        return match($this) {
            self::MENU => 'Menu',
            self::COMMANDE => 'Commande',
        };
    }

    #[\Override] public function getColor(): string
    {
        return match($this) {
            self::MENU => throw new \Exception('To be implemented'),
            self::COMMANDE => throw new \Exception('To be implemented'),
        };
    }

    #[\Override] public function getIcon(): string
    {
        return match($this) {
            self::MENU => throw new \Exception('To be implemented'),
            self::COMMANDE => throw new \Exception('To be implemented'),
        };
    }

    #[\Override] public function getIconBg(): string
    {
        return match($this) {
            self::MENU => throw new \Exception('To be implemented'),
            self::COMMANDE => throw new \Exception('To be implemented'),
        };
    }

    #[\Override] public function getCardTitle(): string
    {
        return match($this) {
            self::MENU => throw new \Exception('To be implemented'),
            self::COMMANDE => throw new \Exception('To be implemented'),
        };
    }
}
