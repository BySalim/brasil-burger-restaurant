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

    #[\Override] public function getColor(): string
    {
        return match($this) {
            self::WAVE => 'blue',
            self::OM => 'orange',
        };
    }

    #[\Override] public function getIcon(): string
    {
        return match($this) {
            self::WAVE => 'account_balance_wallet',
            self::OM => 'phone_android',
        };
    }

    #[\Override] public function getIconBg(): string
    {
        return match($this) {
            self::WAVE => 'bg-blue-50 dark:bg-blue-900/10 text-blue-600',
            self::OM => 'bg-orange-50 dark:bg-orange-900/10 text-orange-600',
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
