package com.brasilburger.infrastructure.cloudinary;

import com.brasilburger.config.AppConfig;

/**
 * Constantes pour les dossiers Cloudinary
 * Utilise la configuration depuis application.properties
 */
public class CloudinaryFolders {

    // Dossier racine des articles
    private static final String ARTICLES_BASE = AppConfig.getCloudinaryArticlesFolder();

    // Sous-dossiers
    public static final String BURGERS = ARTICLES_BASE + "/burgers";
    public static final String MENUS = ARTICLES_BASE + "/menus";
    public static final String COMPLEMENTS = ARTICLES_BASE + "/complements";
    public static final String TEST = ARTICLES_BASE + "/test";

    /**
     * Retourne le dossier de base des articles
     */
    public static String getArticlesBase() {
        return ARTICLES_BASE;
    }

    /**
     * Retourne le dossier des burgers
     */
    public static String getBurgers() {
        return BURGERS;
    }

    /**
     * Retourne le dossier des menus
     */
    public static String getMenus() {
        return MENUS;
    }

    /**
     * Retourne le dossier des complements
     */
    public static String getComplements() {
        return COMPLEMENTS;
    }


    public static String getSubfolderForArticle(String categorie) {
        return switch (categorie.toUpperCase()) {
            case "BURGER" -> BURGERS;
            case "MENU" -> MENUS;
            case "COMPLEMENT" -> COMPLEMENTS;
            default -> throw new IllegalArgumentException("Categorie inconnue: " + categorie);
        };
    }

    /**
     * Empeche l'instantiation
     */
    private CloudinaryFolders() {
        throw new UnsupportedOperationException("Classe utilitaire - pas d'instantiation");
    }
}