<?php

namespace App\Enum;

use App\Entity\Burger;
use App\Entity\Menu;
use App\Entity\Complement;

enum CategorieArticle: string
{
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
}
