<?php

namespace App\Enum;

enum ModeRecuperation: string
{
    case SUR_PLACE = 'SUR_PLACE';
    case EMPORTER = 'EMPORTER';
    case LIVRER = 'LIVRER';

    public function getLabel(): string
    {
        return match($this) {
            self::SUR_PLACE => throw new \Exception('To be implemented'),
            self::EMPORTER => throw new \Exception('To be implemented'),
            self::LIVRER => throw new \Exception('To be implemented'),
        };
    }


    public function getColor(): string
    {
        return match($this) {
            self::SUR_PLACE => throw new \Exception('To be implemented'),
            self::EMPORTER => throw new \Exception('To be implemented'),
            self::LIVRER => throw new \Exception('To be implemented'),
        };
    }

    public function getIcon(): string
    {
        return match($this) {
            self::SUR_PLACE => throw new \Exception('To be implemented'),
            self::EMPORTER => throw new \Exception('To be implemented'),
            self::LIVRER => throw new \Exception('To be implemented'),
        };
    }

    public function getIconBg(): string
    {
        return match($this) {
            self::SUR_PLACE => throw new \Exception('To be implemented'),
            self::EMPORTER => throw new \Exception('To be implemented'),
            self::LIVRER => throw new \Exception('To be implemented'),
        };
    }
}
