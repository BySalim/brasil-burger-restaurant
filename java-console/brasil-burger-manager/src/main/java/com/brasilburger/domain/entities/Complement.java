package com.brasilburger.domain.entities;

import com.brasilburger.domain.entities.enums.CategorieArticle;
import com.brasilburger.domain.entities.enums.TypeComplement;

/**
 * Entite Complement
 */
public class Complement extends Article {
    private TypeComplement type;
    private Integer prix;  // Prix obligatoire pour un complement

    /**
     * Constructeur par defaut
     */
    public Complement() {
        super();
        this.setCategorie(CategorieArticle.COMPLEMENT);
    }

    /**
     * Constructeur avec parametres
     */
    public Complement(String code, String libelle, String imagePublicId, TypeComplement type, Integer prix) {
        super(code, libelle, imagePublicId);
        this.setCategorie(CategorieArticle.COMPLEMENT);
        this.type = type;
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
            throw new IllegalArgumentException("Un complement doit avoir un prix positif");
        }
    }

    /**
     * Verifie si c'est une boisson
     */
    public boolean estBoisson() {
        return type == TypeComplement.BOISSON;
    }

    /**
     * Verifie si ce sont des frites
     */
    public boolean estFrites() {
        return type == TypeComplement.FRITES;
    }

    // ===================================
    // Getters et Setters
    // ===================================

    public TypeComplement getType() {
        return type;
    }

    public void setType(TypeComplement type) {
        this.type = type;
    }

    public void setPrix(Integer prix) {
        validerPrix(prix);
        this.prix = prix;
    }
}