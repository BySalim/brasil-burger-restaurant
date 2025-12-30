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

    #[\Override] public function getColor(): string
    {
        return match($this) {
            self::BOISSON => 'cyan',
            self::FRITES => 'yellow',
        };
    }

    #[\Override] public function getIcon(): string
    {
        return match($this) {
            self::BOISSON => 'local_cafe',
            self::FRITES => 'fastfood',
        };
    }

    #[\Override] public function getIconBg(): string
    {
        return match($this) {
            self::BOISSON => 'bg-cyan-50 dark: bg-cyan-900/10 text-cyan-600',
            self::FRITES => 'bg-yellow-50 dark: bg-yellow-900/10 text-yellow-600',
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
