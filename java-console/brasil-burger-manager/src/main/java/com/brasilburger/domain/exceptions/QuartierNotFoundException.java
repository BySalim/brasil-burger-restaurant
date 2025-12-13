package com.brasilburger.domain.exceptions;

/**
 * Exception levee lorsqu'un quartier est introuvable
 */
public class QuartierNotFoundException extends RuntimeException {

    public QuartierNotFoundException(String message) {
        super(message);
    }

    public QuartierNotFoundException(Long id) {
        super("Quartier introuvable avec l'ID : " + id);
    }

    public QuartierNotFoundException(String champ, String valeur) {
        super("Quartier introuvable avec " + champ + " :  " + valeur);
    }
}