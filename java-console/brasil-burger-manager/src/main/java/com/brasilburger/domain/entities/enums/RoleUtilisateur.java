package com.brasilburger.domain.entities.enums;

/**
 * Roles des utilisateurs dans le systeme
 */
public enum RoleUtilisateur {
    CLIENT("Client"),
    GESTIONNAIRE("Gestionnaire");

    private final String libelle;

    RoleUtilisateur(String libelle) {
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