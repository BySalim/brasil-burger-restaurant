package com.brasilburger.domain.entities;

import java.util.Objects;

/**
 * Entite Quartier
 * Represente un quartier appartenant a une zone
 */
public class Quartier {
    private Long id;
    private String nom;
    private Long idZone;

    /**
     * Constructeur par defaut
     */
    public Quartier() {
    }

    /**
     * Constructeur avec parametres
     */
    public Quartier(String nom, Long idZone) {
        this.nom = nom;
        this.idZone = idZone;
    }

    /**
     * Constructeur complet
     */
    public Quartier(Long id, String nom, Long idZone) {
        this.id = id;
        this.nom = nom;
        this.idZone = idZone;
    }

    // ===================================
    // Methodes metier
    // ===================================

    /**
     * Verifie si le quartier appartient a une zone donnee
     */
    public boolean appartientAZone(Long zoneId) {
        return this.idZone != null && this.idZone.equals(zoneId);
    }

    /**
     * Change la zone du quartier
     */
    public void changerZone(Long nouvelleZoneId) {
        if (nouvelleZoneId == null) {
            throw new IllegalArgumentException("L'ID de la zone ne peut pas etre null");
        }
        this.idZone = nouvelleZoneId;
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

    public Long getIdZone() {
        return idZone;
    }

    public void setIdZone(Long idZone) {
        this.idZone = idZone;
    }

    // ===================================
    // equals, hashCode, toString
    // ===================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quartier quartier = (Quartier) o;
        return Objects.equals(id, quartier.id) && Objects.equals(nom, quartier.nom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nom);
    }

    @Override
    public String toString() {
        return "Quartier{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", idZone=" + idZone +
                '}';
    }
}