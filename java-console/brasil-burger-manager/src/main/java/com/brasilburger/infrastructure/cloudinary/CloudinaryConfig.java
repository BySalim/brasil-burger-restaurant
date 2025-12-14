package com.brasilburger.infrastructure.cloudinary;

import com.brasilburger.config.AppConfig;
import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration de Cloudinary pour la gestion des images
 * Singleton pour partager une seule instance
 */
public class CloudinaryConfig {
    private static final Logger logger = LoggerFactory.getLogger(CloudinaryConfig.class);
    private static Cloudinary cloudinaryInstance;

    static {
        try {
            initializeCloudinary();
            logger.info("Cloudinary initialise avec succes");
        } catch (Exception e) {
            logger.error("Erreur lors de l'initialisation de Cloudinary", e);
            throw new RuntimeException("Impossible d'initialiser Cloudinary", e);
        }
    }

    /**
     * Initialise l'instance Cloudinary avec les credentials
     */
    private static void initializeCloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", AppConfig.getCloudinaryCloudName());
        config.put("api_key", AppConfig.getCloudinaryApiKey());
        config.put("api_secret", AppConfig.getCloudinaryApiSecret());
        config.put("secure", "true"); // Toujours utiliser HTTPS

        cloudinaryInstance = new Cloudinary(config);

        logger.info("Configuration Cloudinary:");
        logger.info("  - Cloud Name: {}", AppConfig.getCloudinaryCloudName());
        logger.info("  - API Key: {}****",
                AppConfig.getCloudinaryApiKey().substring(0, Math.min(4, AppConfig.getCloudinaryApiKey().length())));
    }

    /**
     * Retourne l'instance Cloudinary (Singleton)
     */
    public static Cloudinary getInstance() {
        if (cloudinaryInstance == null) {
            throw new IllegalStateException("Cloudinary n'est pas initialise");
        }
        return cloudinaryInstance;
    }

    /**
     * Teste la connexion a Cloudinary
     * CORRECTION:  Ajout de ObjectUtils.emptyMap()
     */
    public static boolean testConnection() {
        try {
            Cloudinary cloudinary = getInstance();
            Map result = cloudinary.api().ping(ObjectUtils.emptyMap());

            if (result != null && "ok".equals(result.get("status"))) {
                logger.info("Test de connexion Cloudinary reussi");
                return true;
            }

            logger.error("Test de connexion Cloudinary echoue");
            return false;

        } catch (Exception e) {
            logger.error("Erreur lors du test de connexion Cloudinary", e);
            return false;
        }
    }

    /**
     * Genere une URL complete a partir d'un public_id
     */
    public static String generateUrl(String publicId) {
        if (publicId == null || publicId.trim().isEmpty()) {
            return null;
        }

        return cloudinaryInstance.url()
                .secure(true)
                .generate(publicId);
    }

    /**
     * Genere une URL avec transformation (redimensionnement)
     * CORRECTION: Utilisation de new Transformation() au lieu de ObjectUtils.asMap()
     */
    public static String generateUrlWithTransformation(String publicId, int width, int height) {
        if (publicId == null || publicId.trim().isEmpty()) {
            return null;
        }

        return cloudinaryInstance.url()
                .transformation(new Transformation()
                        .width(width)
                        .height(height)
                        .crop("fill"))
                .secure(true)
                .generate(publicId);
    }

    /**
     * Genere une URL miniature (150x150)
     */
    public static String generateThumbnailUrl(String publicId) {
        return generateUrlWithTransformation(publicId, 150, 150);
    }

    /**
     * Obtient les informations de configuration (pour debug)
     */
    public static Map<String, Object> getConfigInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("cloud_name", AppConfig.getCloudinaryCloudName());
        info.put("api_key_masked", AppConfig.getCloudinaryApiKey().substring(0, 4) + "****");
        info.put("secure", true);
        info.put("articles_folder", AppConfig.getCloudinaryArticlesFolder());
        return info;
    }

    /**
     * Empeche l'instantiation
     */
    private CloudinaryConfig() {
        throw new UnsupportedOperationException("Classe utilitaire - pas d'instantiation");
    }
}