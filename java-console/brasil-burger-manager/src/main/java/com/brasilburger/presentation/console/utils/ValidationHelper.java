package com.brasilburger.presentation.console.utils;

import java.util.regex.Pattern;

/**
 * Utilitaire pour les validations courantes
 */
public class ValidationHelper {

    // Patterns de validation
    private static final Pattern TELEPHONE_PATTERN = Pattern.compile("^[0-9]{9}$|^\\+?[0-9]{10,15}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern CODE_ARTICLE_PATTERN = Pattern.compile("^(BRG|MNU|CMP)\\d{3}$");

    /**
     * Valide un numéro de téléphone
     * Format accepté: 9 chiffres (ex: 771234567) ou format international
     */
    public static boolean estTelephoneValide(String telephone) {
        if (telephone == null || telephone.trim().isEmpty()) {
            return false;
        }
        return TELEPHONE_PATTERN.matcher(telephone. trim()).matches();
    }

    /**
     * Valide une adresse email
     */
    public static boolean estEmailValide(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Valide un code article
     * Format:  BRG001, MNU001, CMP001
     */
    public static boolean estCodeArticleValide(String code) {
        if (code == null || code.trim().isEmpty()) {
            return false;
        }
        return CODE_ARTICLE_PATTERN.matcher(code.trim()).matches();
    }

    /**
     * Valide qu'une chaîne n'est pas vide
     */
    public static boolean estNonVide(String texte) {
        return texte != null && !texte.trim().isEmpty();
    }

    /**
     * Valide qu'un nombre est positif
     */
    public static boolean estPositif(Integer nombre) {
        return nombre != null && nombre > 0;
    }

    /**
     * Valide qu'un nombre est dans une plage
     */
    public static boolean estDansPlage(Integer nombre, int min, int max) {
        return nombre != null && nombre >= min && nombre <= max;
    }

    /**
     * Valide la longueur d'une chaîne
     */
    public static boolean estLongueurValide(String texte, int min, int max) {
        if (texte == null) {
            return false;
        }
        int longueur = texte.trim().length();
        return longueur >= min && longueur <= max;
    }

    /**
     * Valide un prix (doit être positif et raisonnable)
     */
    public static boolean estPrixValide(Integer prix) {
        return prix != null && prix > 0 && prix <= 1000000; // Max 1 million FCFA
    }

    /**
     * Valide un format d'image
     */
    public static boolean estFormatImageValide(String nomFichier) {
        if (nomFichier == null || nomFichier.trim().isEmpty()) {
            return false;
        }
        String nom = nomFichier.toLowerCase();
        return nom.endsWith(". jpg") || nom.endsWith(".jpeg") ||
                nom.endsWith(".png") || nom.endsWith(".webp") ||
                nom.endsWith(". gif");
    }

    /**
     * Nettoie un numéro de téléphone (enlève espaces et caractères spéciaux)
     */
    public static String nettoyerTelephone(String telephone) {
        if (telephone == null) {
            return null;
        }
        return telephone.replaceAll("[^0-9+]", "");
    }

    /**
     * Valide un ID (doit être positif et non null)
     */
    public static boolean estIdValide(Long id) {
        return id != null && id > 0;
    }

    /**
     * Retourne un message d'erreur pour un téléphone invalide
     */
    public static String getMessageErreurTelephone() {
        return "Format de téléphone invalide. Utilisez 9 chiffres (ex: 771234567)";
    }

    /**
     * Retourne un message d'erreur pour un email invalide
     */
    public static String getMessageErreurEmail() {
        return "Format d'email invalide";
    }

    /**
     * Retourne un message d'erreur pour un prix invalide
     */
    public static String getMessageErreurPrix() {
        return "Le prix doit être positif et inférieur à 1 000 000 FCFA";
    }

    /**
     * Empêche l'instantiation
     */
    private ValidationHelper() {
        throw new UnsupportedOperationException("Classe utilitaire - pas d'instantiation");
    }
}