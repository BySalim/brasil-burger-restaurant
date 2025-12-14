package com.brasilburger;

import com.brasilburger.config.AppConfig;
import com.brasilburger.domain.entities.*;
import com.brasilburger.domain.entities.enums.TypeComplement;
import com.brasilburger.domain.repositories.IArticleRepository;
import com.brasilburger.domain.repositories.impl.NeonArticleRepository;
import com.brasilburger.domain.services.ICodeArticleGenerator;
import com.brasilburger.domain.services.IImageStorageService;
import com.brasilburger.domain.services.impl.CodeArticleGeneratorImpl;
import com.brasilburger.domain.services.impl.CloudinaryImageStorageService;
import com.brasilburger.infrastructure.cloudinary.CloudinaryConfig;
import com.brasilburger.infrastructure.cloudinary.CloudinaryFolders;
import com.brasilburger.infrastructure.database.NeonConnectionManager;
import com.brasilburger.domain.services.IArticleService;
import com.brasilburger.domain.services.impl.ArticleServiceImpl;
import com.brasilburger.domain.entities.enums.CategorieArticle;
import com.brasilburger.domain.valueobjects.ImageInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        afficherBanner();

        if (! AppConfig.validateConfiguration()) {
            logger.error("Configuration invalide!");
            System.exit(1);
        }

        if (!NeonConnectionManager.testConnection()) {
            logger.error("Impossible de se connecter a la base de donnees");
            System.exit(1);
        }

        if (!CloudinaryConfig.testConnection()) {
            logger.error("Impossible de se connecter a Cloudinary");
            System.exit(1);
        }

        logger.info("Configuration validee - Connexions etablies");

        // Tests unitaires
        testerServiceImageStorage();
        testerServiceArticle();

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
     * Test des services Image Storage et Code Generator
     */
    private static void testerServiceImageStorage() {
        System.out.println("\n=== TESTS UNITAIRES : SERVICE IMAGE STORAGE ===\n");

        IArticleRepository articleRepo = new NeonArticleRepository();
        IImageStorageService imageService = new CloudinaryImageStorageService();
        ICodeArticleGenerator codeGenerator = new CodeArticleGeneratorImpl(articleRepo);

        try {
            System.out.println("Test 1: Configuration.. .");
            Map<String, Object> config = CloudinaryConfig.getConfigInfo();
            System.out.println("  Cloud:  " + config.get("cloud_name"));
            System.out.println("  ✓ Configuration OK");

            System.out.println("\nTest 2: Dossiers.. .");
            System.out.println("  Burgers: " + CloudinaryFolders.getBurgers());
            System.out.println("  ✓ Dossiers OK");

            System.out.println("\nTest 3: Generateur de codes...");
            String codeBurger = codeGenerator.genererCodeBurger();
            String codeMenu = codeGenerator.genererCodeMenu();
            String codeComplement = codeGenerator.genererCodeComplement();
            System.out.println("  Code Burger:       " + codeBurger);
            System.out.println("  Code Menu:        " + codeMenu);
            System.out.println("  Code Complement:  " + codeComplement);
            System.out.println("  ✓ Generateur OK");

            System.out.println("\nTest 4: Generation URL...");
            String testId = CloudinaryFolders.getBurgers() + "/test";
            String url = imageService.getImageUrl(testId);
            System.out.println("  URL:  " + url);
            System.out.println("  ✓ URL OK");

            System.out.println("\nTest 5: Validation format...");
            CloudinaryImageStorageService service = (CloudinaryImageStorageService) imageService;
            assert service.estFormatImageValide("test.jpg");
            assert service.estFormatImageValide("test.png");
            assert ! service.estFormatImageValide("test.txt");
            System.out.println("  ✓ Validation OK");

            System.out.println("\n✓ TOUS LES TESTS REUSSIS\n");

        } catch (Exception e) {
            System.err.println("✗ ERREUR: " + e.getMessage());
            logger.error("Erreur tests", e);
        }
    }

    /**
     * Test INTERACTIF du service Article avec upload d'images
     */
    private static void testerServiceArticle() {
        System.out.println("\n=== TESTS INTERACTIFS :  SERVICE ARTICLE ===\n");

        IArticleRepository articleRepo = new NeonArticleRepository();
        ICodeArticleGenerator codeGenerator = new CodeArticleGeneratorImpl(articleRepo);
        IArticleService articleService = new ArticleServiceImpl(articleRepo, codeGenerator);
        IImageStorageService imageService = new CloudinaryImageStorageService();

        System.out.println("NOTE:  Les images sont OBLIGATOIRES pour tous les articles\n");

        try {
            // Test 1: Création d'un burger avec image
            System.out.println("=== Test 1: Creation d'un burger avec image ===");
            System.out.print("Voulez-vous creer un burger ?  (o/n): ");
            if (scanner.nextLine().trim().equalsIgnoreCase("o")) {

                System.out.print("Libelle du burger: ");
                String libelle = scanner.nextLine().trim();

                System. out.print("Description: ");
                String description = scanner. nextLine().trim();

                System.out.print("Prix (FCFA): ");
                int prix = Integer.parseInt(scanner.nextLine().trim());

                System. out.print("Chemin de l'image:  ");
                String cheminImage = scanner.nextLine().trim();

                try {
                    // Upload de l'image
                    ImageInfo imageInfo = imageService.uploadImage(cheminImage, CloudinaryFolders.getBurgers());
                    System.out.println("  ✓ Image uploadee:  " + imageInfo.getPublicId());

                    // Création du burger
                    Burger burger = articleService.creerBurger(libelle, description, prix, imageInfo.getPublicId());
                    System.out.println("  ✓ Burger cree: " + burger.getCode() + " - " + burger.getLibelle());
                    System.out.println("    Image: " + burger.getImagePublicId());

                } catch (Exception e) {
                    System.out.println("  ✗ Erreur:  " + e.getMessage());
                }
            }

            // Test 2: Création d'un complément avec image
            System.out.println("\n=== Test 2: Creation d'un complement avec image ===");
            System.out.print("Voulez-vous creer un complement ? (o/n): ");
            if (scanner.nextLine().trim().equalsIgnoreCase("o")) {

                System.out.print("Libelle du complement: ");
                String libelle = scanner.nextLine().trim();

                System.out.print("Type (1=Boisson, 2=Frites): ");
                TypeComplement type = scanner.nextLine().trim().equals("1")
                        ? TypeComplement. BOISSON : TypeComplement. FRITES;

                System.out.print("Prix (FCFA): ");
                int prix = Integer.parseInt(scanner.nextLine().trim());

                System.out.print("Chemin de l'image: ");
                String cheminImage = scanner. nextLine().trim();

                try {
                    // Upload de l'image
                    ImageInfo imageInfo = imageService.uploadImage(cheminImage, CloudinaryFolders.getComplements());
                    System.out.println("  ✓ Image uploadee: " + imageInfo.getPublicId());

                    // Création du complément
                    Complement complement = articleService.creerComplement(libelle, type, prix, imageInfo.getPublicId());
                    System.out.println("  ✓ Complement cree:  " + complement.getCode() + " - " + complement.getLibelle());
                    System.out. println("    Image: " + complement.getImagePublicId());

                } catch (Exception e) {
                    System.out. println("  ✗ Erreur: " + e.getMessage());
                }
            }

            // Test 3: Création d'un menu avec image
            System.out.println("\n=== Test 3: Creation d'un menu avec image ===");
            System.out.print("Voulez-vous creer un menu ? (o/n): ");
            if (scanner.nextLine().trim().equalsIgnoreCase("o")) {

                System.out.print("Libelle du menu: ");
                String libelle = scanner.nextLine().trim();

                System.out.print("Description: ");
                String description = scanner.nextLine().trim();

                System.out.print("Chemin de l'image: ");
                String cheminImage = scanner.nextLine().trim();

                try {
                    // Upload de l'image
                    ImageInfo imageInfo = imageService.uploadImage(cheminImage, CloudinaryFolders.getMenus());
                    System.out.println("  ✓ Image uploadee:  " + imageInfo.getPublicId());

                    // Création du menu
                    Menu menu = articleService.creerMenu(libelle, description, imageInfo.getPublicId());
                    System. out.println("  ✓ Menu cree: " + menu.getCode() + " - " + menu.getLibelle());
                    System.out.println("    Image: " + menu.getImagePublicId());

                } catch (Exception e) {
                    System.out.println("  ✗ Erreur:  " + e.getMessage());
                }
            }

            // Test 4: Liste des articles créés
            System.out.println("\n=== Test 4: Liste de tous les articles ===");
            List<Article> articles = articleService. listerTousLesArticles();
            System.out.println("Nombre total d'articles: " + articles. size());

            if (! articles.isEmpty()) {
                System.out.println("\nArticles:");
                for (Article a : articles) {
                    System. out.println("  - " + a.getCode() + ": " + a.getLibelle() + " (" + a.getCategorie() + ")");
                    System.out.println("    Image: " + a.getImagePublicId());
                }
            }

            // Test 5: Test de validation (image obligatoire)
            System.out.println("\n=== Test 5: Validation image obligatoire ===");
            try {
                articleService.creerBurger("Test Burger", "Description", 5000, null);
                System. out.println("  ✗ ERREUR: devrait lancer une exception");
            } catch (IllegalArgumentException e) {
                System.out.println("  ✓ Exception correctement levee: " + e.getMessage());
            }

            // Test 6: Comptage par catégorie
            System.out.println("\n=== Test 6: Comptage par categorie ===");
            long nbBurgers = articleService.compterArticlesParCategorie(CategorieArticle.BURGER);
            long nbMenus = articleService.compterArticlesParCategorie(CategorieArticle.MENU);
            long nbComplements = articleService.compterArticlesParCategorie(CategorieArticle.COMPLEMENT);
            System.out.println("  Burgers: " + nbBurgers);
            System.out.println("  Menus: " + nbMenus);
            System.out.println("  Complements: " + nbComplements);

            System.out.println("\n✓ TOUS LES TESTS TERMINES\n");

        } catch (Exception e) {
            System.err.println("✗ ERREUR: " + e.getMessage());
            logger.error("Erreur tests service article", e);
            e.printStackTrace();
        }
    }


}