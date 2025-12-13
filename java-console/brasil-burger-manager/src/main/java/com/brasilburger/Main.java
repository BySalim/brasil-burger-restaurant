package com.brasilburger;

import com.brasilburger.domain.entities.*;
import com.brasilburger.domain.entities.enums.TypeComplement;
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

        // TEST DES ENTITES ARTICLE
        testerArticles();

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
     * Test des entites Article
     */
    private static void testerArticles() {
        System.out.println("=== TEST DES ENTITES ARTICLE ===\n");

        // Test Burger
        System.out.println("--- Test Burger ---");
        Burger burger = new Burger("BRG001", "Burger Classic", "burgers/classic",
                "Delicieux burger avec steak hache", 5000);
        burger.setId(1L);

        System.out.println("Burger cree:  " + burger);
        System.out.println("Categorie: " + burger.getCategorie());
        System.out.println("Prix: " + burger.getPrix() + " FCFA");
        System.out.println("Est burger ?  " + burger.estBurger());
        System.out.println("Disponible ? " + burger.estDisponible());

        burger.setPrix(5500);
        System.out.println("Nouveau prix: " + burger.getPrix() + " FCFA");

        burger.archiver();
        System.out.println("Apres archivage, disponible ? " + burger.estDisponible());
        burger.restaurer();

        // Test validation
        System.out.println("\nTest validation burger sans prix:");
        try {
            Burger burgerInvalide = new Burger("BRG002", "Test", "test", "desc", null);
        } catch (IllegalArgumentException e) {
            System.out.println("  Erreur capturee: " + e.getMessage());
        }

        System.out.println();

        // Test Complement
        System.out.println("--- Test Complement ---");
        Complement boisson = new Complement("CMP001", "Coca-Cola", "complements/coca",
                TypeComplement.BOISSON, 1000);
        boisson.setId(2L);

        Complement frites = new Complement("CMP002", "Frites", "complements/frites",
                TypeComplement.FRITES, 1500);
        frites.setId(3L);

        System.out.println("Boisson creee: " + boisson);
        System.out.println("Type: " + boisson.getType());
        System.out.println("Prix: " + boisson.getPrix() + " FCFA");
        System.out.println("Est boisson ? " + boisson.estBoisson());

        System.out.println("\nFrites creees: " + frites);
        System.out.println("Est frites ? " + frites.estFrites());

        System.out.println();

        // Test Menu
        System.out.println("--- Test Menu ---");
        Menu menu = new Menu("MNU001", "Menu Deluxe", "menus/deluxe",
                "Menu complet avec burger, frites et boisson");
        menu.setId(4L);

        System.out.println("Menu cree: " + menu);
        System.out.println("Prix initial (vide): " + menu.getPrix() + " FCFA");
        System.out.println("Nombre d'articles: " + menu.getNombreArticles());

        // Ajout d'articles au menu via ArticleQuantifier
        ArticleQuantifier aqBurger = new ArticleQuantifier(1, 5000, burger.getId());
        ArticleQuantifier aqFrites = new ArticleQuantifier(1, 1500, frites.getId());
        ArticleQuantifier aqBoisson = new ArticleQuantifier(1, 1000, boisson.getId());

        menu.ajouterArticle(aqBurger);
        menu.ajouterArticle(aqFrites);
        menu.ajouterArticle(aqBoisson);

        System.out.println("\nApres ajout des composants:");
        System.out.println("Nombre d'articles: " + menu.getNombreArticles());
        System.out.println("Prix total calcule: " + menu.getPrix() + " FCFA");
        System.out.println(menu);

        // Test retrait
        menu.retirerArticle(aqBoisson);
        System.out.println("\nApres retrait de la boisson:");
        System.out.println("Nombre d'articles: " + menu.getNombreArticles());
        System.out.println("Prix total: " + menu.getPrix() + " FCFA");

        System.out.println("\n======================");
    }
}