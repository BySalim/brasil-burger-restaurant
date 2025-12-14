package com.brasilburger.presentation.console.menus;

import com.brasilburger.presentation.console.controllers.LivreurController;
import com.brasilburger.presentation.console.utils.ConsoleOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Menu de gestion des livreurs
 * Point d'entrée pour toutes les opérations liées aux livreurs
 */
public class MenuLivreur {

    private static final Logger logger = LoggerFactory.getLogger(MenuLivreur.class);
    private final LivreurController livreurController;

    public MenuLivreur() {
        this.livreurController = new LivreurController();
    }

    /**
     * Affiche et gère le menu des livreurs
     */
    public void afficher() {
        try {
            ConsoleOutput.viderConsole();
            afficherBanniere();
            livreurController.afficherMenu();
        } catch (Exception e) {
            ConsoleOutput.erreur("Erreur dans le menu livreur: " + e.getMessage());
            logger.error("Erreur menu livreur", e);
        }
    }

    /**
     * Affiche la bannière du menu livreur
     */
    private void afficherBanniere() {
        ConsoleOutput.separateurEpais();
        System.out.println(ConsoleOutput.GRAS + ConsoleOutput.CYAN +
                centre("🚴  GESTION DES LIVREURS", 60) + ConsoleOutput.RESET);
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