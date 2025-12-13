package com.brasilburger.domain.valueobjects;

import java.util.Objects;

/**
 * Value Object representant les informations d'une image stockee sur Cloudinary
 * Immuable
 */
public class ImageInfo {
    private final String publicId;
    private final String url;
    private final String format;

    /**
     * Constructeur complet
     * @param publicId Identifiant public Cloudinary (ex:   "brasil-burger/articles/burger001")
     * @param url URL complete de l'image
     * @param format Format de l'image (jpg, png, webp, etc.)
     */
    public ImageInfo(String publicId, String url, String format) {
        if (publicId == null || publicId.trim().isEmpty()) {
            throw new IllegalArgumentException("Le publicId ne peut pas etre vide");
        }
        if (url == null || url.trim().isEmpty()) {
            throw new IllegalArgumentException("L'URL ne peut pas etre vide");
        }

        this.publicId = publicId.trim();
        this.url = url.trim();
        this.format = (format != null) ? format.trim().toLowerCase() : "jpg";
    }

    /**
     * Constructeur simplifie (format par defaut :  jpg)
     */
    public ImageInfo(String publicId, String url) {
        this(publicId, url, "jpg");
    }

    /**
     * Verifie si le format est valide
     */
    public boolean estFormatValide() {
        String[] formatsValides = {"jpg", "jpeg", "png", "webp", "gif"};
        for (String formatValide : formatsValides) {
            if (formatValide.equalsIgnoreCase(this.format)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Genere une URL avec transformation (redimensionnement)
     * @param largeur Largeur souhaitee
     * @param hauteur Hauteur souhaitee
     */
    public String getUrlAvecTransformation(int largeur, int hauteur) {
        // Format Cloudinary:   https://res.cloudinary.com/cloud_name/image/upload/w_300,h_200/publicId
        String[] parts = url.split("/upload/");
        if (parts.length == 2) {
            return parts[0] + "/upload/w_" + largeur + ",h_" + hauteur + "/" + parts[1];
        }
        return url;
    }

    /**
     * Genere une URL en miniature (thumbnail)
     */
    public String getUrlMiniature() {
        return getUrlAvecTransformation(150, 150);
    }

    public String getPublicId() {
        return publicId;
    }

    public String getUrl() {
        return url;
    }

    public String getFormat() {
        return format;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageInfo imageInfo = (ImageInfo) o;
        return Objects.equals(publicId, imageInfo.publicId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(publicId);
    }

    @Override
    public String toString() {
        return "ImageInfo{" +
                "publicId='" + publicId + '\'' +
                ", url='" + url + '\'' +
                ", format='" + format + '\'' +
                '}';
    }
}