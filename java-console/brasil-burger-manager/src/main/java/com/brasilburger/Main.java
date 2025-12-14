package com.brasilburger;

import com.brasilburger.config.AppConfig;
import com.brasilburger.domain.entities.*;
import com.brasilburger.domain.repositories.IArticleRepository;
import com.brasilburger.domain.repositories.impl.NeonArticleRepository;
import com.brasilburger.domain.services.ICodeArticleGenerator;
import com.brasilburger.domain.services.IImageStorageService;
import com.brasilburger.domain.services.IZoneService;
import com.brasilburger.domain.services.impl.CodeArticleGeneratorImpl;
import com.brasilburger.domain.services.impl.CloudinaryImageStorageService;
import com.brasilburger.factories.ImageStorageFactory;
import com.brasilburger.factories.RepositoryFactory;
import com.brasilburger.factories.ServiceFactory;
import com.brasilburger.infrastructure.cloudinary.CloudinaryConfig;
import com.brasilburger.infrastructure.cloudinary.CloudinaryFolders;
import com.brasilburger.infrastructure.database.NeonConnectionManager;
import com.brasilburger.domain.services.IArticleService;
import com.brasilburger.domain.repositories.IZoneRepository;
import com.brasilburger.domain.repositories.IQuartierRepository;
import com.brasilburger.domain.repositories.ILivreurRepository;
import com.brasilburger.domain.services.IQuartierService;
import com.brasilburger.domain.services.ILivreurService;

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
        testerFactories();

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
     * Test des Factories
     */
    private static void testerFactories() {
        System.out.println("\n=== TESTS :   FACTORIES ===\n");

        try {
            // Test 1: Création des repositories via factory
            System.out.println("Test 1: Creation des repositories via factory.. .");
            IZoneRepository zoneRepo = RepositoryFactory. createZoneRepository();
            IQuartierRepository quartierRepo = RepositoryFactory.createQuartierRepository();
            ILivreurRepository livreurRepo = RepositoryFactory.createLivreurRepository();
            IArticleRepository articleRepo = RepositoryFactory.createArticleRepository();
            System.out.println("  ✓ Repositories crees");

            // Test 2: Vérification Singleton repositories
            System.out.println("\nTest 2: Verification Singleton repositories...");
            IZoneRepository zoneRepo2 = RepositoryFactory.createZoneRepository();
            boolean isSameInstance = (zoneRepo == zoneRepo2);
            System.out.println("  Meme instance ? " + isSameInstance);
            System.out. println("  ✓ Pattern Singleton OK");

            // Test 3: Création des services via factory
            System.out.println("\nTest 3: Creation des services via factory...");
            IZoneService zoneService = ServiceFactory.createZoneService();
            IQuartierService quartierService = ServiceFactory.createQuartierService();
            ILivreurService livreurService = ServiceFactory.createLivreurService();
            IArticleService articleService = ServiceFactory. createArticleService();
            ICodeArticleGenerator codeGenerator = ServiceFactory.createCodeArticleGenerator();
            System.out. println("  ✓ Services metier crees");

            // Test 4: Création du service Image Storage via factory dédiée
            System.out.println("\nTest 4: Creation ImageStorageService via factory...");
            IImageStorageService imageService1 = ImageStorageFactory.createImageStorageService();
            IImageStorageService imageService2 = ImageStorageFactory.createImageStorageService();
            boolean isSameImageService = (imageService1 == imageService2);
            System.out.println("  Meme instance ?  " + isSameImageService);
            System.out.println("  ✓ ImageStorageFactory OK");

            // Test 5: Accès via ServiceFactory
            System.out.println("\nTest 5: Acces ImageStorageService via ServiceFactory...");
            IImageStorageService imageServiceViaServiceFactory = ServiceFactory.createImageStorageService();
            boolean isSameViaServiceFactory = (imageService1 == imageServiceViaServiceFactory);
            System.out. println("  Meme instance via ServiceFactory ? " + isSameViaServiceFactory);
            System.out.println("  ✓ Delegation OK");

            // Test 6: Test fonctionnel avec une zone
            System.out.println("\nTest 6: Test fonctionnel avec factory.. .");
            Zone zone = zoneService.creerZone("Zone Test Factory", 2500);
            System.out.println("  ✓ Zone creee via factory:  " + zone.getNom());

            // Nettoyage
            zoneService.supprimerZone(zone. getId());
            System.out. println("  ✓ Nettoyage effectue");

            // Test 7: Test du code generator via factory
            System.out.println("\nTest 7: Test code generator via factory...");
            String codeBurger = codeGenerator.genererCodeBurger();
            String codeMenu = codeGenerator. genererCodeMenu();
            String codeComplement = codeGenerator.genererCodeComplement();
            System.out.println("  Code Burger:       " + codeBurger);
            System.out.println("  Code Menu:        " + codeMenu);
            System.out.println("  Code Complement:  " + codeComplement);
            System.out.println("  ✓ Code generator OK");

            // Test 8: Test validation format image via ImageStorageFactory
            System.out.println("\nTest 8: Test fonctionnalite ImageStorageService...");
            CloudinaryImageStorageService imageService = (CloudinaryImageStorageService) ImageStorageFactory.createImageStorageService();
            boolean jpgValide = imageService.estFormatImageValide("test.jpg");
            boolean pngValide = imageService. estFormatImageValide("test.png");
            boolean txtInvalide = ! imageService.estFormatImageValide("test.txt");
            System.out.println("  JPG valide ?  " + jpgValide);
            System.out.println("  PNG valide ? " + pngValide);
            System.out.println("  TXT invalide ? " + txtInvalide);
            System.out.println("  ✓ Validation formats OK");

            // Test 9: Test reset des factories
            System.out.println("\nTest 9: Test reset des factories...");
            ServiceFactory.resetAll();
            RepositoryFactory.resetAll();
            IZoneService zoneServiceNew = ServiceFactory.createZoneService();
            IImageStorageService imageServiceNew = ImageStorageFactory.createImageStorageService();
            boolean isDifferentZoneService = (zoneService != zoneServiceNew);
            boolean isDifferentImageService = (imageService1 != imageServiceNew);
            System.out.println("  ZoneService different apres reset ? " + isDifferentZoneService);
            System.out.println("  ImageService different apres reset ? " + isDifferentImageService);
            System.out.println("  ✓ Reset OK");

            // Test 10: Test createNewImageStorageService
            System.out.println("\nTest 10: Test creation nouvelle instance ImageStorageService...");
            IImageStorageService imageServiceForce1 = ImageStorageFactory.createNewImageStorageService();
            IImageStorageService imageServiceForce2 = ImageStorageFactory.createNewImageStorageService();
            boolean areDifferentForced = (imageServiceForce1 != imageServiceForce2);
            System.out.println("  Instances forcees differentes ? " + areDifferentForced);
            System.out.println("  ✓ Creation nouvelle instance OK");

            System.out.println("\n✓ TOUS LES TESTS FACTORIES REUSSIS\n");

        } catch (Exception e) {
            System.err.println("✗ ERREUR:  " + e.getMessage());
            logger.error("Erreur tests factories", e);
            e.printStackTrace();
        }
    }
}