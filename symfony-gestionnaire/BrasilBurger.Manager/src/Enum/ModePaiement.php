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
            self::OM => Color::BLACK,
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

    public function getImageUrl(): string
    {
        return match($this) {
            self::WAVE => 'https://play-lh.googleusercontent.com/B2sfLVgRWgV_bk5rtF51w6AieJWXc0qWbyWoaA8pMNp-is41AmvhJYVr95Dq9hT97Es=w480-h960-rw',
            self::OM => 'https://play-lh.googleusercontent.com/VGOxVRf_AtRYSFbYCr1qZ-eZDDldQxt8dpjQ62MFpoS9JXK-f2l1DIKxjt8TJ8MX-txu',
        };
    }
}
