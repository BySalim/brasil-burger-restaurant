package com.brasilburger.domain.valueobjects;

import java.util.Objects;

/**
 * Value Object representant un prix en FCFA
 */
public class Prix {
    private final int montant;

    /**
     * Constructeur avec validation
     * @param montant Montant en FCFA (doit etre >= 0)
     * @throws IllegalArgumentException si le montant est negatif
     */
    public Prix(int montant) {
        if (montant < 0) {
            throw new IllegalArgumentException("Le prix ne peut pas etre negatif :  " + montant);
        }
        this.montant = montant;
    }

    /**
     * Cree un prix nul (0 FCFA)
     */
    public static Prix zero() {
        return new Prix(0);
    }

    /**
     * Verifie si le prix est nul
     */
    public boolean estNul() {
        return montant == 0;
    }

    /**
     * Verifie si le prix est positif
     */
    public boolean estPositif() {
        return montant > 0;
    }

    /**
     * Additionne deux prix
     */
    public Prix ajouter(Prix autre) {
        if (autre == null) {
            return this;
        }
        return new Prix(this.montant + autre.montant);
    }

    /**
     * Soustrait un prix
     */
    public Prix soustraire(Prix autre) {
        if (autre == null) {
            return this;
        }
        int nouveauMontant = this.montant - autre.montant;
        if (nouveauMontant < 0) {
            throw new IllegalArgumentException("Le resultat de la soustraction ne peut pas etre negatif");
        }
        return new Prix(nouveauMontant);
    }

    /**
     * Multiplie le prix par une quantite
     */
    public Prix multiplier(int quantite) {
        if (quantite < 0) {
            throw new IllegalArgumentException("La quantite ne peut pas etre negative");
        }
        return new Prix(this.montant * quantite);
    }

    /**
     * Compare deux prix
     */
    public boolean estSuperieurA(Prix autre) {
        return autre != null && this.montant > autre.montant;
    }

    /**
     * Compare deux prix
     */
    public boolean estInferieurA(Prix autre) {
        return autre != null && this.montant < autre.montant;
    }

    public int getMontant() {
        return montant;
    }

    /**
     * Formate le prix en FCFA
     */
    public String formater() {
        return String.format("%,d FCFA", montant);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Prix prix = (Prix) o;
        return montant == prix.montant;
    }

    @Override
    public int hashCode() {
        return Objects.hash(montant);
    }

    @Override
    public String toString() {
        return formater();
    }
}