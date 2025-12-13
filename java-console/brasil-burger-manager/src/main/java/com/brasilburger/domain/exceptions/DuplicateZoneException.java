package com.brasilburger.domain.exceptions;

/**
 * Exception levee lorsqu'une zone avec le meme nom existe deja
 */
public class DuplicateZoneException extends RuntimeException {

    public DuplicateZoneException(String nom) {
        super("Une zone avec le nom '" + nom + "' existe deja");
    }

    public DuplicateZoneException(String message, String nom) {
        super(message + " :  " + nom);
    }
}