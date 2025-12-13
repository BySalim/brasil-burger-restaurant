package com.brasilburger.domain.entities.enums;

/**
 * Categories pour ArticleQuantifier
 */
public enum CategorieArticleQuantifier {
    MENU("Menu"),
    COMMANDE("Commande");

    private final String libelle;

    CategorieArticleQuantifier(String libelle) {
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