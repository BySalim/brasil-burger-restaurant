package com.brasilburger;

import com.brasilburger.config.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        // Afficher le banner
        afficherBanner();

        // Valider la configuration
        if (!AppConfig.validateConfiguration()) {
            logger.error("Configuration invalide !  Verifiez vos variables d'environnement.");
            System.err.println("\nERREUR:  Variables d'environnement manquantes !");
            System.err.println("Veuillez definir:  DB_URL, DB_USERNAME, DB_PASSWORD");
            System.err.println("                  CLOUDINARY_CLOUD_NAME, CLOUDINARY_API_KEY, CLOUDINARY_API_SECRET");
            System.exit(1);
        }

        // Afficher les infos de l'application
        logger.info("Configuration validee avec succes");
        logger.info("Demarrage de {} v{}", AppConfig.getAppName(), AppConfig.getAppVersion());
        logger.info("Environnement: {}", AppConfig.getAppEnvironment());

        System.out.println("\n=== CONFIGURATION CHARGEE ===");
        System.out.println("Application: " + AppConfig.getAppName());
        System.out.println("Version: " + AppConfig.getAppVersion());
        System.out.println("Environnement: " + AppConfig.getAppEnvironment());
        System.out.println("Pool Size: " + AppConfig.getDatabasePoolSize());
        System.out.println("==============================\n");

        logger.info("Application prete !");
    }

    /**
     * Affiche le banner de demarrage depuis banner.txt
     */
    private static void afficherBanner() {
        try {
            InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("banner.txt");

            if (inputStream != null) {
                String banner = new BufferedReader(
                        new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                        .lines()
                        .collect(Collectors.joining("\n"));

                System.out.println(banner);
                System.out.println();
            } else {
                logger.warn("Fichier banner.txt introuvable");
            }
        } catch (Exception e) {
            logger.warn("Impossible d'afficher le banner: {}", e.getMessage());
        }
    }
}