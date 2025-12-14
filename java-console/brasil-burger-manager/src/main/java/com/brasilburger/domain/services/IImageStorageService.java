package com.brasilburger.domain.services;

import com.brasilburger.domain.valueobjects.ImageInfo;

import java.io.File;
import java.util.List;

/**
 * Interface du service de stockage d'images
 * Abstraction pour permettre differents providers (Cloudinary, S3, local, etc.)
 */
public interface IImageStorageService {

    /**
     * Upload une image depuis un fichier local
     * @param file Fichier image
     * @param folder Dossier de destination (ex: "burgers", "menus")
     * @return ImageInfo contenant public_id et URL
     */
    ImageInfo uploadImage(File file, String folder);

    /**
     * Upload une image depuis un chemin
     * @param filePath Chemin du fichier
     * @param folder Dossier de destination
     * @return ImageInfo contenant public_id et URL
     */
    ImageInfo uploadImage(String filePath, String folder);

    /**
     * Supprime une image par son public_id
     * @param publicId Identifiant public Cloudinary
     * @return true si suppression reussie
     */
    boolean deleteImage(String publicId);

    /**
     * Recupere l'URL d'une image
     * @param publicId Identifiant public
     * @return URL complete de l'image
     */
    String getImageUrl(String publicId);

    /**
     * Recupere l'URL d'une image avec transformation
     * @param publicId Identifiant public
     * @param width Largeur souhaitee
     * @param height Hauteur souhaitee
     * @return URL avec transformation
     */
    String getImageUrl(String publicId, int width, int height);

    /**
     * Recupere l'URL miniature d'une image
     * @param publicId Identifiant public
     * @return URL miniature (150x150)
     */
    String getThumbnailUrl(String publicId);

    /**
     * Liste toutes les images d'un dossier
     * @param folder Nom du dossier
     * @return Liste des ImageInfo
     */
    List<ImageInfo> listImages(String folder);

    /**
     * Verifie si une image existe
     * @param publicId Identifiant public
     * @return true si l'image existe
     */
    boolean imageExists(String publicId);
}