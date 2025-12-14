package com.brasilburger.presentation.validators;

import com.brasilburger.domain.entities.Zone;

import java.util.ArrayList;
import java.util.List;

/**
 * Validateur pour l'entité Zone
 * Centralise toutes les règles de validation métier pour les zones
 */
public class ZoneValidator {

    /**
     * Valide une zone complète
     * @param zone Zone à valider
     * @return Liste des erreurs de validation (vide si valide)
     */
    public static List<String> valider(Zone zone) {
        List<String> erreurs = new ArrayList<>();

        if (zone == null) {
            erreurs.add("La zone ne peut pas être null");
            return erreurs;
        }

        // Validation du nom
        erreurs.addAll(validerNom(zone.getNom()));

        // Validation du prix de livraison
        erreurs.addAll(validerPrixLivraison(zone.getPrixLivraison()));

        return erreurs;
    }

    /**
     * Valide le nom d'une zone
     */
    public static List<String> validerNom(String nom) {
        List<String> erreurs = new ArrayList<>();

        if (nom == null || nom.trim().isEmpty()) {
            erreurs.add("Le nom de la zone ne peut pas être vide");
            return erreurs;
        }

        String nomTrim = nom.trim();

        if (nomTrim.length() < 3) {
            erreurs.add("Le nom de la zone doit contenir au moins 3 caractères");
        }

        if (nomTrim.length() > 100) {
            erreurs.add("Le nom de la zone ne peut pas dépasser 100 caractères");
        }

        // Validation des caractères (lettres, chiffres, espaces, tirets)
        if (!nomTrim.matches("^[a-zA-ZÀ-ÿ0-9\\s\\-']+$")) {
            erreurs.add("Le nom de la zone contient des caractères invalides");
        }

        return erreurs;
    }

    /**
     * Valide le prix de livraison
     */
    public static List<String> validerPrixLivraison(Integer prix) {
        List<String> erreurs = new ArrayList<>();

        if (prix == null) {
            erreurs.add("Le prix de livraison ne peut pas être null");
            return erreurs;
        }

        if (prix < 0) {
            erreurs.add("Le prix de livraison ne peut pas être négatif");
        }

        if (prix > 100000) {
            erreurs.add("Le prix de livraison ne peut pas dépasser 100 000 FCFA");
        }

        return erreurs;
    }

    /**
     * Vérifie si une zone est valide
     */
    public static boolean estValide(Zone zone) {
        return valider(zone).isEmpty();
    }

    /**
     * Lance une exception si la zone est invalide
     */
    public static void validerOuLancerException(Zone zone) {
        List<String> erreurs = valider(zone);
        if (!erreurs.isEmpty()) {
            throw new IllegalArgumentException("Zone invalide: " + String.join(", ", erreurs));
        }
    }

    /**
     * Empêche l'instantiation
     */
    private ZoneValidator() {
        throw new UnsupportedOperationException("Classe utilitaire - pas d'instantiation");
    }
}