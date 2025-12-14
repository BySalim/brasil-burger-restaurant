package com.brasilburger.presentation.console.menus;

import com.brasilburger.presentation.console.controllers.ZoneController;
import com.brasilburger.presentation.console.utils.ConsoleOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Menu de gestion des zones
 * Point d'entrée pour toutes les opérations liées aux zones
 */
public class MenuZone {

    private static final Logger logger = LoggerFactory.getLogger(MenuZone.class);
    private final ZoneController zoneController;

    public MenuZone() {
        this.zoneController = new ZoneController();
    }

    /**
     * Affiche et gère le menu des zones
     */
    public void afficher() {
        try {
            ConsoleOutput.viderConsole();
            afficherBanniere();
            zoneController.afficherMenu();
        } catch (Exception e) {
            ConsoleOutput.erreur("Erreur dans le menu zone:   " + e.getMessage());
            logger.error("Erreur menu zone", e);
        }
    }

    /**
     * Affiche la bannière du menu zone
     */
    private void afficherBanniere() {
        ConsoleOutput.separateurEpais();
        System.out.println(ConsoleOutput.GRAS + ConsoleOutput.CYAN +
                centre("🌍  GESTION DES ZONES DE LIVRAISON", 60) + ConsoleOutput.RESET);
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