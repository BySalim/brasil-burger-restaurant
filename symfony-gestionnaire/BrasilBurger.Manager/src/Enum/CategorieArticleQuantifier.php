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

    #[\Override] public function getColor(): Color
    {
        return match($this) {
            self::MENU => Color::YELLOW,
            self::COMMANDE => Color::BLUE,
        };
    }

    #[\Override] public function getIcon(): string
    {
        return match($this) {
            self::MENU => 'restaurant_menu',
            self::COMMANDE => 'shopping_cart',
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
