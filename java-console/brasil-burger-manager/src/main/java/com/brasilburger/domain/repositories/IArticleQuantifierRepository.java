package com.brasilburger.domain.repositories;

import com.brasilburger.domain.entities.ArticleQuantifier;

import java.util.List;
import java.util.Optional;

/**
 * Interface du repository ArticleQuantifier
 * Gère la persistance des ArticleQuantifier (composants de menu et panier)
 */
public interface IArticleQuantifierRepository {

    /**
     * Sauvegarde un ArticleQuantifier (INSERT ou UPDATE)
     * @param articleQuantifier ArticleQuantifier à sauvegarder
     * @return ArticleQuantifier sauvegardé avec son ID
     */
    ArticleQuantifier save(ArticleQuantifier articleQuantifier);

    /**
     * Recherche un ArticleQuantifier par son ID
     * @param id ID de l'ArticleQuantifier
     * @return Optional contenant l'ArticleQuantifier si trouvé
     */
    Optional<ArticleQuantifier> findById(Long id);

    /**
     * Récupère tous les ArticleQuantifier d'un menu
     * @param menuId ID du menu
     * @return Liste des ArticleQuantifier du menu
     */
    List<ArticleQuantifier> findByMenuId(Long menuId);

    /**
     * Récupère tous les ArticleQuantifier d'un panier
     * @param panierId ID du panier
     * @return Liste des ArticleQuantifier du panier
     */
    List<ArticleQuantifier> findByPanierId(Long panierId);

    /**
     * Supprime un ArticleQuantifier par son ID
     * @param id ID de l'ArticleQuantifier
     */
    void delete(Long id);

    /**
     * Supprime tous les ArticleQuantifier d'un menu
     * @param menuId ID du menu
     * @return Nombre d'ArticleQuantifier supprimés
     */
    int deleteByMenuId(Long menuId);

    /**
     * Supprime tous les ArticleQuantifier d'un panier
     * @param panierId ID du panier
     * @return Nombre d'ArticleQuantifier supprimés
     */
    int deleteByPanierId(Long panierId);

    /**
     * Compte le nombre d'ArticleQuantifier dans un menu
     * @param menuId ID du menu
     * @return Nombre d'ArticleQuantifier
     */
    long countByMenuId(Long menuId);

    /**
     * Compte le nombre d'ArticleQuantifier dans un panier
     * @param panierId ID du panier
     * @return Nombre d'ArticleQuantifier
     */
    long countByPanierId(Long panierId);

    /**
     * Récupère tous les ArticleQuantifier
     * @return Liste de tous les ArticleQuantifier
     */
    List<ArticleQuantifier> findAll();
}