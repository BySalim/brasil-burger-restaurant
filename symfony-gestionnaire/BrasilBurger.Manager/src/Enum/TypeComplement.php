<?php
namespace App\Enum;

use App\Base\DisplayEnumInterface;
use App\Base\EnumHelperTrait;

enum TypeComplement: string implements DisplayEnumInterface
{
    use EnumHelperTrait;

    case BOISSON = 'BOISSON';
    case FRITES = 'FRITES';

    #[\Override] public function getLabel(): string
    {
        return match($this) {
            self::BOISSON => 'Boisson',
            self:: FRITES => 'Frites',
        };
    }

    #[\Override] public function getColor(): Color
    {
        return match($this) {
            self::BOISSON => Color::CYAN,
            self::FRITES => Color::YELLOW,
        };
    }

    #[\Override] public function getIcon(): string
    {
        return match($this) {
            self::BOISSON => 'local_cafe',
            self::FRITES => 'fastfood',
        };
    }

    #[\Override] public function getCardTitle(): string
    {
        return match($this) {
            self:: BOISSON => 'Boissons',
            self:: FRITES => 'Frites',
        };
    }
}
