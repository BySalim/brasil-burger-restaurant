package com.brasilburger.domain.entities;

import java.util.Objects;

/**
 * Entite Zone
 */
public class Zone {
    private Long id;
    private String nom;
    private Integer prixLivraison;
    private boolean estArchiver;

    /**
     * Constructeur par defaut
     */
    public Zone() {
        this.estArchiver = false;
    }

    /**
     * Constructeur avec parametres
     */
    public Zone(String nom, Integer prixLivraison) {
        this();
        this.nom = nom;
        this.prixLivraison = prixLivraison;
    }

    /**
     * Constructeur complet
     */
    public Zone(Long id, String nom, Integer prixLivraison, boolean estArchiver) {
        this.id = id;
        this.nom = nom;
        this.prixLivraison = prixLivraison;
        this.estArchiver = estArchiver;
    }

    // ===================================
    // Methodes metier
    // ===================================

    /**
     * Archive la zone
     */
    public void archiver() {
        this.estArchiver = true;
    }

    /**
     * Restaure la zone
     */
    public void restaurer() {
        this.estArchiver = false;
    }

    /**
     * Modifie le prix de livraison
     */
    public void modifierPrixLivraison(Integer nouveauPrix) {
        if (nouveauPrix == null || nouveauPrix < 0) {
            throw new IllegalArgumentException("Le prix de livraison doit etre positif ou nul");
        }
        this.prixLivraison = nouveauPrix;
    }

    /**
     * Modifie le nom de la zone
     */
    public void modifierNom(String nouveauNom) {
        if (nouveauNom == null || nouveauNom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom ne peut pas etre vide");
        }
        this.nom = nouveauNom.trim();
    }

    /**
     * Verifie si la zone est active (non archivee)
     */
    public boolean estActive() {
        return !estArchiver;
    }

    // ===================================
    // Getters et Setters
    // ===================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Integer getPrixLivraison() {
        return prixLivraison;
    }

    public void setPrixLivraison(Integer prixLivraison) {
        this.prixLivraison = prixLivraison;
    }

    public boolean isEstArchiver() {
        return estArchiver;
    }

    public void setEstArchiver(boolean estArchiver) {
        this.estArchiver = estArchiver;
    }

    // ===================================
    // equals, hashCode, toString
    // ===================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Zone zone = (Zone) o;
        return Objects.equals(id, zone.id) && Objects.equals(nom, zone.nom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nom);
    }

    @Override
    public String toString() {
        return "Zone{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prixLivraison=" + prixLivraison +
                ", estArchiver=" + estArchiver +
                '}';
    }
}