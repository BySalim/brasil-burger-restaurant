package com.brasilburger.domain.exceptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Exception levee lors d'une erreur de validation
 */
public class ValidationException extends RuntimeException {
    private final List<String> erreurs;

    public ValidationException(String message) {
        super(message);
        this.erreurs = new ArrayList<>();
        this.erreurs.add(message);
    }

    public ValidationException(List<String> erreurs) {
        super("Erreurs de validation : " + String.join(", ", erreurs));
        this.erreurs = new ArrayList<>(erreurs);
    }

    public ValidationException(String champ, String messageErreur) {
        super("Erreur de validation sur le champ '" + champ + "' : " + messageErreur);
        this.erreurs = new ArrayList<>();
        this.erreurs.add(champ + " : " + messageErreur);
    }

    public List<String> getErreurs() {
        return new ArrayList<>(erreurs);
    }

    public boolean hasErreurs() {
        return !erreurs.isEmpty();
    }

    public int getNombreErreurs() {
        return erreurs.size();
    }
}