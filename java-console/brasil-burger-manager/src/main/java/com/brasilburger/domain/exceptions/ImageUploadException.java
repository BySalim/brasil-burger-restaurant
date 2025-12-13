package com.brasilburger.domain.exceptions;

/**
 * Exception levee lors d'une erreur d'upload d'image
 */
public class ImageUploadException extends RuntimeException {

    public ImageUploadException(String message) {
        super(message);
    }

    public ImageUploadException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImageUploadException(String nomFichier, String raison) {
        super("Impossible d'uploader l'image '" + nomFichier + "' : " + raison);
    }
}