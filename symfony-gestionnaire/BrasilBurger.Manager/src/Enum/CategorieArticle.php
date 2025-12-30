<?php

namespace App\Enum;

use App\Base\DisplayEnumInterface;
use App\Base\EnumHelperTrait;
use App\Entity\Burger;
use App\Entity\Menu;
use App\Entity\Complement;
use Override;

enum CategorieArticle: string implements DisplayEnumInterface
{
    use EnumHelperTrait;

    case BURGER = 'BURGER';
    case MENU = 'MENU';
    case COMPLEMENT = 'COMPLEMENT';

    /**
     * Retourne la classe PHP correspondante à cette catégorie
     *
     * @return class-string
     */
    public function getEntityClass(): string
    {
        return match ($this) {
            self::BURGER => Burger::class,
            self::MENU => Menu::class,
            self::COMPLEMENT => Complement::class,
        };
    }

    /**
     * Retourne le nom court du dossier
     */
    public function getFolderName(): string
    {
        return match ($this) {
            self::BURGER => 'burgers',
            self::MENU => 'menus',
            self::COMPLEMENT => 'complements',
        };
    }

    #[\Override] public function getLabel(): string
    {
        return match($this) {
            self::BURGER => 'Burger',
            self::MENU => 'Menu',
            self::COMPLEMENT => 'Complément',
        };
    }

    #[\Override] public function getColor(): Color
    {
        return match($this) {
            self::BURGER => Color::BLUE,
            self::MENU => Color::YELLOW,
            self::COMPLEMENT => Color::GRAY,
        };
    }

    #[\Override] public function getIcon(): string
    {
        return match($this) {
            self::BURGER => 'lunch_dining',
            self::MENU => 'restaurant_menu',
            self::COMPLEMENT => 'fastfood',
        };
    }

    #[\Override] public function getCardTitle(): string
    {
        return match($this) {
            self::BURGER => 'Burgers',
            self::MENU => 'Menus',
            self::COMPLEMENT => 'Compléments',
        };
    }
}
