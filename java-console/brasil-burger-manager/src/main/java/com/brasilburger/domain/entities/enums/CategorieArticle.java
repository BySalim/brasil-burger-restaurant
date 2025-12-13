package com.brasilburger.domain.entities.enums;

/**
 * Categories d'articles disponibles
 */
public enum CategorieArticle {
    BURGER("Burger"),
    MENU("Menu"),
    COMPLEMENT("Complement");

    private final String libelle;

    CategorieArticle(String libelle) {
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