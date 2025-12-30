<?php

namespace App\Enum;

use App\Base\DisplayEnumInterface;

enum EtatCommande: string implements DisplayEnumInterface
{
    case EN_ATTENTE = 'EN_ATTENTE';
    case EN_PREPARATION = 'EN_PREPARATION';
    case TERMINER = 'TERMINER';
    case ANNULER = 'ANNULER';

    public function getLabel(): string
    {
        return match($this) {
            self::EN_ATTENTE => throw new \Exception('To be implemented'),
            self::EN_PREPARATION => throw new \Exception('To be implemented'),
            self::TERMINER => throw new \Exception('To be implemented'),
            self::ANNULER => throw new \Exception('To be implemented'),
        };
    }

    public function getColor(): string
    {
        return match($this) {
            self::EN_ATTENTE => throw new \Exception('To be implemented'),
            self::EN_PREPARATION => throw new \Exception('To be implemented'),
            self::TERMINER => throw new \Exception('To be implemented'),
            self::ANNULER => throw new \Exception('To be implemented'),
        };
    }

    public function getIcon(): string
    {
        return match($this) {
            self::EN_ATTENTE => throw new \Exception('To be implemented'),
            self::EN_PREPARATION => throw new \Exception('To be implemented'),
            self::TERMINER => throw new \Exception('To be implemented'),
            self::ANNULER => throw new \Exception('To be implemented'),
        };
    }

}
