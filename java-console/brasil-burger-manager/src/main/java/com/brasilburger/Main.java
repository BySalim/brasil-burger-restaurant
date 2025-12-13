package com.brasilburger;

import com.brasilburger.domain.entities.Livreur;
import com.brasilburger.domain.entities.Quartier;
import com.brasilburger.domain.entities.Zone;
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

        // TEST DES ENTITES
        testerEntites();

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
     * Test des entites de base
     */
    private static void testerEntites() {
        System.out.println("=== TEST DES ENTITES ===\n");

        // Test Zone
        System.out.println("--- Test Zone ---");
        Zone zone1 = new Zone("Plateau", 2000);
        zone1.setId(1L);
        System.out.println("Zone creee:  " + zone1);
        System.out.println("Zone active ?  " + zone1.estActive());

        zone1.modifierPrixLivraison(2500);
        System.out.println("Nouveau prix: " + zone1.getPrixLivraison() + " FCFA");

        zone1.archiver();
        System.out.println("Apres archivage: " + zone1);
        System.out.println("Zone active ? " + zone1.estActive());

        zone1.restaurer();
        System.out.println("Apres restauration, active ? " + zone1.estActive());

        // Test validation
        System.out.println("\nTest validation prix negatif:");
        try {
            zone1.modifierPrixLivraison(-100);
        } catch (IllegalArgumentException e) {
            System.out.println("  Erreur capturee: " + e.getMessage());
        }

        System.out.println();

        // Test Quartier
        System.out.println("--- Test Quartier ---");
        Quartier quartier1 = new Quartier("Mermoz", 1L);
        quartier1.setId(1L);
        System.out.println("Quartier cree: " + quartier1);
        System.out.println("Appartient a la zone 1 ? " + quartier1.appartientAZone(1L));
        System.out.println("Appartient a la zone 2 ? " + quartier1.appartientAZone(2L));

        quartier1.changerZone(2L);
        System.out.println("Apres changement de zone:  " + quartier1);
        System.out.println("Appartient a la zone 2 ? " + quartier1.appartientAZone(2L));

        // Test validation
        System.out.println("\nTest validation zone null:");
        try {
            quartier1.changerZone(null);
        } catch (IllegalArgumentException e) {
            System.out.println("  Erreur capturee: " + e.getMessage());
        }

        System.out.println();

        // Test Livreur
        System.out.println("--- Test Livreur ---");
        Livreur livreur1 = new Livreur("Diop", "Moussa", "771234567");
        livreur1.setId(1L);
        System.out.println("Livreur cree: " + livreur1);
        System.out.println("Nom complet: " + livreur1.getNomComplet());
        System.out.println("Peut etre affecte ? " + livreur1.peutEtreAffecte());

        livreur1.marquerOccupe();
        System.out.println("\nApres marquage occupe:");
        System.out.println("  Disponible ? " + livreur1.isEstDisponible());
        System.out.println("  Peut etre affecte ? " + livreur1.peutEtreAffecte());

        livreur1.marquerDisponible();
        System.out.println("\nApres marquage disponible:");
        System.out.println("  Disponible ? " + livreur1.isEstDisponible());
        System.out.println("  Peut etre affecte ? " + livreur1.peutEtreAffecte());

        livreur1.archiver();
        System.out.println("\nApres archivage:");
        System.out.println("  Archive ? " + livreur1.isEstArchiver());
        System.out.println("  Disponible ? " + livreur1.isEstDisponible());
        System.out.println("  Peut etre affecte ? " + livreur1.peutEtreAffecte());

        // Test validation
        System.out.println("\nTest validation livreur archive:");
        try {
            livreur1.marquerDisponible();
        } catch (IllegalStateException e) {
            System.out.println("  Erreur capturee: " + e.getMessage());
        }

        livreur1.restaurer();
        System.out.println("\nApres restauration:");
        System.out.println("  Archive ? " + livreur1.isEstArchiver());
        System.out.println("  Disponible ? " + livreur1.isEstDisponible());
        System.out.println("  Peut etre affecte ? " + livreur1.peutEtreAffecte());

        System.out.println("\n======================");
    }
}