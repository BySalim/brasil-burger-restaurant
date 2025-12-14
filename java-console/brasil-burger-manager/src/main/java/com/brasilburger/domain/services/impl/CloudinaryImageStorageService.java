package com.brasilburger.domain.services.impl;

import com.brasilburger.domain.exceptions.ImageUploadException;
import com.brasilburger.domain.services.IImageStorageService;
import com.brasilburger.domain.valueobjects.ImageInfo;
import com.brasilburger.infrastructure.cloudinary.CloudinaryConfig;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Implementation du service de stockage d'images avec Cloudinary
 */
public class CloudinaryImageStorageService implements IImageStorageService {
    private static final Logger logger = LoggerFactory.getLogger(CloudinaryImageStorageService.class);
    private final Cloudinary cloudinary;

    public CloudinaryImageStorageService() {
        this.cloudinary = CloudinaryConfig.getInstance();
    }

    /**
     * Valide et nettoie un chemin de fichier
     * - Supprime les guillemets
     * - Valide l'existence
     * - Valide le format
     */
    public File validerEtObtenirFichier(String cheminSaisi) {
        if (cheminSaisi == null || cheminSaisi.trim().isEmpty()) {
            throw new IllegalArgumentException("Le chemin du fichier ne peut pas etre vide");
        }

        // Nettoyer le chemin
        String cheminNettoye = cheminSaisi.trim();

        // Supprimer les guillemets doubles
        if (cheminNettoye.startsWith("\"") && cheminNettoye.endsWith("\"")) {
            cheminNettoye = cheminNettoye.substring(1, cheminNettoye.length() - 1);
        }

        // Supprimer les guillemets simples
        if (cheminNettoye.startsWith("'") && cheminNettoye.endsWith("'")) {
            cheminNettoye = cheminNettoye.substring(1, cheminNettoye.length() - 1);
        }

        cheminNettoye = cheminNettoye.trim();

        // Créer le fichier
        File fichier = new File(cheminNettoye);

        // Valider l'existence
        if (!fichier.exists()) {
            throw new IllegalArgumentException("Le fichier n'existe pas:  " + cheminNettoye);
        }

        if (!fichier.isFile()) {
            throw new IllegalArgumentException("Le chemin ne pointe pas vers un fichier: " + cheminNettoye);
        }

        if (!fichier.canRead()) {
            throw new IllegalArgumentException("Le fichier n'est pas accessible en lecture: " + cheminNettoye);
        }

        // Valider le format
        if (!estFormatImageValide(fichier.getName())) {
            throw new IllegalArgumentException(
                    "Format non supporte: " + fichier.getName() +
                            ".Formats acceptes: jpg, jpeg, png, webp, gif"
            );
        }

        return fichier;
    }

    /**
     * Verifie si le format de l'image est valide
     */
    public boolean estFormatImageValide(String nomFichier) {
        if (nomFichier == null) {
            return false;
        }

        String nomLower = nomFichier.toLowerCase();
        return nomLower.endsWith(".jpg") ||
                nomLower.endsWith(".jpeg") ||
                nomLower.endsWith(".png") ||
                nomLower.endsWith(".webp") ||
                nomLower.endsWith(".gif");
    }

    @Override
    public ImageInfo uploadImage(File file, String folder) {
        if (file == null || !file.exists()) {
            throw new IllegalArgumentException("Le fichier n'existe pas");
        }

        if (!file.canRead()) {
            throw new IllegalArgumentException("Le fichier n'est pas accessible en lecture");
        }

        try {
            // Options d'upload
            Map<String, Object> options = ObjectUtils.asMap(
                    "folder", folder,
                    "resource_type", "image",
                    "use_filename", true,
                    "unique_filename", true,
                    "overwrite", false
            );

            // Upload
            Map uploadResult = cloudinary.uploader().upload(file, options);

            // Extraire les informations
            String publicId = (String) uploadResult.get("public_id");
            String url = (String) uploadResult.get("secure_url");
            String format = (String) uploadResult.get("format");

            logger.info("Image uploadee avec succes: public_id={}, url={}", publicId, url);

            return new ImageInfo(publicId, url, format);

        } catch (Exception e) {
            logger.error("Erreur lors de l'upload de l'image:  {}", file.getName(), e);
            throw new ImageUploadException(file.getName(), e.getMessage());
        }
    }

    @Override
    public ImageInfo uploadImage(String filePath, String folder) {
        File file = validerEtObtenirFichier(filePath);
        return uploadImage(file, folder);
    }

    @Override
    public boolean deleteImage(String publicId) {
        if (publicId == null || publicId.trim().isEmpty()) {
            throw new IllegalArgumentException("Le publicId ne peut pas etre vide");
        }

        try {
            Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            String resultStatus = (String) result.get("result");

            boolean success = "ok".equals(resultStatus);

            if (success) {
                logger.info("Image supprimee avec succes: public_id={}", publicId);
            } else {
                logger.warn("Echec de la suppression de l'image: public_id={}, status={}",
                        publicId, resultStatus);
            }

            return success;

        } catch (Exception e) {
            logger.error("Erreur lors de la suppression de l'image: {}", publicId, e);
            return false;
        }
    }

    @Override
    public String getImageUrl(String publicId) {
        return CloudinaryConfig.generateUrl(publicId);
    }

    @Override
    public String getImageUrl(String publicId, int width, int height) {
        return CloudinaryConfig.generateUrlWithTransformation(publicId, width, height);
    }

    @Override
    public String getThumbnailUrl(String publicId) {
        return CloudinaryConfig.generateThumbnailUrl(publicId);
    }

    @Override
    public List<ImageInfo> listImages(String folder) {
        List<ImageInfo> images = new ArrayList<>();

        try {
            Map options = ObjectUtils.asMap(
                    "type", "upload",
                    "prefix", folder,
                    "max_results", 100
            );

            Map result = cloudinary.api().resources(options);
            List<Map> resources = (List<Map>) result.get("resources");

            for (Map resource : resources) {
                String publicId = (String) resource.get("public_id");
                String url = (String) resource.get("secure_url");
                String format = (String) resource.get("format");

                images.add(new ImageInfo(publicId, url, format));
            }

            logger.debug("Nombre d'images trouvees dans le dossier {}: {}", folder, images.size());

        } catch (Exception e) {
            logger.error("Erreur lors du listage des images du dossier:  {}", folder, e);
        }

        return images;
    }

    @Override
    public boolean imageExists(String publicId) {
        if (publicId == null || publicId.trim().isEmpty()) {
            return false;
        }

        try {
            Map result = cloudinary.api().resource(publicId, ObjectUtils.emptyMap());
            return result != null && result.containsKey("public_id");

        } catch (Exception e) {
            logger.debug("Image non trouvee: {}", publicId);
            return false;
        }
    }
}