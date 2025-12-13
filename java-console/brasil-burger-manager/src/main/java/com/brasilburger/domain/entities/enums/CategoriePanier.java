package com.brasilburger.domain.entities.enums;

/**
 * Categories de panier
 */
public enum CategoriePanier {
    BURGER("Burger simple"),
    MENU("Menu");

    private final String libelle;

    CategoriePanier(String libelle) {
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