<?php

namespace App\Enum;

use App\Entity\Burger;
use App\Entity\Menu;
use App\Entity\Complement;

enum CategorieArticle: string
{
    case BURGER = 'Burger';
    case MENU = 'Menu';
    case COMPLEMENT = 'Complement';

    /**
     * Retourne la classe PHP correspondante à cette catégorie
     *
     * @return class-string
     */
    public function getEntityClass(): string
    {
        return match($this) {
            self::BURGER => Burger::class,
            self::MENU => Menu::class,
            self::COMPLEMENT => Complement::class,
        };
    }
}
