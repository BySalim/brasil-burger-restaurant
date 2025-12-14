package com.brasilburger.presentation.console.menus;

import com.brasilburger.presentation.console.controllers.ArticleController;
import com.brasilburger.presentation.console.utils.ConsoleOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Menu de gestion des articles
 * Point d'entrée pour toutes les opérations liées aux articles (Burgers, Menus, Compléments)
 */
public class MenuArticle {

    private static final Logger logger = LoggerFactory.getLogger(MenuArticle.class);
    private final ArticleController articleController;

    public MenuArticle() {
        this.articleController = new ArticleController();
    }

    /**
     * Affiche et gère le menu des articles
     */
    public void afficher() {
        try {
            ConsoleOutput.viderConsole();
            afficherBanniere();
            articleController.afficherMenu();
        } catch (Exception e) {
            ConsoleOutput.erreur("Erreur dans le menu article: " + e.getMessage());
            logger.error("Erreur menu article", e);
        }
    }

    /**
     * Affiche la bannière du menu article
     */
    private void afficherBanniere() {
        ConsoleOutput.separateurEpais();
        System.out.println(ConsoleOutput.GRAS + ConsoleOutput.CYAN +
                centre("🍔  GESTION DES ARTICLES", 60) + ConsoleOutput.RESET);
        System.out.println(centre("Burgers • Menus • Compléments", 60));
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