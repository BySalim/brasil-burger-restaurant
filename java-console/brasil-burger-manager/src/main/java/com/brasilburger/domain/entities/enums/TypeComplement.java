package com.brasilburger.domain.entities.enums;

/**
 * Types de complements disponibles
 */
public enum TypeComplement {
    BOISSON("Boisson"),
    FRITES("Frites");

    private final String libelle;

    TypeComplement(String libelle) {
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