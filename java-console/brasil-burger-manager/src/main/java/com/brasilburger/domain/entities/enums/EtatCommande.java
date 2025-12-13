package com.brasilburger.domain.entities.enums;

/**
 * Etats possibles d'une commande
 */
public enum EtatCommande {
    EN_ATTENTE("En attente"),
    EN_PREPARATION("En preparation"),
    TERMINER("Termine"),
    ANNULER("Annule");

    private final String libelle;

    EtatCommande(String libelle) {
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