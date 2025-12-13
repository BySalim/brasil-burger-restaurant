package com.brasilburger.domain.entities.enums;

/**
 * Types de recuperation d'une commande
 */
public enum TypeRecuperation {
    SUR_PLACE("Sur place"),
    EMPORTER("A emporter"),
    LIVRER("Livraison");

    private final String libelle;

    TypeRecuperation(String libelle) {
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