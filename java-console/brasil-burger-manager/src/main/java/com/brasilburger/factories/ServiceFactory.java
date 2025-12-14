package com.brasilburger.factories;

import com.brasilburger.domain. services.*;
import com.brasilburger.domain.services.impl.*;

/**
 * Factory pour créer les instances de services
 * Centralise la création et gère les dépendances entre services
 */
public class ServiceFactory {

    private static IZoneService zoneService;
    private static IQuartierService quartierService;
    private static ILivreurService livreurService;
    private static IArticleService articleService;
    private static ICodeArticleGenerator codeArticleGenerator;

    /**
     * Retourne une instance de ZoneService (Singleton)
     */
    public static IZoneService createZoneService() {
        if (zoneService == null) {
            zoneService = new ZoneServiceImpl(RepositoryFactory.createZoneRepository());
        }
        return zoneService;
    }

    /**
     * Retourne une instance de QuartierService (Singleton)
     */
    public static IQuartierService createQuartierService() {
        if (quartierService == null) {
            quartierService = new QuartierServiceImpl(
                    RepositoryFactory.createQuartierRepository(),
                    RepositoryFactory.createZoneRepository()
            );
        }
        return quartierService;
    }

    /**
     * Retourne une instance de LivreurService (Singleton)
     */
    public static ILivreurService createLivreurService() {
        if (livreurService == null) {
            livreurService = new LivreurServiceImpl(RepositoryFactory.createLivreurRepository());
        }
        return livreurService;
    }

    /**
     * Retourne une instance de CodeArticleGenerator (Singleton)
     */
    public static ICodeArticleGenerator createCodeArticleGenerator() {
        if (codeArticleGenerator == null) {
            codeArticleGenerator = new CodeArticleGeneratorImpl(RepositoryFactory.createArticleRepository());
        }
        return codeArticleGenerator;
    }

    /**
     * Retourne une instance de ArticleService (Singleton)
     */
    public static IArticleService createArticleService() {
        if (articleService == null) {
            articleService = new ArticleServiceImpl(
                    RepositoryFactory.createArticleRepository(),
                    createCodeArticleGenerator()
            );
        }
        return articleService;
    }

    /**
     * Retourne une instance de ImageStorageService (Singleton)
     * Délègue à ImageStorageFactory
     */
    public static IImageStorageService createImageStorageService() {
        return ImageStorageFactory.createImageStorageService();
    }

    /**
     * Réinitialise tous les services (utile pour les tests)
     */
    public static void resetAll() {
        zoneService = null;
        quartierService = null;
        livreurService = null;
        articleService = null;
        codeArticleGenerator = null;
        ImageStorageFactory.reset();  // Réinitialise aussi ImageStorageFactory
    }

    /**
     * Empêche l'instantiation
     */
    private ServiceFactory() {
        throw new UnsupportedOperationException("Classe factory - pas d'instantiation");
    }
}