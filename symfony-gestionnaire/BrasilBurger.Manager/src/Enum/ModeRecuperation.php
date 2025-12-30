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

    #[\Override] public function getColor(): string
    {
        return match($this) {
            self::SUR_PLACE => 'purple',
            self::EMPORTER => 'blue',
            self::LIVRER => 'orange',
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

    #[\Override] public function getIconBg(): string
    {
        return match($this) {
            self::SUR_PLACE => 'bg-purple-50 dark:bg-purple-900/10 text-purple-600',
            self::EMPORTER => 'bg-blue-50 dark:bg-blue-900/10 text-blue-600',
            self::LIVRER => 'bg-orange-50 dark:bg-orange-900/10 text-orange-600',
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
