package com.brasilburger.factories;

import com.brasilburger.domain.services.IImageStorageService;
import com.brasilburger.domain.services.impl.CloudinaryImageStorageService;

/**
 * Factory pour créer les instances de services de stockage d'images
 * Permet de changer facilement de provider (Cloudinary, S3, local, etc.)
 */
public class ImageStorageFactory {

    private static IImageStorageService imageStorageService;

    /**
     * Retourne une instance de ImageStorageService (Singleton)
     * Par défaut utilise Cloudinary
     */
    public static IImageStorageService createImageStorageService() {
        if (imageStorageService == null) {
            imageStorageService = new CloudinaryImageStorageService();
        }
        return imageStorageService;
    }

    /**
     * Crée une instance spécifique de ImageStorageService
     * Permet de forcer une nouvelle instance (utile pour les tests)
     */
    public static IImageStorageService createNewImageStorageService() {
        return new CloudinaryImageStorageService();
    }

    /**
     * Réinitialise le service (utile pour les tests)
     */
    public static void reset() {
        imageStorageService = null;
    }

    /**
     * Empêche l'instantiation
     */
    private ImageStorageFactory() {
        throw new UnsupportedOperationException("Classe factory - pas d'instantiation");
    }
}