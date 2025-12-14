package com.brasilburger.presentation.validators;

import com.brasilburger.domain.entities.Livreur;
import com.brasilburger.presentation.console.utils.ValidationHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Validateur pour l'entité Livreur
 * Centralise toutes les règles de validation métier pour les livreurs
 */
public class LivreurValidator {

    /**
     * Valide un livreur complet
     * @param livreur Livreur à valider
     * @return Liste des erreurs de validation (vide si valide)
     */
    public static List<String> valider(Livreur livreur) {
        List<String> erreurs = new ArrayList<>();

        if (livreur == null) {
            erreurs.add("Le livreur ne peut pas être null");
            return erreurs;
        }

        // Validation du nom
        erreurs.addAll(validerNom(livreur.getNom()));

        // Validation du prénom
        erreurs.addAll(validerPrenom(livreur.getPrenom()));

        // Validation du téléphone
        erreurs.addAll(validerTelephone(livreur.getTelephone()));

        return erreurs;
    }

    /**
     * Valide le nom d'un livreur
     */
    public static List<String> validerNom(String nom) {
        List<String> erreurs = new ArrayList<>();

        if (nom == null || nom.trim().isEmpty()) {
            erreurs.add("Le nom du livreur ne peut pas être vide");
            return erreurs;
        }

        String nomTrim = nom.trim();

        if (nomTrim.length() < 2) {
            erreurs.add("Le nom du livreur doit contenir au moins 2 caractères");
        }

        if (nomTrim.length() > 50) {
            erreurs.add("Le nom du livreur ne peut pas dépasser 50 caractères");
        }

        // Validation des caractères (lettres, espaces, tirets)
        if (!nomTrim.matches("^[a-zA-ZÀ-ÿ\\s\\-']+$")) {
            erreurs.add("Le nom du livreur contient des caractères invalides");
        }

        return erreurs;
    }

    /**
     * Valide le prénom d'un livreur
     */
    public static List<String> validerPrenom(String prenom) {
        List<String> erreurs = new ArrayList<>();

        if (prenom == null || prenom.trim().isEmpty()) {
            erreurs.add("Le prénom du livreur ne peut pas être vide");
            return erreurs;
        }

        String prenomTrim = prenom.trim();

        if (prenomTrim.length() < 2) {
            erreurs.add("Le prénom du livreur doit contenir au moins 2 caractères");
        }

        if (prenomTrim.length() > 50) {
            erreurs.add("Le prénom du livreur ne peut pas dépasser 50 caractères");
        }

        // Validation des caractères (lettres, espaces, tirets)
        if (!prenomTrim.matches("^[a-zA-ZÀ-ÿ\\s\\-']+$")) {
            erreurs.add("Le prénom du livreur contient des caractères invalides");
        }

        return erreurs;
    }

    /**
     * Valide le téléphone d'un livreur
     */
    public static List<String> validerTelephone(String telephone) {
        List<String> erreurs = new ArrayList<>();

        if (telephone == null || telephone.trim().isEmpty()) {
            erreurs.add("Le téléphone du livreur ne peut pas être vide");
            return erreurs;
        }

        String telephoneTrim = telephone.trim();

        if (! ValidationHelper.estTelephoneValide(telephoneTrim)) {
            erreurs.add(ValidationHelper.getMessageErreurTelephone());
        }

        return erreurs;
    }

    /**
     * Vérifie si un livreur est valide
     */
    public static boolean estValide(Livreur livreur) {
        return valider(livreur).isEmpty();
    }

    /**
     * Lance une exception si le livreur est invalide
     */
    public static void validerOuLancerException(Livreur livreur) {
        List<String> erreurs = valider(livreur);
        if (!erreurs.isEmpty()) {
            throw new IllegalArgumentException("Livreur invalide: " + String.join(", ", erreurs));
        }
    }

    /**
     * Empêche l'instantiation
     */
    private LivreurValidator() {
        throw new UnsupportedOperationException("Classe utilitaire - pas d'instantiation");
    }
}