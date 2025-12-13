package com.brasilburger.domain.exceptions;

/**
 * Exception levee lorsqu'une zone est introuvable
 */
public class ZoneNotFoundException extends RuntimeException {

    public ZoneNotFoundException(String message) {
        super(message);
    }

    public ZoneNotFoundException(Long id) {
        super("Zone introuvable avec l'ID : " + id);
    }

    public ZoneNotFoundException(String champ, String valeur) {
        super("Zone introuvable avec " + champ + " : " + valeur);
    }
}