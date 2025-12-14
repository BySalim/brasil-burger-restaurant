package com.brasilburger.presentation.console.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Utilitaire pour formater des données en tableau
 */
public class TableFormatter {

    private final List<String> headers;
    private final List<List<String>> rows;
    private final List<Integer> columnWidths;

    public TableFormatter(String...  headers) {
        this.headers = List.of(headers);
        this.rows = new ArrayList<>();
        this.columnWidths = new ArrayList<>();

        // Initialiser les largeurs avec les en-têtes
        for (String header : headers) {
            columnWidths.add(header.length());
        }
    }

    /**
     * Ajoute une ligne au tableau
     */
    public void ajouterLigne(String... valeurs) {
        if (valeurs.length != headers.size()) {
            throw new IllegalArgumentException(
                    "Nombre de colonnes incorrect:  attendu " + headers.size() + ", reçu " + valeurs.length
            );
        }

        List<String> ligne = new ArrayList<>();
        for (int i = 0; i < valeurs.length; i++) {
            String valeur = valeurs[i] != null ? valeurs[i] : "";
            ligne.add(valeur);

            // Mettre à jour la largeur maximale de la colonne
            if (valeur.length() > columnWidths.get(i)) {
                columnWidths.set(i, valeur.length());
            }
        }
        rows.add(ligne);
    }

    /**
     * Affiche le tableau
     */
    public void afficher() {
        if (rows.isEmpty()) {
            System.out.println("Aucune donnée à afficher");
            return;
        }

        // Ligne de séparation
        afficherLigneSeparation();

        // En-têtes
        afficherLigne(headers);
        afficherLigneSeparation();

        // Lignes de données
        for (List<String> ligne : rows) {
            afficherLigne(ligne);
        }

        // Ligne de séparation finale
        afficherLigneSeparation();

        // Afficher le nombre total de lignes
        System.out.println("Total:  " + rows.size() + " ligne(s)");
    }

    /**
     * Affiche une ligne du tableau
     */
    private void afficherLigne(List<String> valeurs) {
        System.out.print("│ ");
        for (int i = 0; i < valeurs.size(); i++) {
            String valeur = valeurs.get(i);
            int largeur = columnWidths.get(i);
            System.out.print(padRight(valeur, largeur));
            System.out.print(" │ ");
        }
        System.out.println();
    }

    /**
     * Affiche une ligne de séparation
     */
    private void afficherLigneSeparation() {
        System.out.print("├");
        for (int i = 0; i < columnWidths.size(); i++) {
            System.out.print("─".repeat(columnWidths.get(i) + 2));
            if (i < columnWidths.size() - 1) {
                System.out.print("┼");
            }
        }
        System.out.println("┤");
    }

    /**
     * Complète une chaîne à droite avec des espaces
     */
    private String padRight(String texte, int largeur) {
        if (texte.length() >= largeur) {
            return texte;
        }
        return texte + " ".repeat(largeur - texte.length());
    }

    /**
     * Vide le tableau
     */
    public void vider() {
        rows.clear();
        // Réinitialiser les largeurs
        for (int i = 0; i < headers.size(); i++) {
            columnWidths.set(i, headers.get(i).length());
        }
    }

    /**
     * Retourne le nombre de lignes
     */
    public int getNombreLignes() {
        return rows.size();
    }
}