package com.brasilburger.domain.entities;

import com.brasilburger.domain.entities.enums.CategorieArticle;

/**
 * Entite Burger
 */
public class Burger extends Article {
    private String description;
    private Integer prix;

    /**
     * Constructeur par defaut
     */
    public Burger() {
        super();
        this.setCategorie(CategorieArticle.BURGER);
    }

    /**
     * Constructeur avec parametres
     */
    public Burger(String code, String libelle, String imagePublicId, String description, Integer prix) {
        super(code, libelle, imagePublicId);
        this.setCategorie(CategorieArticle.BURGER);
        this.description = description;
        this.setPrix(prix);
    }

    // ===================================
    // Implementation de la methode abstraite
    // ===================================

    @Override
    public Integer getPrix() {
        return prix;
    }

    // ===================================
    // Methodes metier
    // ===================================

    /**
     * Valide que le prix est positif
     */
    private void validerPrix(Integer prix) {
        if (prix == null || prix <= 0) {
            throw new IllegalArgumentException("Un burger doit avoir un prix positif");
        }
    }

    // ===================================
    // Getters et Setters
    // ===================================

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrix(Integer prix) {
        validerPrix(prix);
        this.prix = prix;
    }
}