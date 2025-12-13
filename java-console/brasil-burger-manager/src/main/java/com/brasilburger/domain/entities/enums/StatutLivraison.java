package com.brasilburger.domain.entities.enums;

/**
 * Statuts possibles d'une livraison
 */
public enum StatutLivraison {
    EN_COURS("En cours"),
    TERMINER("Termine");

    private final String libelle;

    StatutLivraison(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }

    @Override
    public String toString() {
        return libelle;
    }
}