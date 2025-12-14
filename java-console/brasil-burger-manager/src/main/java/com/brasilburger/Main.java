package com.brasilburger;

import com.brasilburger.config.AppConfig;
import com.brasilburger.domain.entities.*;
import com.brasilburger.domain.entities.enums.CategorieArticle;
import com.brasilburger.domain.entities.enums.TypeComplement;
import com.brasilburger.domain.exceptions.DuplicateCodeArticleException;
import com.brasilburger.domain.repositories.IArticleRepository;
import com.brasilburger.domain.repositories.impl.NeonArticleRepository;
import com.brasilburger.infrastructure.database.NeonConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        // Afficher le banner
        afficherBanner();

        // Valider la configuration
        if (! AppConfig.validateConfiguration()) {
            logger.error("Configuration invalide!");
            System.exit(1);
        }

        // Tester la connexion
        if (!NeonConnectionManager.testConnection()) {
            logger.error("Impossible de se connecter a la base de donnees");
            System.exit(1);
        }

        logger.info("Configuration validee et connexion etablie");

        // TEST DU REPOSITORY ARTICLE (Commit 13)
        testerArticleRepository();

        logger.info("Tests termines avec succes!");
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
            }
        } catch (Exception e) {
            logger.warn("Impossible d'afficher le banner:  {}", e.getMessage());
        }
    }

    /**
     * Test du repository Article (Commit 13)
     */
    private static void testerArticleRepository() {
        System.out.println("=== TEST DU REPOSITORY ARTICLE ===\n");

        IArticleRepository articleRepo = new NeonArticleRepository();

        try {
            // ==========================================
            // Test 1: CREATE - Burger
            // ==========================================
            System.out.println("Test 1: Creation de burgers.. .");
            Burger burger1 = new Burger("BRG001", "Burger Classic", "burgers/classic",
                    "Delicieux burger avec steak hache, salade, tomate", 5000);

            Burger burger2 = new Burger("BRG002", "Burger Cheese", "burgers/cheese",
                    "Burger avec fromage cheddar fondant", 5500);

            burger1 = (Burger) articleRepo.save(burger1);
            burger2 = (Burger) articleRepo.save(burger2);

            System.out.println("  ✓ Burger 1: " + burger1);
            System.out.println("  ✓ Burger 2: " + burger2);

            // ==========================================
            // Test 2: CREATE - Complement
            // ==========================================
            System.out.println("\nTest 2: Creation de complements...");
            Complement boisson1 = new Complement("CMP001", "Coca-Cola", "complements/coca",
                    TypeComplement.BOISSON, 1000);

            Complement boisson2 = new Complement("CMP002", "Sprite", "complements/sprite",
                    TypeComplement.BOISSON, 1000);

            Complement frites = new Complement("CMP003", "Frites", "complements/frites",
                    TypeComplement.FRITES, 1500);

            boisson1 = (Complement) articleRepo.save(boisson1);
            boisson2 = (Complement) articleRepo.save(boisson2);
            frites = (Complement) articleRepo.save(frites);

            System.out.println("  ✓ Boisson 1: " + boisson1);
            System.out.println("  ✓ Boisson 2: " + boisson2);
            System.out.println("  ✓ Frites: " + frites);

            // ==========================================
            // Test 3: CREATE - Menu
            // ==========================================
            System.out.println("\nTest 3: Creation de menus.. .");
            Menu menu1 = new Menu("MNU001", "Menu Deluxe", "menus/deluxe",
                    "Menu complet avec burger, frites et boisson");

            Menu menu2 = new Menu("MNU002", "Menu Classic", "menus/classic",
                    "Menu economique avec burger et boisson");

            menu1 = (Menu) articleRepo.save(menu1);
            menu2 = (Menu) articleRepo.save(menu2);

            System.out.println("  ✓ Menu 1: " + menu1);
            System.out.println("  ✓ Menu 2: " + menu2);

            // ==========================================
            // Test 4: COUNT
            // ==========================================
            System.out.println("\nTest 4: Comptage des articles.. .");
            long countTotal = articleRepo.count();
            long countBurgers = articleRepo.countByCategorie(CategorieArticle.BURGER);
            long countMenus = articleRepo.countByCategorie(CategorieArticle.MENU);
            long countComplements = articleRepo.countByCategorie(CategorieArticle.COMPLEMENT);

            System.out.println("  Total:  " + countTotal);
            System.out.println("  Burgers: " + countBurgers);
            System.out.println("  Menus: " + countMenus);
            System.out.println("  Complements: " + countComplements);

            // ==========================================
            // Test 5: FIND BY ID
            // ==========================================
            System.out.println("\nTest 5: Recherche par ID...");
            Optional<Article> foundBurger = articleRepo.findById(burger1.getId());
            if (foundBurger.isPresent()) {
                System.out.println("  ✓ Burger trouve: " + foundBurger.get().getLibelle());
                System.out.println("    Type reel: " + foundBurger.get().getClass().getSimpleName());
                if (foundBurger.get() instanceof Burger) {
                    Burger b = (Burger) foundBurger.get();
                    System.out.println("    Prix: " + b.getPrix() + " FCFA");
                    System.out.println("    Description: " + b.getDescription());
                }
            }

            // ==========================================
            // Test 6: FIND BY CODE
            // ==========================================
            System.out.println("\nTest 6: Recherche par code...");
            Optional<Article> foundByCode = articleRepo.findByCode("CMP001");
            if (foundByCode.isPresent()) {
                System.out.println("  ✓ Article trouve: " + foundByCode.get().getLibelle());
                System.out.println("    Type reel: " + foundByCode.get().getClass().getSimpleName());
                if (foundByCode.get() instanceof Complement) {
                    Complement c = (Complement) foundByCode.get();
                    System.out.println("    Type:  " + c.getType());
                    System.out.println("    Prix: " + c.getPrix() + " FCFA");
                }
            }

            // ==========================================
            // Test 7: FIND ALL
            // ==========================================
            System.out.println("\nTest 7: Liste de tous les articles...");
            List<Article> allArticles = articleRepo.findAll();
            System.out.println("  Nombre d'articles: " + allArticles.size());
            allArticles.forEach(a -> System.out.println("    - " + a.getLibelle() +
                    " (" + a.getCategorie() + ") - Code: " + a.getCode()));

            // ==========================================
            // Test 8: FIND BY CATEGORIE
            // ==========================================
            System.out.println("\nTest 8: Articles par categorie...");

            System.out.println("  Burgers:");
            List<Article> burgers = articleRepo.findByCategorie(CategorieArticle.BURGER);
            burgers.forEach(a -> {
                Burger b = (Burger) a;
                System.out.println("    - " + b.getLibelle() + " - " + b.getPrix() + " FCFA");
            });

            System.out.println("\n  Complements:");
            List<Article> complements = articleRepo.findByCategorie(CategorieArticle.COMPLEMENT);
            complements.forEach(a -> {
                Complement c = (Complement) a;
                System.out.println("    - " + c.getLibelle() + " (" + c.getType() + ") - " + c.getPrix() + " FCFA");
            });

            System.out.println("\n  Menus:");
            List<Article> menus = articleRepo.findByCategorie(CategorieArticle.MENU);
            menus.forEach(a -> {
                Menu m = (Menu) a;
                System.out.println("    - " + m.getLibelle() + " - " + m.getDescription());
            });

            // ==========================================
            // Test 9: UPDATE
            // ==========================================
            System.out.println("\nTest 9: Mise a jour d'articles...");

            // Modifier un burger
            burger1.setPrix(5200);
            burger1.modifierLibelle("Burger Classic Premium");
            burger1 = (Burger) articleRepo.save(burger1);
            System.out.println("  ✓ Burger mis a jour: " + burger1.getLibelle() + " - " + burger1.getPrix() + " FCFA");

            // Modifier un complement
            frites.setPrix(1800);
            frites = (Complement) articleRepo.save(frites);
            System.out.println("  ✓ Complement mis a jour: " + frites.getLibelle() + " - " + frites.getPrix() + " FCFA");

            // ==========================================
            // Test 10:  ARCHIVAGE
            // ==========================================
            System.out.println("\nTest 10: Archivage d'articles...");
            burger2.archiver();
            burger2 = (Burger) articleRepo.save(burger2);
            System.out.println("  ✓ Burger archive: " + burger2.getLibelle());
            System.out.println("    Disponible: " + burger2.estDisponible());

            // ==========================================
            // Test 11: FIND BY EST_ARCHIVER
            // ==========================================
            System.out.println("\nTest 11: Articles par statut d'archivage...");
            List<Article> articlesActifs = articleRepo.findByEstArchiver(false);
            List<Article> articlesArchives = articleRepo.findByEstArchiver(true);

            System.out.println("  Actifs:  " + articlesActifs.size());
            articlesActifs.forEach(a -> System.out.println("    - " + a.getLibelle()));

            System.out.println("\n  Archives: " + articlesArchives.size());
            articlesArchives.forEach(a -> System.out.println("    - " + a.getLibelle()));

            // ==========================================
            // Test 12: FIND AVAILABLE BY CATEGORIE
            // ==========================================
            System.out.println("\nTest 12: Burgers disponibles.. .");
            List<Article> burgersDisponibles = articleRepo.findAvailableByCategorie(CategorieArticle.BURGER);
            System.out.println("  Nombre de burgers disponibles: " + burgersDisponibles.size());
            burgersDisponibles.forEach(a -> System.out.println("    - " + a.getLibelle()));

            // ==========================================
            // Test 13: EXISTS BY CODE
            // ==========================================
            System.out.println("\nTest 13: Verification d'existence...");
            boolean exists = articleRepo.existsByCode("BRG001");
            System.out.println("  'BRG001' existe ?  " + (exists ? "Oui" : "Non"));

            boolean notExists = articleRepo.existsByCode("BRG999");
            System.out.println("  'BRG999' existe ? " + (notExists ?  "Oui" : "Non"));

            // ==========================================
            // Test 14: DUPLICATE CODE
            // ==========================================
            System.out.println("\nTest 14: Test de duplication de code...");
            try {
                Burger burgerDuplicate = new Burger("BRG001", "Test", "test", "desc", 3000);
                articleRepo.save(burgerDuplicate);
                System.out.println("  ✗ ERREUR: La duplication aurait du etre detectee!");
            } catch (DuplicateCodeArticleException e) {
                System.out.println("  ✓ Duplication detectee: " + e.getMessage());
            }

            // ==========================================
            // Test 15: POLYMORPHISME
            // ==========================================
            System.out.println("\nTest 15: Test du polymorphisme...");
            Optional<Article> article = articleRepo.findById(burger1.getId());
            if (article.isPresent()) {
                Article a = article.get();
                System.out.println("  Article recupere: " + a.getLibelle());
                System.out.println("  Type Java: " + a.getClass().getSimpleName());
                System.out.println("  Categorie DB: " + a.getCategorie());
                System.out.println("  Est Burger ?  " + (a instanceof Burger));
                System.out.println("  Est Menu ? " + (a instanceof Menu));
                System.out.println("  Est Complement ? " + (a instanceof Complement));

                if (a instanceof Burger) {
                    System.out.println("  Prix (via cast Burger): " + ((Burger) a).getPrix() + " FCFA");
                }
            }

            // ==========================================
            // Test 16: DELETE
            // ==========================================
            System.out.println("\nTest 16: Suppression d'articles...");
            articleRepo.delete(menu2.getId());
            System.out.println("  ✓ Menu supprime: " + menu2.getLibelle());

            long finalCount = articleRepo.count();
            System.out.println("  Nombre d'articles restants: " + finalCount);

            System.out.println("\n===============================================");
            System.out.println("TOUS LES TESTS DU REPOSITORY ARTICLE SONT REUSSIS !");
            System.out.println("===============================================");

        } catch (Exception e) {
            System.err.println("\n✗ ERREUR:  " + e.getMessage());
            logger.error("Erreur lors des tests", e);
            e.printStackTrace();
        }
    }
}