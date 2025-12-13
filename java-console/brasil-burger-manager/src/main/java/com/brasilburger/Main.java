package com.brasilburger;

import com.brasilburger.domain.entities.enums.*;
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

        // TEST DES ENUMS
        testerEnums();

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
     * Test des enums du domaine
     */
    private static void testerEnums() {
        System.out.println("=== TEST DES ENUMS ===\n");

        // Test CategorieArticle
        System.out.println("Categories d'articles:");
        for (CategorieArticle cat : CategorieArticle.values()) {
            System.out.println("  - " + cat.name() + ": " + cat.getLibelle());
        }

        // Test TypeComplement
        System.out.println("\nTypes de complements:");
        for (TypeComplement type : TypeComplement.values()) {
            System.out.println("  - " + type.name() + ": " + type.getLibelle());
        }

        // Test RoleUtilisateur
        System.out.println("\nRoles utilisateurs:");
        for (RoleUtilisateur role : RoleUtilisateur.values()) {
            System.out.println("  - " + role.name() + ": " + role.getLibelle());
        }

        // Test EtatCommande
        System.out.println("\nEtats de commande:");
        for (EtatCommande etat : EtatCommande.values()) {
            System.out.println("  - " + etat.name() + ": " + etat.getLibelle());
        }

        // Test TypeRecuperation
        System.out.println("\nTypes de recuperation:");
        for (TypeRecuperation type : TypeRecuperation.values()) {
            System.out.println("  - " + type.name() + ": " + type.getLibelle());
        }

        // Test ModePaiement
        System.out.println("\nModes de paiement:");
        for (ModePaiement mode :  ModePaiement.values()) {
            System.out.println("  - " + mode.name() + ": " + mode.getLibelle());
        }

        // Test StatutLivraison
        System.out.println("\nStatuts de livraison:");
        for (StatutLivraison statut : StatutLivraison.values()) {
            System.out.println("  - " + statut.name() + ": " + statut.getLibelle());
        }

        // Test CategoriePanier
        System.out.println("\nCategories de panier:");
        for (CategoriePanier cat :  CategoriePanier.values()) {
            System.out.println("  - " + cat.name() + ": " + cat.getLibelle());
        }

        System.out.println("\n======================");
    }
}