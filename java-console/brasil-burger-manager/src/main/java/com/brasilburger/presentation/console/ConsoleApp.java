package com.brasilburger.presentation.console;

import com.brasilburger.config.AppConfig;
import com.brasilburger.infrastructure.cloudinary.CloudinaryConfig;
import com.brasilburger.infrastructure.database.NeonConnectionManager;
import com.brasilburger.presentation.console.utils.ConsoleOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * Application console principale
 * Point d'entrée de l'application
 */
public class ConsoleApp {

    private static final Logger logger = LoggerFactory.getLogger(ConsoleApp.class);

    /**
     * Démarre l'application console
     */
    public void demarrer() {
        try {
            // Afficher le banner de démarrage
            afficherBanner();

            // Vérifier la configuration
            ConsoleOutput.chargement("Vérification de la configuration");
            if (!AppConfig.validateConfiguration()) {
                ConsoleOutput.effacerLigne();
                ConsoleOutput.erreur("Configuration invalide!");
                ConsoleOutput.info("Vérifiez votre fichier .env");
                return;
            }
            ConsoleOutput.effacerLigne();
            ConsoleOutput.succes("Configuration validée");

            // Test connexion base de données
            ConsoleOutput.chargement("Connexion à la base de données");
            if (!NeonConnectionManager.testConnection()) {
                ConsoleOutput.effacerLigne();
                ConsoleOutput.erreur("Impossible de se connecter à la base de données");
                ConsoleOutput.info("Vérifiez vos paramètres de connexion Neon");
                return;
            }
            ConsoleOutput.effacerLigne();
            ConsoleOutput.succes("Connexion base de données établie");

            // Test connexion Cloudinary
            ConsoleOutput.chargement("Connexion à Cloudinary");
            if (!CloudinaryConfig.testConnection()) {
                ConsoleOutput.effacerLigne();
                ConsoleOutput.erreur("Impossible de se connecter à Cloudinary");
                ConsoleOutput.info("Vérifiez vos paramètres Cloudinary");
                return;
            }
            ConsoleOutput.effacerLigne();
            ConsoleOutput.succes("Connexion Cloudinary établie");

            System.out.println();
            ConsoleOutput.succes("Toutes les connexions sont établies!");
            ConsoleOutput.info("Démarrage de l'application.. .");

            // Petite pause pour que l'utilisateur voie les messages
            Thread.sleep(1500);

            // Lancer le menu principal
            MenuPrincipal menuPrincipal = new MenuPrincipal();
            menuPrincipal.afficher();

            logger.info("Application terminée normalement");

        } catch (Exception e) {
            ConsoleOutput.erreur("Erreur lors du démarrage: " + e.getMessage());
            logger.error("Erreur fatale au démarrage", e);
        }
    }

    /**
     * Affiche le banner de démarrage depuis banner.txt
     */
    private void afficherBanner() {
        try {
            InputStream inputStream = getClass().getClassLoader()
                    .getResourceAsStream("banner.txt");

            if (inputStream != null) {
                String banner = new BufferedReader(
                        new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                        .lines()
                        .collect(Collectors.joining("\n"));

                System.out.println(ConsoleOutput.CYAN + banner + ConsoleOutput.RESET);
                System.out.println();
            } else {
                // Banner par défaut si le fichier n'existe pas
                afficherBannerParDefaut();
            }
        } catch (Exception e) {
            logger.warn("Impossible d'afficher le banner:  {}", e.getMessage());
            afficherBannerParDefaut();
        }
    }

    /**
     * Affiche un banner par défaut
     */
    private void afficherBannerParDefaut() {
        System.out.println(ConsoleOutput.CYAN);
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                            ║");
        System.out.println("║           🍔  BRASIL BURGER MANAGER  🍔                   ║");
        System.out.println("║                                                            ║");
        System.out.println("║          Système de Gestion de Restaurant                 ║");
        System.out.println("║                                                            ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.println(ConsoleOutput.RESET);
        System.out.println();
    }
}