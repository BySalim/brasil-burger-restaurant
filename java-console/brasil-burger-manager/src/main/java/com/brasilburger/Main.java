package com.brasilburger;

import com.brasilburger.domain.valueobjects.ImageInfo;
import com.brasilburger.domain.valueobjects.Prix;
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

        // TEST DES VALUE OBJECTS
        testerValueObjects();

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
            logger.warn("Impossible d'afficher le banner: {}", e.getMessage());
        }
    }

    /**
     * Test des Value Objects
     */
    private static void testerValueObjects() {
        System.out.println("=== TEST DES VALUE OBJECTS ===\n");

        // Test Prix
        System.out.println("--- Test Prix ---");
        try {
            Prix prix1 = new Prix(5000);
            Prix prix2 = new Prix(3500);
            Prix prix3 = Prix.zero();

            System.out.println("Prix 1: " + prix1.formater());
            System.out.println("Prix 2: " + prix2.formater());
            System.out.println("Prix nul: " + prix3.formater());

            // Operations
            Prix somme = prix1.ajouter(prix2);
            System.out.println("Somme (5000 + 3500): " + somme.formater());

            Prix difference = prix1.soustraire(prix2);
            System.out.println("Difference (5000 - 3500): " + difference.formater());

            Prix multiple = prix1.multiplier(3);
            System.out.println("Multiple (5000 x 3): " + multiple.formater());

            // Comparaisons
            System.out.println("5000 > 3500 ? " + prix1.estSuperieurA(prix2));
            System.out.println("5000 < 3500 ? " + prix1.estInferieurA(prix2));
            System.out.println("Prix nul ? " + prix3.estNul());
            System.out.println("Prix positif ? " + prix1.estPositif());

            // Test validation
            System.out.println("\nTest validation (prix negatif):");
            try {
                Prix prixInvalide = new Prix(-100);
            } catch (IllegalArgumentException e) {
                System.out.println("  Erreur capturee: " + e.getMessage());
            }

        } catch (Exception e) {
            System.err.println("Erreur lors du test Prix: " + e.getMessage());
        }

        System.out.println();

        // Test ImageInfo
        System.out.println("--- Test ImageInfo ---");
        try {
            ImageInfo image1 = new ImageInfo(
                    "brasil-burger/articles/burger_classic",
                    "https://res.cloudinary.com/dkmrexigc/image/upload/v1766198145/burger_brazil_nmfggy.jpg",
                    "jpg"
            );

            ImageInfo image2 = new ImageInfo(
                    "brasil-burger/articles/menu_deluxe",
                    "https://res.cloudinary.com/demo/image/upload/v1234567/brasil-burger/articles/menu_deluxe.png"
            );

            System.out.println("Image 1:  " + image1);
            System.out.println("Public ID: " + image1.getPublicId());
            System.out.println("URL: " + image1.getUrl());
            System.out.println("Format: " + image1.getFormat());
            System.out.println("Format valide ?  " + image1.estFormatValide());

            System.out.println("\nImage 2: " + image2);
            System.out.println("Format par defaut: " + image2.getFormat());

            // Transformations
            System.out.println("\nTransformations:");
            System.out.println("URL miniature (150x150): " + image1.getUrlMiniature());
            System.out.println("URL redimensionnee (300x200): " + image1.getUrlAvecTransformation(300, 200));

            // Test validation
            System.out.println("\nTest validation (publicId vide):");
            try {
                ImageInfo imageInvalide = new ImageInfo("", "http://example.com/image.jpg");
            } catch (IllegalArgumentException e) {
                System.out.println("  Erreur capturee: " + e.getMessage());
            }

            System.out.println("\nTest validation (URL vide):");
            try {
                ImageInfo imageInvalide = new ImageInfo("test_id", "");
            } catch (IllegalArgumentException e) {
                System.out.println("  Erreur capturee: " + e.getMessage());
            }

        } catch (Exception e) {
            System.err.println("Erreur lors du test ImageInfo: " + e.getMessage());
        }

        System.out.println("\n======================");
    }
}