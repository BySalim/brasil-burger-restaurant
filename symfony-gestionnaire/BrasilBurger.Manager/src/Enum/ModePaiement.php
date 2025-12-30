<?php
namespace App\Enum;

use App\Base\DisplayEnumInterface;
use App\Base\EnumHelperTrait;

enum ModePaiement: string implements DisplayEnumInterface
{
    use EnumHelperTrait;

    case WAVE = 'WAVE';
    case OM = 'OM';

    #[\Override] public function getLabel(): string
    {
        return match($this) {
            self:: WAVE => 'Wave',
            self::OM => 'Orange Money',
        };
    }

    #[\Override] public function getColor(): Color
    {
        return match($this) {
            self::WAVE => Color::BLUE,
            self::OM => Color::ORANGE,
        };
    }

    #[\Override] public function getIcon(): string
    {
        return match($this) {
            self::WAVE => 'account_balance_wallet',
            self::OM => 'phone_android',
        };
    }

    #[\Override] public function getCardTitle(): string
    {
        return match($this) {
            self::WAVE => 'Paiements Wave',
            self::OM => 'Paiements Orange Money',
        };
    }
}
