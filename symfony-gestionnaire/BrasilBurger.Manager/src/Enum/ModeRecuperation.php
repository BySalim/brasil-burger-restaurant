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
            self::SUR_PLACE => throw new \Exception('To be implemented'),
            self::EMPORTER => throw new \Exception('To be implemented'),
            self::LIVRER => throw new \Exception('To be implemented'),
        };
    }


    #[\Override] public function getColor(): string
    {
        return match($this) {
            self::SUR_PLACE => throw new \Exception('To be implemented'),
            self::EMPORTER => throw new \Exception('To be implemented'),
            self::LIVRER => throw new \Exception('To be implemented'),
        };
    }

    #[\Override] public function getIcon(): string
    {
        return match($this) {
            self::SUR_PLACE => throw new \Exception('To be implemented'),
            self::EMPORTER => throw new \Exception('To be implemented'),
            self::LIVRER => throw new \Exception('To be implemented'),
        };
    }

    #[\Override] public function getIconBg(): string
    {
        return match($this) {
            self::SUR_PLACE => throw new \Exception('To be implemented'),
            self::EMPORTER => throw new \Exception('To be implemented'),
            self::LIVRER => throw new \Exception('To be implemented'),
        };
    }

    #[\Override] public function getCardTitle(): string
    {
        return match($this) {
            self::SUR_PLACE => throw new \Exception('To be implemented'),
            self::EMPORTER => throw new \Exception('To be implemented'),
            self::LIVRER => throw new \Exception('To be implemented'),
        };
    }
}
