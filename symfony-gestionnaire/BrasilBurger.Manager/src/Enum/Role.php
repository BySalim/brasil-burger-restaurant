<?php
namespace App\Enum;

use App\Base\DisplayEnumInterface;
use App\Base\EnumHelperTrait;

enum Role: string implements DisplayEnumInterface
{
    use EnumHelperTrait;

    case CLIENT = 'CLIENT';
    case GESTIONNAIRE = 'GESTIONNAIRE';

    #[\Override] public function getLabel(): string
    {
        return match($this) {
            self::CLIENT => 'Client',
            self::GESTIONNAIRE => 'Gestionnaire',
        };
    }

    #[\Override] public function getColor(): Color
    {
        return match($this) {
            self:: CLIENT => Color::BLUE,
            self::GESTIONNAIRE => Color::PURPLE,
        };
    }

    #[\Override] public function getIcon(): string
    {
        return match($this) {
            self::CLIENT => 'person',
            self::GESTIONNAIRE => 'admin_panel_settings',
        };
    }

    #[\Override] public function getCardTitle(): string
    {
        return match($this) {
            self::CLIENT => 'Clients',
            self::GESTIONNAIRE => 'Gestionnaires',
        };
    }
}
