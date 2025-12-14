package com.brasilburger;

import com.brasilburger.presentation.console.ConsoleApp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe principale de l'application
 * Point d'entrée du programme
 */
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Démarrage de Brasil Burger Manager");

        try {
            ConsoleApp app = new ConsoleApp();
            app.demarrer();
        } catch (Exception e) {
            logger.error("Erreur fatale", e);
            System.err.println("Une erreur fatale s'est produite.  Consultez les logs pour plus de détails.");
            System.exit(1);
        }

        logger.info("Arrêt de Brasil Burger Manager");
    }
}