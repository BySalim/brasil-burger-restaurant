package com.brasilburger.domain.entities;

import java.util.Objects;

/**
 * Entite Livreur
 * Represente un livreur avec sa disponibilite
 */
public class Livreur {
    private Long id;
    private String nom;
    private String prenom;
    private String telephone;
    private boolean estArchiver;
    private boolean estDisponible;

    /**
     * Constructeur par defaut
     */
    public Livreur() {
        this.estArchiver = false;
        this.estDisponible = true;
    }

    /**
     * Constructeur avec parametres
     */
    public Livreur(String nom, String prenom, String telephone) {
        this();
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
    }

    /**
     * Constructeur complet
     */
    public Livreur(Long id, String nom, String prenom, String telephone,
                   boolean estArchiver, boolean estDisponible) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.estArchiver = estArchiver;
        this.estDisponible = estDisponible;
    }

    // ===================================
    // Methodes metier
    // ===================================

    /**
     * Archive le livreur
     * Un livreur archive n'est plus disponible
     */
    public void archiver() {
        this.estArchiver = true;
        this.estDisponible = false;
    }

    /**
     * Restaure le livreur
     */
    public void restaurer() {
        this.estArchiver = false;
        this.estDisponible = true;
    }

    /**
     * Marque le livreur comme disponible
     */
    public void marquerDisponible() {
        if (this.estArchiver) {
            throw new IllegalStateException("Un livreur archive ne peut pas etre marque disponible");
        }
        this.estDisponible = true;
    }

    /**
     * Marque le livreur comme occupe
     */
    public void marquerOccupe() {
        this.estDisponible = false;
    }

    /**
     * Verifie si le livreur peut etre affecte a une livraison
     */
    public boolean peutEtreAffecte() {
        return !this.estArchiver && this.estDisponible;
    }

    /**
     * Obtient le nom complet du livreur
     */
    public String getNomComplet() {
        return prenom + " " + nom;
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

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public boolean isEstArchiver() {
        return estArchiver;
    }

    public void setEstArchiver(boolean estArchiver) {
        this.estArchiver = estArchiver;
    }

    public boolean isEstDisponible() {
        return estDisponible;
    }

    public void setEstDisponible(boolean estDisponible) {
        this.estDisponible = estDisponible;
    }

    // ===================================
    // equals, hashCode, toString
    // ===================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Livreur livreur = (Livreur) o;
        return Objects.equals(id, livreur.id) && Objects.equals(telephone, livreur.telephone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, telephone);
    }

    @Override
    public String toString() {
        return "Livreur{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", telephone='" + telephone + '\'' +
                ", estArchiver=" + estArchiver +
                ", estDisponible=" + estDisponible +
                '}';
    }
}