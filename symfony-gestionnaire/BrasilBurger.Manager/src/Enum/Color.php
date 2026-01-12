<?php

namespace App\Enum;

enum Color: string
{
    case BLUE = 'blue';
    case INDIGO = 'indigo';
    case PURPLE = 'purple';
    case PINK = 'pink';
    case RED = 'red';
    case ORANGE = 'orange';
    case YELLOW = 'yellow';
    case GREEN = 'green';
    case TEAL = 'teal';
    case CYAN = 'cyan';
    case GRAY = 'gray';
    case SLATE = 'slate';
    case BLACK = 'black';
    case WHITE = 'white';


    /**
     * Retourne les classes Tailwind CSS pour ce badge de couleur
     */
    public function getBadgeClasses(): string
    {
        return match($this) {
            self::BLUE => 'bg-blue-100 text-blue-700 border-blue-200 dark:bg-blue-900/30 dark:text-blue-300 dark:border-blue-800',
            self::INDIGO => 'bg-indigo-100 text-indigo-700 border-indigo-200 dark:bg-indigo-900/30 dark:text-indigo-300 dark:border-indigo-800',
            self::PURPLE => 'bg-purple-100 text-purple-700 border-purple-200 dark:bg-purple-900/30 dark:text-purple-300 dark:border-purple-800',
            self::PINK => 'bg-pink-100 text-pink-700 border-pink-200 dark:bg-pink-900/30 dark:text-pink-300 dark:border-pink-800',
            self::RED => 'bg-red-100 text-red-700 border-red-200 dark:bg-red-900/30 dark:text-red-300 dark:border-red-800',
            self::ORANGE => 'bg-orange-100 text-orange-800 border-orange-200 dark:bg-orange-900/30 dark:text-orange-300 dark:border-orange-800',
            self::YELLOW => 'bg-yellow-100 text-yellow-800 border-yellow-200 dark:bg-yellow-900/30 dark:text-yellow-300 dark:border-yellow-800',
            self::GREEN => 'bg-green-100 text-green-700 border-green-200 dark:bg-green-900/30 dark:text-green-300 dark:border-green-800',
            self::TEAL => 'bg-teal-100 text-teal-700 border-teal-200 dark:bg-teal-900/30 dark:text-teal-300 dark:border-teal-800',
            self::CYAN => 'bg-cyan-100 text-cyan-800 border-cyan-200 dark:bg-cyan-900/30 dark:text-cyan-300 dark:border-cyan-800',
            self::GRAY => 'bg-gray-100 text-gray-700 border-gray-200 dark:bg-gray-900/30 dark:text-gray-300 dark:border-gray-800',
            self::SLATE => 'bg-slate-100 text-slate-700 border-slate-200 dark:bg-slate-800 dark:text-slate-300 dark:border-slate-700',
            self::BLACK => 'bg-gray-200 text-gray-900 border-gray-300 dark:bg-gray-900/40 dark:text-gray-100 dark:border-gray-800',
            self::WHITE => 'bg-white text-gray-700 border-gray-200 dark:bg-gray-800 dark:text-gray-100 dark:border-gray-700',
        };
    }

    public function getIconClasseBg(): string
    {
        return match ($this) {
            self::BLUE   => 'bg-blue-50 dark:bg-blue-900/10 text-blue-600',
            self::INDIGO => 'bg-indigo-50 dark:bg-indigo-900/10 text-indigo-600',
            self::PURPLE => 'bg-purple-50 dark:bg-purple-900/10 text-purple-600',
            self::PINK   => 'bg-pink-50 dark:bg-pink-900/10 text-pink-600',
            self::RED    => 'bg-red-50 dark:bg-red-900/10 text-red-600',
            self::ORANGE => 'bg-orange-50 dark:bg-orange-900/10 text-orange-600',
            self::YELLOW => 'bg-yellow-50 dark:bg-yellow-900/10 text-yellow-600',
            self::GREEN  => 'bg-green-50 dark:bg-green-900/10 text-green-600',
            self::TEAL   => 'bg-teal-50 dark:bg-teal-900/10 text-teal-600',
            self::CYAN   => 'bg-cyan-50 dark:bg-cyan-900/10 text-cyan-600',
            self::GRAY   => 'bg-gray-50 dark:bg-gray-900/10 text-gray-600',
            self::SLATE  => 'bg-slate-50 dark:bg-slate-900/10 text-slate-600',
            self::BLACK => 'bg-gray-200 dark:bg-gray-900/10 text-gray-900',
            self::WHITE => 'bg-white dark:bg-gray-800 text-gray-600',
        };
    }

    /**
     * Retourne les classes Tailwind CSS pour un badge foncé (fond coloré, texte blanc)
     */
    public function getSolidBadgeClasses(): string
    {
        return match($this) {
            self::BLUE => 'bg-blue-600 text-white border-blue-700 dark:bg-blue-700 dark:border-blue-800',
            self::INDIGO => 'bg-indigo-600 text-white border-indigo-700 dark: bg-indigo-700 dark:border-indigo-800',
            self::PURPLE => 'bg-purple-600 text-white border-purple-700 dark:bg-purple-700 dark:border-purple-800',
            self::PINK => 'bg-pink-600 text-white border-pink-700 dark:bg-pink-700 dark:border-pink-800',
            self::RED => 'bg-red-600 text-white border-red-700 dark:bg-red-700 dark:border-red-800',
            self::ORANGE => 'bg-orange-600 text-white border-orange-700 dark:bg-orange-700 dark:border-orange-800',
            self::YELLOW => 'bg-yellow-500 text-white border-yellow-600 dark:bg-yellow-600 dark:border-yellow-700',
            self::GREEN => 'bg-green-600 text-white border-green-700 dark:bg-green-700 dark:border-green-800',
            self::TEAL => 'bg-teal-600 text-white border-teal-700 dark:bg-teal-700 dark:border-teal-800',
            self::CYAN => 'bg-cyan-600 text-white border-cyan-700 dark:bg-cyan-700 dark:border-cyan-800',
            self::GRAY => 'bg-gray-600 text-white border-gray-700 dark:bg-gray-700 dark:border-gray-800',
            self::SLATE => 'bg-slate-600 text-white border-slate-700 dark:bg-slate-700 dark:border-slate-800',
            self::BLACK => 'bg-gray-900 text-white border-gray-900 dark:bg-black dark:border-black',
            self::WHITE => 'bg-white text-gray-800 border-gray-300 dark:bg-gray-100 dark:text-gray-900 dark:border-gray-300',
        };
    }

    /**
     * Retourne le label traduit de la couleur
     */
    public function getLabel(): string
    {
        return match($this) {
            self::BLUE => 'Bleu',
            self::INDIGO => 'Indigo',
            self::PURPLE => 'Violet',
            self::PINK => 'Rose',
            self::RED => 'Rouge',
            self::ORANGE => 'Orange',
            self::YELLOW => 'Jaune',
            self::GREEN => 'Vert',
            self::TEAL => 'Sarcelle',
            self::CYAN => 'Cyan',
            self::GRAY => 'Gris',
            self::SLATE => 'Ardoise',
            self::BLACK => 'Black',
            self::WHITE => 'White',
        };
    }
}
