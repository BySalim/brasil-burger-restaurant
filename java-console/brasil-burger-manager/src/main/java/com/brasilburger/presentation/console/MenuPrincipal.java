package com.brasilburger.presentation.console;

import com.brasilburger.presentation.console.menus.MenuArticle;
import com.brasilburger.presentation.console.menus.MenuLivreur;
import com.brasilburger.presentation.console.menus.MenuQuartier;
import com.brasilburger.presentation.console.menus.MenuZone;
import com.brasilburger.presentation.console.utils.ConsoleInput;
import com.brasilburger.presentation.console.utils.ConsoleOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Menu principal de l'application
 * Point d'entrée de toute l'interface console
 */
public class MenuPrincipal {

    private static final Logger logger = LoggerFactory.getLogger(MenuPrincipal.class);

    private final MenuZone menuZone;
    private final MenuQuartier menuQuartier;
    private final MenuLivreur menuLivreur;
    private final MenuArticle menuArticle;

    public MenuPrincipal() {
        this.menuZone = new MenuZone();
        this.menuQuartier = new MenuQuartier();
        this.menuLivreur = new MenuLivreur();
        this.menuArticle = new MenuArticle();
    }

    /**
     * Affiche et gère le menu principal
     */
    public void afficher() {
        boolean continuer = true;

        while (continuer) {
            try {
                ConsoleOutput.viderConsole();
                afficherBanniere();
                afficherMenu();

                int choix = ConsoleInput.lireChoixMenu(4);

                switch (choix) {
                    case 1:
                        menuZone.afficher();
                        break;
                    case 2:
                        menuQuartier.afficher();
                        break;
                    case 3:
                        menuLivreur.afficher();
                        break;
                    case 4:
                        menuArticle.afficher();
                        break;
                    case 0:
                        continuer = quitter();
                        break;
                    default:
                        ConsoleOutput.erreur("Choix invalide");
                        ConsoleInput.attendreEntree();
                }

            } catch (Exception e) {
                ConsoleOutput.erreur("Erreur:  " + e.getMessage());
                logger.error("Erreur dans le menu principal", e);
                ConsoleInput.attendreEntree();
            }
        }
    }

    /**
     * Affiche la bannière de l'application
     */
    private void afficherBanniere() {
        ConsoleOutput.separateurEpais();
        System.out.println(ConsoleOutput.GRAS + ConsoleOutput.ROUGE +
                centre("🍔  BRASIL BURGER MANAGER  🍔", 60) + ConsoleOutput.RESET);
        System.out.println(ConsoleOutput.CYAN +
                centre("Système de Gestion de Restaurant", 60) + ConsoleOutput.RESET);
        ConsoleOutput.separateurEpais();
        System.out.println();
    }

    /**
     * Affiche le menu principal
     */
    private void afficherMenu() {
        ConsoleOutput.afficherGras("MENU PRINCIPAL");
        System.out.println();

        System.out.println("  1.🌍  Gestion des zones");
        System.out.println("  2.🏘️  Gestion des quartiers");
        System.out.println("  3.🚴  Gestion des livreurs");
        System.out.println("  4.🍔  Gestion des articles");
        System.out.println();
        System.out.println("  0.🚪  Quitter");
        System.out.println();
    }

    /**
     * Gère la sortie de l'application
     * @return false pour quitter, true pour continuer
     */
    private boolean quitter() {
        ConsoleOutput.sousTitre("Quitter l'application");

        if (ConsoleInput.lireConfirmation("Voulez-vous vraiment quitter")) {
            ConsoleOutput.viderConsole();
            afficherMessageAuRevoir();
            return false;
        }

        return true;
    }

    /**
     * Affiche le message d'au revoir
     */
    private void afficherMessageAuRevoir() {
        System.out.println();
        ConsoleOutput.separateurEpais();
        System.out.println(ConsoleOutput.GRAS + ConsoleOutput.VERT +
                centre("Merci d'avoir utilisé Brasil Burger Manager!", 60) + ConsoleOutput.RESET);
        System.out.println(ConsoleOutput.CYAN +
                centre("À bientôt!  👋", 60) + ConsoleOutput.RESET);
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