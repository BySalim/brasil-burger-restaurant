package com.brasilburger.factories;

import com.brasilburger.domain.repositories.*;
import com.brasilburger.domain.repositories.impl.*;

/**
 * Factory pour créer les instances de repositories
 * Centralise la création et facilite l'injection de dépendances
 */
public class RepositoryFactory {

    private static IZoneRepository zoneRepository;
    private static IQuartierRepository quartierRepository;
    private static ILivreurRepository livreurRepository;
    private static IArticleRepository articleRepository;

    /**
     * Retourne une instance de ZoneRepository (Singleton)
     */
    public static IZoneRepository createZoneRepository() {
        if (zoneRepository == null) {
            zoneRepository = new NeonZoneRepository();
        }
        return zoneRepository;
    }

    /**
     * Retourne une instance de QuartierRepository (Singleton)
     */
    public static IQuartierRepository createQuartierRepository() {
        if (quartierRepository == null) {
            quartierRepository = new NeonQuartierRepository();
        }
        return quartierRepository;
    }

    /**
     * Retourne une instance de LivreurRepository (Singleton)
     */
    public static ILivreurRepository createLivreurRepository() {
        if (livreurRepository == null) {
            livreurRepository = new NeonLivreurRepository();
        }
        return livreurRepository;
    }

    /**
     * Retourne une instance de ArticleRepository (Singleton)
     */
    public static IArticleRepository createArticleRepository() {
        if (articleRepository == null) {
            articleRepository = new NeonArticleRepository();
        }
        return articleRepository;
    }

    /**
     * Réinitialise tous les repositories (utile pour les tests)
     */
    public static void resetAll() {
        zoneRepository = null;
        quartierRepository = null;
        livreurRepository = null;
        articleRepository = null;
    }

    /**
     * Empêche l'instantiation
     */
    private RepositoryFactory() {
        throw new UnsupportedOperationException("Classe factory - pas d'instantiation");
    }
}