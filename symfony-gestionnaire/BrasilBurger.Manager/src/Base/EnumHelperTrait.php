<?php

namespace App\Base;

/**
 * Trait pour ajouter des méthodes statiques utiles aux Enums
 *
 * ⚠️ REQUIS : L'Enum doit implémenter DisplayEnumInterface
 *
 */
trait EnumHelperTrait
{
    /**
     * Retourne tous les choix pour un formulaire Symfony
     * Format : ['Label' => EnumCase]
     *
     * @return array<string, static>
     */
    public static function getChoices(): array
    {
        $choices = [];
        foreach (static:: cases() as $case) {
            /** @var DisplayEnumInterface $case */
            $choices[$case->getLabel()] = $case;
        }
        return $choices;
    }

    /**
     * Retourne les choix avec la valeur comme clé
     * Format : ['VALUE' => 'Label']
     *
     * @return array<string, string>
     */
    public static function getValueLabelPairs(): array
    {
        $pairs = [];
        foreach (static::cases() as $case) {
            /** @var DisplayEnumInterface $case */
            $pairs[$case->value] = $case->getLabel();
        }
        return $pairs;
    }

    /**
     * Retourne un tableau complet pour usage API/JSON
     *
     * @return array<int, array{value:  string, label: string, color:  string, icon: string, iconBg: string, cardTitle: string}>
     */
    public static function toArray(): array
    {
        return array_map(
            function (DisplayEnumInterface $case) {
                return [
                    'value' => $case->value,
                    'label' => $case->getLabel(),
                    'color' => $case->getColor(),
                    'icon' => $case->getIcon(),
                    'iconBg' => $case->getIconBg(),
                    'cardTitle' => $case->getCardTitle(),
                ];
            },
            static::cases()
        );
    }

    /**
     * Trouve un cas par son label (insensible à la casse)
     *
     * @return DisplayEnumInterface|null
     */
    public static function fromLabel(string $label): DisplayEnumInterface|null
    {
        foreach (static::cases() as $case) {
            /** @var DisplayEnumInterface $case */
            if (strcasecmp($case->getLabel(), $label) === 0) {
                return $case;
            }
        }
        return null;
    }


    /**
     * Retourne les labels de tous les cas
     *
     * @return string[]
     */
    public static function getLabels(): array
    {
        return array_map(
            fn(DisplayEnumInterface $case) => $case->getLabel(),
            static::cases()
        );
    }

}
