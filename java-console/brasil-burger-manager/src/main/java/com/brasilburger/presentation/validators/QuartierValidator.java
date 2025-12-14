package com.brasilburger.presentation.validators;

import com.brasilburger.domain.entities.Quartier;

import java.util.ArrayList;
import java.util.List;

/**
 * Validateur pour l'entité Quartier
 * Centralise toutes les règles de validation métier pour les quartiers
 */
public class QuartierValidator {

    /**
     * Valide un quartier complet
     * @param quartier Quartier à valider
     * @return Liste des erreurs de validation (vide si valide)
     */
    public static List<String> valider(Quartier quartier) {
        List<String> erreurs = new ArrayList<>();

        if (quartier == null) {
            erreurs.add("Le quartier ne peut pas être null");
            return erreurs;
        }

        // Validation du nom
        erreurs.addAll(validerNom(quartier.getNom()));

        // Validation de l'ID de zone
        erreurs.addAll(validerZoneId(quartier.getIdZone()));

        return erreurs;
    }

    /**
     * Valide le nom d'un quartier
     */
    public static List<String> validerNom(String nom) {
        List<String> erreurs = new ArrayList<>();

        if (nom == null || nom.trim().isEmpty()) {
            erreurs.add("Le nom du quartier ne peut pas être vide");
            return erreurs;
        }

        String nomTrim = nom. trim();

        if (nomTrim.length() < 2) {
            erreurs.add("Le nom du quartier doit contenir au moins 2 caractères");
        }

        if (nomTrim.length() > 100) {
            erreurs.add("Le nom du quartier ne peut pas dépasser 100 caractères");
        }

        // Validation des caractères (lettres, chiffres, espaces, tirets)
        if (!nomTrim.matches("^[a-zA-ZÀ-ÿ0-9\\s\\-']+$")) {
            erreurs.add("Le nom du quartier contient des caractères invalides");
        }

        return erreurs;
    }

    /**
     * Valide l'ID de zone
     */
    public static List<String> validerZoneId(Long zoneId) {
        List<String> erreurs = new ArrayList<>();

        if (zoneId == null) {
            erreurs.add("L'ID de la zone ne peut pas être null");
            return erreurs;
        }

        if (zoneId <= 0) {
            erreurs.add("L'ID de la zone doit être positif");
        }

        return erreurs;
    }

    /**
     * Vérifie si un quartier est valide
     */
    public static boolean estValide(Quartier quartier) {
        return valider(quartier).isEmpty();
    }

    /**
     * Lance une exception si le quartier est invalide
     */
    public static void validerOuLancerException(Quartier quartier) {
        List<String> erreurs = valider(quartier);
        if (!erreurs.isEmpty()) {
            throw new IllegalArgumentException("Quartier invalide: " + String. join(", ", erreurs));
        }
    }

    /**
     * Empêche l'instantiation
     */
    private QuartierValidator() {
        throw new UnsupportedOperationException("Classe utilitaire - pas d'instantiation");
    }
}