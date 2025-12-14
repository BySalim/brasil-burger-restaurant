package com.brasilburger.domain.services;

import com.brasilburger.domain.entities.enums.CategorieArticle;

/**
 * Interface du generateur de codes uniques pour les articles
 * Permet de generer des codes automatiques selon la categorie
 */
public interface ICodeArticleGenerator {

    /**
     * Genere le prochain code pour un article selon sa categorie
     * @param categorie Categorie de l'article (BURGER, MENU, COMPLEMENT)
     * @return Code unique genere (ex: BRG001, MNU001, CMP001)
     */
    String genererCode(CategorieArticle categorie);

    /**
     * Genere le prochain code pour un burger
     * @return Code unique au format BRG###
     */
    String genererCodeBurger();

    /**
     * Genere le prochain code pour un menu
     * @return Code unique au format MNU###
     */
    String genererCodeMenu();

    /**
     * Genere le prochain code pour un complement
     * @return Code unique au format CMP###
     */
    String genererCodeComplement();

    /**
     * Reinitialise les compteurs (utile pour les tests)
     */
    void reinitialiserCompteurs();
}