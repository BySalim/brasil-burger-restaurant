package com.brasilburger.domain.exceptions;

/**
 * Exception levee lorsqu'un livreur est introuvable
 */
public class LivreurNotFoundException extends RuntimeException {

    public LivreurNotFoundException(String message) {
        super(message);
    }

    public LivreurNotFoundException(Long id) {
        super("Livreur introuvable avec l'ID : " + id);
    }

    public LivreurNotFoundException(String champ, String valeur) {
        super("Livreur introuvable avec " + champ + " : " + valeur);
    }
}