package com.brasilburger;

import com.brasilburger.domain.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        // Afficher le banner
        afficherBanner();

        // TEST DES EXCEPTIONS
        testerExceptions();

        logger.info("Tests termines avec succes !");
    }

    /**
     * Affiche le banner de demarrage depuis banner.txt
     */
    private static void afficherBanner() {
        try {
            InputStream inputStream = Main.class.getClassLoader()
                    .getResourceAsStream("banner.txt");

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
            logger.warn("Impossible d'afficher le banner:  {}", e.getMessage());
        }
    }

    /**
     * Test des exceptions personnalisees
     */
    private static void testerExceptions() {
        System.out.println("=== TEST DES EXCEPTIONS ===\n");

        // Test ArticleNotFoundException
        System.out.println("--- Test ArticleNotFoundException ---");
        try {
            throw new ArticleNotFoundException(123L);
        } catch (ArticleNotFoundException e) {
            System.out.println("Exception capturee:  " + e.getMessage());
        }

        try {
            throw new ArticleNotFoundException("code", "BRG001");
        } catch (ArticleNotFoundException e) {
            System.out.println("Exception capturee: " + e.getMessage());
        }

        System.out.println();

        // Test ZoneNotFoundException
        System.out.println("--- Test ZoneNotFoundException ---");
        try {
            throw new ZoneNotFoundException(456L);
        } catch (ZoneNotFoundException e) {
            System.out.println("Exception capturee: " + e.getMessage());
        }

        try {
            throw new ZoneNotFoundException("nom", "Plateau");
        } catch (ZoneNotFoundException e) {
            System.out.println("Exception capturee: " + e.getMessage());
        }

        System.out.println();

        // Test QuartierNotFoundException
        System.out.println("--- Test QuartierNotFoundException ---");
        try {
            throw new QuartierNotFoundException(789L);
        } catch (QuartierNotFoundException e) {
            System.out.println("Exception capturee: " + e.getMessage());
        }

        System.out.println();

        // Test LivreurNotFoundException
        System.out.println("--- Test LivreurNotFoundException ---");
        try {
            throw new LivreurNotFoundException("telephone", "771234567");
        } catch (LivreurNotFoundException e) {
            System.out.println("Exception capturee: " + e.getMessage());
        }

        System.out.println();

        // Test ImageUploadException
        System.out.println("--- Test ImageUploadException ---");
        try {
            throw new ImageUploadException("burger.jpg", "Fichier trop volumineux");
        } catch (ImageUploadException e) {
            System.out.println("Exception capturee: " + e.getMessage());
        }

        try {
            throw new ImageUploadException("Erreur de connexion a Cloudinary",
                    new RuntimeException("Connection timeout"));
        } catch (ImageUploadException e) {
            System.out.println("Exception capturee: " + e.getMessage());
            System.out.println("Cause: " + e.getCause().getMessage());
        }

        System.out.println();

        // Test ValidationException
        System.out.println("--- Test ValidationException ---");
        try {
            throw new ValidationException("libelle", "Le libelle ne peut pas etre vide");
        } catch (ValidationException e) {
            System.out.println("Exception capturee: " + e.getMessage());
            System.out.println("Nombre d'erreurs: " + e.getNombreErreurs());
        }

        try {
            throw new ValidationException(Arrays.asList(
                    "Le nom est obligatoire",
                    "Le prix doit etre positif",
                    "L'image est requise"
            ));
        } catch (ValidationException e) {
            System.out.println("Exception capturee: " + e.getMessage());
            System.out.println("Erreurs detaillees:");
            e.getErreurs().forEach(erreur -> System.out.println("  - " + erreur));
        }

        System.out.println();

        // Test DuplicateCodeArticleException
        System.out.println("--- Test DuplicateCodeArticleException ---");
        try {
            throw new DuplicateCodeArticleException("BRG001");
        } catch (DuplicateCodeArticleException e) {
            System.out.println("Exception capturee: " + e.getMessage());
        }

        System.out.println();

        // Test DuplicateZoneException
        System.out.println("--- Test DuplicateZoneException ---");
        try {
            throw new DuplicateZoneException("Parcelles Assainies");
        } catch (DuplicateZoneException e) {
            System.out.println("Exception capturee: " + e.getMessage());
        }

        System.out.println("\n======================");
    }
}