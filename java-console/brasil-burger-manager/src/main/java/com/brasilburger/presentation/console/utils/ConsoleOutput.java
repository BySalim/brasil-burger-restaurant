package com.brasilburger.presentation.console.utils;

/**
 * Utilitaire pour l'affichage console formaté
 * Gère les couleurs et les styles (ANSI)
 */
public class ConsoleOutput {

    // Codes couleurs ANSI
    public static final String RESET = "\u001B[0m";
    public static final String NOIR = "\u001B[30m";
    public static final String ROUGE = "\u001B[31m";
    public static final String VERT = "\u001B[32m";
    public static final String JAUNE = "\u001B[33m";
    public static final String BLEU = "\u001B[34m";
    public static final String MAGENTA = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String BLANC = "\u001B[37m";

    // Styles
    public static final String GRAS = "\u001B[1m";
    public static final String SOULIGNE = "\u001B[4m";

    /**
     * Affiche un message de succès
     */
    public static void succes(String message) {
        System.out.println(VERT + "✓ " + message + RESET);
    }

    /**
     * Affiche un message d'erreur
     */
    public static void erreur(String message) {
        System.out.println(ROUGE + "✗ " + message + RESET);
    }

    /**
     * Affiche un avertissement
     */
    public static void avertissement(String message) {
        System.out.println(JAUNE + "⚠ " + message + RESET);
    }

    /**
     * Affiche une information
     */
    public static void info(String message) {
        System.out.println(CYAN + "ℹ " + message + RESET);
    }

    /**
     * Affiche un titre
     */
    public static void titre(String titre) {
        System.out.println();
        System.out.println(GRAS + BLEU + "=== " + titre + " ===" + RESET);
        System.out.println();
    }

    /**
     * Affiche un sous-titre
     */
    public static void sousTitre(String sousTitre) {
        System.out.println();
        System.out.println(CYAN + "--- " + sousTitre + " ---" + RESET);
    }

    /**
     * Affiche une ligne de séparation
     */
    public static void separateur() {
        System.out.println("─".repeat(60));
    }

    /**
     * Affiche une ligne de séparation épaisse
     */
    public static void separateurEpais() {
        System.out.println("═".repeat(60));
    }

    /**
     * Affiche un message coloré
     */
    public static void afficherCouleur(String message, String couleur) {
        System.out.println(couleur + message + RESET);
    }

    /**
     * Affiche un message en gras
     */
    public static void afficherGras(String message) {
        System.out.println(GRAS + message + RESET);
    }

    /**
     * Vide la console (cross-platform)
     */
    public static void viderConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // Si le vidage échoue, afficher juste des lignes vides
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }

    /**
     * Affiche un message d'en-tête de menu
     */
    public static void afficherMenuHeader(String titre) {
        separateurEpais();
        System.out.println(GRAS + BLEU + centre(titre, 60) + RESET);
        separateurEpais();
    }

    /**
     * Centre un texte dans une largeur donnée
     */
    private static String centre(String texte, int largeur) {
        if (texte.length() >= largeur) {
            return texte;
        }
        int espacesGauche = (largeur - texte.length()) / 2;
        return " ".repeat(espacesGauche) + texte;
    }

    /**
     * Affiche un message de chargement
     */
    public static void chargement(String message) {
        System.out.print(JAUNE + "⏳ " + message + "..." + RESET);
    }

    /**
     * Efface la ligne courante
     */
    public static void effacerLigne() {
        System.out.print("\r" + " ".repeat(80) + "\r");
    }

    /**
     * Empêche l'instantiation
     */
    private ConsoleOutput() {
        throw new UnsupportedOperationException("Classe utilitaire - pas d'instantiation");
    }
}