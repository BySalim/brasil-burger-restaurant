<?php
namespace App\Enum;

use App\Base\DisplayEnumInterface;
use App\Base\EnumHelperTrait;

enum StatutLivraison: string implements DisplayEnumInterface
{
    use EnumHelperTrait;

    case EN_COURS = 'EN_COURS';
    case TERMINER = 'TERMINER';

    #[\Override] public function getLabel(): string
    {
        return match($this) {
            self::EN_COURS => 'En cours',
            self::TERMINER => 'Terminée',
        };
    }

    #[\Override] public function getColor(): Color
    {
        return match($this) {
            self::EN_COURS => Color::BLUE,
            self::TERMINER => Color::GREEN,
        };
    }

    #[\Override] public function getIcon(): string
    {
        return match($this) {
            self::EN_COURS => 'local_shipping',
            self::TERMINER => 'check_circle',
        };
    }

    #[\Override] public function getCardTitle(): string
    {
        return match($this) {
            self::EN_COURS => 'Livraisons en cours',
            self::TERMINER => 'Livraisons terminées',
        };
    }

    /**
     * Déterminer si le statut est final (ne peut plus être modifié)
     */
    public function isFinal(): bool
    {
        return $this === self::TERMINER;
    }

    /**
     * Définir les transitions autorisées depuis chaque statut
     *
     * @return StatutLivraison[]
     */
    public function getAllowedTransitions(): array
    {
        return match ($this) {
            self:: EN_COURS => [self:: TERMINER],
            self:: TERMINER => [],
        };
    }

    /**
     * Retourne un tableau indiquant pour chaque statut s'il est accessible depuis le statut actuel
     *
     * @return array<string, array{value: string, allowed:  bool, label: string, icon:  string, colorSolid:  string}>
     */
    public function getEditableStatusMap(): array
    {
        $allowedTransitions = $this->getAllowedTransitions();
        $map = [];

        foreach (self:: cases() as $case) {
            if ($case !== self::EN_COURS) {
                $map[$case->value] = [
                    'value' => $case->value,
                    'allowed' => in_array($case, $allowedTransitions, true),
                    'label' => $case->getLabel(),
                    'icon' => $case->getIcon(),
                    'colorSolid' => $case->getColor()->getSolidBadgeClasses(),
                ];
            }
        }

        return $map;
    }
}
