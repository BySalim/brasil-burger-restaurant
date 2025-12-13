package com.brasilburger.domain.entities.enums;

/**
 * Modes de paiement disponibles
 */
public enum ModePaiement {
    WAVE("Wave"),
    OM("Orange Money");

    private final String libelle;

    ModePaiement(String libelle) {
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