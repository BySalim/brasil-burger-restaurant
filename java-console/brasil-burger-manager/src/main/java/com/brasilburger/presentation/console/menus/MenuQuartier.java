package com.brasilburger.presentation.console.menus;

import com.brasilburger.presentation.console.controllers.QuartierController;
import com.brasilburger.presentation.console.utils.ConsoleOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Menu de gestion des quartiers
 * Point d'entrée pour toutes les opérations liées aux quartiers
 */
public class MenuQuartier {

    private static final Logger logger = LoggerFactory.getLogger(MenuQuartier.class);
    private final QuartierController quartierController;

    public MenuQuartier() {
        this.quartierController = new QuartierController();
    }

    /**
     * Affiche et gère le menu des quartiers
     */
    public void afficher() {
        try {
            ConsoleOutput.viderConsole();
            afficherBanniere();
            quartierController.afficherMenu();
        } catch (Exception e) {
            ConsoleOutput.erreur("Erreur dans le menu quartier: " + e.getMessage());
            logger.error("Erreur menu quartier", e);
        }
    }

    /**
     * Affiche la bannière du menu quartier
     */
    private void afficherBanniere() {
        ConsoleOutput.separateurEpais();
        System.out.println(ConsoleOutput.GRAS + ConsoleOutput.CYAN +
                centre("🏘️  GESTION DES QUARTIERS", 60) + ConsoleOutput.RESET);
        ConsoleOutput.separateurEpais();
        System.out.println();
    }

    /**
     * Centre un texte dans une largeur donnée
     */
    private String centre(String texte, int largeur) {
        if (texte.length() >= largeur) {
            return texte;
        }
        int espacesGauche = (largeur - texte.length()) / 2;
        return " ".repeat(espacesGauche) + texte;
    }
}