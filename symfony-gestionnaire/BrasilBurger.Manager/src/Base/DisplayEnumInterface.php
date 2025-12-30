<?php

namespace App\Base;

use App\Enum\Color;

/**
 * Interface pour les enums avec méthodes d'affichage
 *
 * ⚠️ IMPORTANT : Tous les enums implémentant cette interface DOIVENT
 * utiliser le trait EnumHelperTrait pour bénéficier des méthodes statiques :
 *
 * - getChoices(): array
 * - getValueLabelPairs(): array
 * - toArray(): array
 * - etc
 *
 * @see EnumHelperTrait
 */
interface DisplayEnumInterface
{
    public function getLabel(): string;
    public function getCardTitle(): string;
    public function getColor(): Color;
    public function getIcon(): string;
}
