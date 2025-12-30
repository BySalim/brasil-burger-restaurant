<?php
namespace App\Enum;

use App\Base\DisplayEnumInterface;
use App\Base\EnumHelperTrait;

enum CategorieArticleQuantifier: string implements DisplayEnumInterface
{
    use EnumHelperTrait;

    case MENU = 'MENU';
    case COMMANDE = 'COMMANDE';

    #[\Override] public function getLabel(): string
    {
        return match($this) {
            self::MENU => 'Composition de menu',
            self:: COMMANDE => 'Article de commande',
        };
    }

    #[\Override] public function getColor(): string
    {
        return match($this) {
            self::MENU => 'yellow',
            self::COMMANDE => 'blue',
        };
    }

    #[\Override] public function getIcon(): string
    {
        return match($this) {
            self::MENU => 'restaurant_menu',
            self::COMMANDE => 'shopping_cart',
        };
    }

    #[\Override] public function getIconBg(): string
    {
        return match($this) {
            self::MENU => 'bg-yellow-50 dark: bg-yellow-900/10 text-yellow-600',
            self::COMMANDE => 'bg-blue-50 dark: bg-blue-900/10 text-blue-600',
        };
    }

    #[\Override] public function getCardTitle(): string
    {
        return match($this) {
            self:: MENU => 'Compositions de menus',
            self::COMMANDE => 'Articles de commande',
        };
    }
}
