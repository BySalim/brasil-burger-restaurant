<?php

namespace App\Enum;

use App\Base\DisplayEnumInterface;
use App\Entity\Burger;
use App\Entity\Menu;
use App\Entity\Complement;

enum CategorieArticle: string implements DisplayEnumInterface
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

    #[\Override] public function getLabel(): string
    {
        return match($this) {
            self::BURGER => 'Burger',
            self::MENU => 'Menu',
            self::COMPLEMENT => 'Complément',
        };
    }

    #[\Override] public function getColor(): string
    {
        return match($this) {
            self::BURGER => 'blue',
            self::MENU => 'yellow',
            self::COMPLEMENT => 'gray',
        };
    }

    #[\Override] public function getIcon(): string
    {
        return match($this) {
            self::BURGER => throw new \Exception('To be implemented'),
            self::MENU => throw new \Exception('To be implemented'),
            self::COMPLEMENT => throw new \Exception('To be implemented'),
        };
    }

    #[\Override] public function getIconBg(): string
    {
        return match($this) {
            self::BURGER => throw new \Exception('To be implemented'),
            self::MENU => throw new \Exception('To be implemented'),
            self::COMPLEMENT => throw new \Exception('To be implemented'),
        };
    }

    #[\Override] public function getCardTitle(): string
    {
        return match($this) {
            self::BURGER => throw new \Exception('To be implemented'),
            self::MENU => throw new \Exception('To be implemented'),
            self::COMPLEMENT => throw new \Exception('To be implemented'),
        };
    }
}
