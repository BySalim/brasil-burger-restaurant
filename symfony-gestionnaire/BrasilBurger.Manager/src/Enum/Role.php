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

    #[\Override] public function getColor(): string
    {
        return match($this) {
            self:: CLIENT => 'blue',
            self::GESTIONNAIRE => 'purple',
        };
    }

    #[\Override] public function getIcon(): string
    {
        return match($this) {
            self::CLIENT => 'person',
            self::GESTIONNAIRE => 'admin_panel_settings',
        };
    }

    #[\Override] public function getIconBg(): string
    {
        return match($this) {
            self::CLIENT => 'bg-blue-50 dark:bg-blue-900/10 text-blue-600',
            self::GESTIONNAIRE => 'bg-purple-50 dark:bg-purple-900/10 text-purple-600',
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
