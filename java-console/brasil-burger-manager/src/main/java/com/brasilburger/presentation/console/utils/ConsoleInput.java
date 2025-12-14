package com.brasilburger.presentation.console.utils;

import java.util.Scanner;

/**
 * Utilitaire pour la lecture des entrées console
 * Gère les validations et les conversions
 */
public class ConsoleInput {

    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Lit une chaîne de caractères
     */
    public static String lireTexte(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    /**
     * Lit une chaîne non vide
     */
    public static String lireTexteNonVide(String prompt) {
        String texte;
        do {
            texte = lireTexte(prompt);
            if (texte.isEmpty()) {
                System.out.println("  ⚠ Ce champ ne peut pas être vide");
            }
        } while (texte.isEmpty());
        return texte;
    }

    /**
     * Lit un entier
     */
    public static int lireEntier(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("  ⚠ Veuillez entrer un nombre entier valide");
            }
        }
    }

    /**
     * Lit un entier avec validation de plage
     */
    public static int lireEntier(String prompt, int min, int max) {
        while (true) {
            int valeur = lireEntier(prompt);
            if (valeur >= min && valeur <= max) {
                return valeur;
            }
            System.out.println("  ⚠ La valeur doit être entre " + min + " et " + max);
        }
    }

    /**
     * Lit un entier positif
     */
    public static int lireEntierPositif(String prompt) {
        while (true) {
            int valeur = lireEntier(prompt);
            if (valeur > 0) {
                return valeur;
            }
            System.out.println("  ⚠ La valeur doit être positive");
        }
    }

    /**
     * Lit un long
     */
    public static long lireLong(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                String input = scanner.nextLine().trim();
                return Long.parseLong(input);
            } catch (NumberFormatException e) {
                System.out.println("  ⚠ Veuillez entrer un nombre valide");
            }
        }
    }

    /**
     * Lit une confirmation (oui/non)
     */
    public static boolean lireConfirmation(String prompt) {
        String reponse = lireTexte(prompt + " (o/n): ").toLowerCase();
        return reponse.equals("o") || reponse.equals("oui");
    }

    /**
     * Lit un choix dans un menu
     */
    public static int lireChoixMenu(int nbOptions) {
        return lireEntier("\nVotre choix: ", 0, nbOptions);
    }

    /**
     * Attend que l'utilisateur appuie sur Entrée
     */
    public static void attendreEntree() {
        System.out.print("\nAppuyez sur Entrée pour continuer...");
        scanner.nextLine();
    }

    /**
     * Lit un texte optionnel (peut être vide)
     */
    public static String lireTexteOptional(String prompt) {
        return lireTexte(prompt);
    }

    /**
     * Ferme le scanner (à appeler à la fin du programme)
     */
    public static void fermer() {
        scanner.close();
    }

    /**
     * Empêche l'instantiation
     */
    private ConsoleInput() {
        throw new UnsupportedOperationException("Classe utilitaire - pas d'instantiation");
    }
}