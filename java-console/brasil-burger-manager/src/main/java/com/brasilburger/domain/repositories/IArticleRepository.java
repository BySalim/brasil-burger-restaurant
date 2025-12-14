package com.brasilburger.domain.repositories;

import com.brasilburger.domain.entities.Article;
import com.brasilburger.domain.entities.enums.CategorieArticle;

import java.util.List;
import java.util.Optional;

/**
 * Interface du repository pour l'entite Article
 */
public interface IArticleRepository {

    /**
     * Sauvegarde un article (creation ou mise a jour)
     * @return Article sauvegarde avec son ID genere
     */
    Article save(Article article);

    /**
     * Trouve un article par son ID
     */
    Optional<Article> findById(Long id);

    /**
     * Trouve un article par son code
     */
    Optional<Article> findByCode(String code);

    /**
     * Retourne tous les articles
     */
    List<Article> findAll();

    /**
     * Retourne les articles par categorie (BURGER, MENU, COMPLEMENT)
     */
    List<Article> findByCategorie(CategorieArticle categorie);

    /**
     * Retourne les articles selon leur statut d'archivage
     */
    List<Article> findByEstArchiver(boolean estArchiver);

    /**
     * Retourne les articles disponibles (non archives) par categorie
     */
    List<Article> findAvailableByCategorie(CategorieArticle categorie);

    /**
     * Supprime un article par son ID
     */
    void delete(Long id);

    /**
     * Verifie si un article avec ce code existe deja
     */
    boolean existsByCode(String code);

    /**
     * Compte le nombre total d'articles
     */
    long count();

    /**
     * Compte le nombre d'articles par categorie
     */
    long countByCategorie(CategorieArticle categorie);
}