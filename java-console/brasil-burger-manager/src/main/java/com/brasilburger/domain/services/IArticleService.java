package com.brasilburger.domain.services;

import com.brasilburger.domain.entities.*;
import com.brasilburger.domain.entities.enums.CategorieArticle;
import com.brasilburger.domain.entities.enums.TypeComplement;

import java.util.List;
import java.util.Optional;

/**
 * Interface du service Article
 * Contient la logique métier pour la gestion des articles (Burger, Menu, Complement)
 */
public interface IArticleService {

    /**
     * Crée un burger avec génération automatique du code
     * @param libelle Libellé du burger
     * @param description Description
     * @param prix Prix en FCFA
     * @param imagePublicId Public ID Cloudinary (optionnel)
     * @return Burger créé
     */
    Burger creerBurger(String libelle, String description, Integer prix, String imagePublicId);

    /**
     * Crée un complément avec génération automatique du code
     * @param libelle Libellé du complément
     * @param type Type (BOISSON, FRITES)
     * @param prix Prix en FCFA
     * @param imagePublicId Public ID Cloudinary (optionnel)
     * @return Complément créé
     */
    Complement creerComplement(String libelle, TypeComplement type, Integer prix, String imagePublicId);

    /**
     * Crée un menu avec génération automatique du code
     * @param libelle Libellé du menu
     * @param description Description
     * @param imagePublicId Public ID Cloudinary (optionnel)
     * @return Menu créé
     */
    Menu creerMenu(String libelle, String description, String imagePublicId);

    /**
     * Modifie un article
     * @param id ID de l'article
     * @param nouveauLibelle Nouveau libellé (null pour ne pas modifier)
     * @param nouveauImagePublicId Nouveau public_id image (null pour ne pas modifier)
     * @return Article modifié
     */
    Article modifierArticle(Long id, String nouveauLibelle, String nouveauImagePublicId);

    /**
     * Archive un article
     * @param id ID de l'article
     * @return Article archivé
     */
    Article archiverArticle(Long id);

    /**
     * Restaure un article archivé
     * @param id ID de l'article
     * @return Article restauré
     */
    Article restaurerArticle(Long id);

    /**
     * Supprime un article
     * @param id ID de l'article
     */
    void supprimerArticle(Long id);

    /**
     * Récupère un article par son ID
     * @param id ID de l'article
     * @return Optional contenant l'article si trouvé
     */
    Optional<Article> obtenirArticleParId(Long id);

    /**
     * Récupère un article par son code
     * @param code Code de l'article
     * @return Optional contenant l'article si trouvé
     */
    Optional<Article> obtenirArticleParCode(String code);

    /**
     * Liste tous les articles
     * @return Liste de tous les articles
     */
    List<Article> listerTousLesArticles();

    /**
     * Liste les articles par catégorie
     * @param categorie Catégorie (BURGER, MENU, COMPLEMENT)
     * @return Liste des articles de la catégorie
     */
    List<Article> listerArticlesParCategorie(CategorieArticle categorie);

    /**
     * Liste les articles disponibles (non archivés)
     * @return Liste des articles disponibles
     */
    List<Article> listerArticlesDisponibles();

    /**
     * Liste les articles disponibles par catégorie
     * @param categorie Catégorie
     * @return Liste des articles disponibles
     */
    List<Article> listerArticlesDisponiblesParCategorie(CategorieArticle categorie);

    /**
     * Liste les burgers disponibles
     * @return Liste des burgers disponibles
     */
    List<Burger> listerBurgersDisponibles();

    /**
     * Liste les menus disponibles
     * @return Liste des menus disponibles
     */
    List<Menu> listerMenusDisponibles();

    /**
     * Liste les compléments disponibles
     * @return Liste des compléments disponibles
     */
    List<Complement> listerComplementsDisponibles();

    /**
     * Liste les compléments par type
     * @param type Type de complément (BOISSON, FRITES)
     * @return Liste des compléments
     */
    List<Complement> listerComplementsParType(TypeComplement type);

    /**
     * Compte le nombre total d'articles
     * @return Nombre d'articles
     */
    long compterArticles();

    /**
     * Compte le nombre d'articles par catégorie
     * @param categorie Catégorie
     * @return Nombre d'articles
     */
    long compterArticlesParCategorie(CategorieArticle categorie);

    /**
     * Vérifie si un article existe par son code
     * @param code Code de l'article
     * @return true si l'article existe
     */
    boolean articleExiste(String code);

    // ===================================
    // Gestion des composants de Menu
    // ===================================

    /**
     * Ajoute un composant (Burger ou Complément) à un menu
     * @param menuId ID du menu
     * @param articleId ID de l'article à ajouter
     * @param quantite Quantité
     * @return Menu mis à jour
     */
    Menu ajouterComposantAuMenu(Long menuId, Long articleId, Integer quantite);

    /**
     * Retire un composant d'un menu
     * @param menuId ID du menu
     * @param articleQuantifierId ID de l'ArticleQuantifier à retirer
     * @return Menu mis à jour
     */
    Menu retirerComposantDuMenu(Long menuId, Long articleQuantifierId);

    /**
     * Modifie la quantité d'un composant dans un menu
     * @param articleQuantifierId ID de l'ArticleQuantifier
     * @param nouvelleQuantite Nouvelle quantité
     * @return ArticleQuantifier mis à jour
     */
    ArticleQuantifier modifierQuantiteComposant(Long articleQuantifierId, Integer nouvelleQuantite);

    /**
     * Récupère tous les composants d'un menu avec leurs articles
     * @param menuId ID du menu
     * @return Liste des ArticleQuantifier
     */
    List<ArticleQuantifier> obtenirComposantsMenu(Long menuId);

    /**
     * Calcule le prix total d'un menu (en excluant les articles archivés)
     * @param menuId ID du menu
     * @return Prix total en FCFA
     */
    Integer calculerPrixMenu(Long menuId);

    /**
     * Charge un menu avec tous ses composants
     * @param menuId ID du menu
     * @return Menu avec composants chargés
     */
    Optional<Menu> chargerMenuAvecComposants(Long menuId);

    /**
     * Supprime tous les composants d'un menu
     * @param menuId ID du menu
     * @return Nombre de composants supprimés
     */
    int viderComposantsMenu(Long menuId);
}