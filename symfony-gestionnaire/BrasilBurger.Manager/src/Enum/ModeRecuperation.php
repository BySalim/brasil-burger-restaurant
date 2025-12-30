<?php
namespace App\Enum;

use App\Base\DisplayEnumInterface;
use App\Base\EnumHelperTrait;

enum ModeRecuperation: string implements DisplayEnumInterface
{
    use EnumHelperTrait;

    case SUR_PLACE = 'SUR_PLACE';
    case EMPORTER = 'EMPORTER';
    case LIVRER = 'LIVRER';

    #[\Override] public function getLabel(): string
    {
        return match($this) {
            self::SUR_PLACE => 'Sur place',
            self::EMPORTER => 'À emporter',
            self::LIVRER => 'Livraison',
        };
    }

    #[\Override] public function getColor(): Color
    {
        return match($this) {
            self::SUR_PLACE => Color::PURPLE,
            self::EMPORTER => Color::BLUE,
            self::LIVRER => Color::ORANGE,
        };
    }

    #[\Override] public function getIcon(): string
    {
        return match($this) {
            self::SUR_PLACE => 'restaurant',
            self::EMPORTER => 'shopping_bag',
            self::LIVRER => 'two_wheeler',
        };
    }

    #[\Override] public function getCardTitle(): string
    {
        return match($this) {
            self::SUR_PLACE => 'Commandes sur place',
            self::EMPORTER => 'Commandes à emporter',
            self::LIVRER => 'Commandes en livraison',
        };
    }
}
