package com.brasilburger;

import com.brasilburger.config.AppConfig;
import com.brasilburger.domain.entities.*;
import com.brasilburger.domain.entities.enums.TypeComplement;
import com.brasilburger.domain.repositories.IArticleRepository;
import com.brasilburger.domain.repositories.impl.NeonArticleRepository;
import com.brasilburger.domain.services.IImageStorageService;
import com.brasilburger.domain.services.impl.CloudinaryImageStorageService;
import com.brasilburger.domain.valueobjects.ImageInfo;
import com.brasilburger.infrastructure.cloudinary.CloudinaryConfig;
import com.brasilburger.infrastructure.cloudinary.CloudinaryFolders;
import com.brasilburger.infrastructure.database.NeonConnectionManager;
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

        if (! NeonConnectionManager.testConnection()) {
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

        // Tests interactifs
        System.out.print("\nVoulez-vous lancer les tests interactifs ? (o/n): ");
        String reponse = scanner.nextLine().trim().toLowerCase();
        if (reponse.equals("o") || reponse.equals("oui")) {
            testerArticlesAvecImages();
        }

        logger.info("Tests termines avec succes!");
    }

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

    private static void testerServiceImageStorage() {
        System.out.println("\n=== TESTS UNITAIRES :  SERVICE IMAGE STORAGE ===\n");

        IImageStorageService imageService = new CloudinaryImageStorageService();

        try {
            System.out.println("Test 1: Configuration.. .");
            Map<String, Object> config = CloudinaryConfig.getConfigInfo();
            System.out.println("  Cloud:  " + config.get("cloud_name"));
            System.out.println("  ✓ Configuration OK");

            System.out.println("\nTest 2: Dossiers.. .");
            System.out.println("  Burgers: " + CloudinaryFolders.getBurgers());
            System.out.println("  ✓ Dossiers OK");

            System.out.println("\nTest 3: Generation URL...");
            String testId = CloudinaryFolders.getBurgers() + "/test";
            String url = imageService.getImageUrl(testId);
            System.out.println("  URL:  " + url);
            System.out.println("  ✓ URL OK");

            System.out.println("\nTest 4: Validation format...");
            CloudinaryImageStorageService service = (CloudinaryImageStorageService) imageService;
            assert service.estFormatImageValide("test.jpg");
            assert service.estFormatImageValide("test.png");
            assert ! service.estFormatImageValide("test.txt");
            System.out.println("  ✓ Validation OK");

            System.out.println("\n✓ TOUS LES TESTS REUSSIS\n");

        } catch (Exception e) {
            System.err.println("✗ ERREUR:  " + e.getMessage());
            logger.error("Erreur tests", e);
        }
    }

    private static void testerArticlesAvecImages() {
        System.out.println("\n=== TESTS INTERACTIFS ===\n");

        IArticleRepository articleRepo = new NeonArticleRepository();
        IImageStorageService imageService = new CloudinaryImageStorageService();

        while (true) {
            System.out.println("\n=== MENU ===");
            System.out.println("1.Creer burger");
            System.out.println("2.Creer complement");
            System.out.println("3.Creer menu");
            System.out.println("4.Lister articles");
            System.out.println("5.Rechercher article");
            System.out.println("6.Modifier article");
            System.out.println("7.Supprimer article");
            System.out.println("8.Lister images");
            System.out.println("0.Quitter");
            System.out.print("\nChoix: ");

            String choix = scanner.nextLine().trim();

            switch (choix) {
                case "1":  creerBurger(articleRepo, imageService); break;
                case "2": creerComplement(articleRepo, imageService); break;
                case "3":  creerMenu(articleRepo, imageService); break;
                case "4": listerArticles(articleRepo); break;
                case "5":  rechercherArticle(articleRepo); break;
                case "6": modifierArticle(articleRepo, imageService); break;
                case "7": supprimerArticle(articleRepo, imageService); break;
                case "8": listerImages(imageService); break;
                case "0": return;
                default: System.out.println("Choix invalide!");
            }
        }
    }

    private static void creerBurger(IArticleRepository repo, IImageStorageService imageService) {
        System.out.println("\n=== CREER BURGER ===");

        try {
            System.out.print("Code:  ");
            String code = scanner.nextLine().trim();

            System.out.print("Libelle: ");
            String libelle = scanner.nextLine().trim();

            System.out.print("Description: ");
            String description = scanner.nextLine().trim();

            System.out.print("Prix: ");
            int prix = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Chemin image (ENTER pour sauter): ");
            String chemin = scanner.nextLine().trim();

            String publicId = null;
            if (!chemin.isEmpty()) {
                try {
                    ImageInfo img = imageService.uploadImage(chemin, CloudinaryFolders.getBurgers());
                    publicId = img.getPublicId();
                    System.out.println("  ✓ Image uploadee");
                } catch (Exception e) {
                    System.out.println("  ✗ " + e.getMessage());
                }
            }

            Burger burger = new Burger(code, libelle, publicId, description, prix);
            repo.save(burger);
            System.out.println("\n✓ BURGER CREE!");

        } catch (Exception e) {
            System.out.println("✗ ERREUR: " + e.getMessage());
        }
    }

    private static void creerComplement(IArticleRepository repo, IImageStorageService imageService) {
        System.out.println("\n=== CREER COMPLEMENT ===");

        try {
            System.out.print("Code: ");
            String code = scanner.nextLine().trim();

            System.out.print("Libelle: ");
            String libelle = scanner.nextLine().trim();

            System.out.print("Type (1=Boisson, 2=Frites): ");
            TypeComplement type = scanner.nextLine().trim().equals("1") ?
                    TypeComplement.BOISSON : TypeComplement.FRITES;

            System.out.print("Prix: ");
            int prix = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Chemin image (ENTER pour sauter): ");
            String chemin = scanner.nextLine().trim();

            String publicId = null;
            if (!chemin.isEmpty()) {
                try {
                    ImageInfo img = imageService.uploadImage(chemin, CloudinaryFolders.getComplements());
                    publicId = img.getPublicId();
                    System.out.println("  ✓ Image uploadee");
                } catch (Exception e) {
                    System.out.println("  ✗ " + e.getMessage());
                }
            }

            Complement complement = new Complement(code, libelle, publicId, type, prix);
            repo.save(complement);
            System.out.println("\n✓ COMPLEMENT CREE!");

        } catch (Exception e) {
            System.out.println("✗ ERREUR: " + e.getMessage());
        }
    }

    private static void creerMenu(IArticleRepository repo, IImageStorageService imageService) {
        System.out.println("\n=== CREER MENU ===");

        try {
            System.out.print("Code: ");
            String code = scanner.nextLine().trim();

            System.out.print("Libelle: ");
            String libelle = scanner.nextLine().trim();

            System.out.print("Description: ");
            String description = scanner.nextLine().trim();

            System.out.print("Chemin image (ENTER pour sauter): ");
            String chemin = scanner.nextLine().trim();

            String publicId = null;
            if (!chemin.isEmpty()) {
                try {
                    ImageInfo img = imageService.uploadImage(chemin, CloudinaryFolders.getMenus());
                    publicId = img.getPublicId();
                    System.out.println("  ✓ Image uploadee");
                } catch (Exception e) {
                    System.out.println("  ✗ " + e.getMessage());
                }
            }

            Menu menu = new Menu(code, libelle, publicId, description);
            repo.save(menu);
            System.out.println("\n✓ MENU CREE!");

        } catch (Exception e) {
            System.out.println("✗ ERREUR: " + e.getMessage());
        }
    }

    private static void listerArticles(IArticleRepository repo) {
        System.out.println("\n=== LISTE ARTICLES ===");
        List<Article> articles = repo.findAll();

        if (articles.isEmpty()) {
            System.out.println("Aucun article");
            return;
        }

        for (Article a : articles) {
            System.out.println("\n" + a.getCode() + " - " + a.getLibelle());
            System.out.println("  Type: " + a.getCategorie());
            if (a.getImagePublicId() != null) {
                System.out.println("  Image: " + a.getImagePublicId());
            }
        }
    }

    private static void rechercherArticle(IArticleRepository repo) {
        System.out.print("\nCode article: ");
        String code = scanner.nextLine().trim();

        Optional<Article> opt = repo.findByCode(code);
        if (opt.isPresent()) {
            Article a = opt.get();
            System.out.println("\n✓ TROUVE:");
            System.out.println("  " + a.getLibelle());
            System.out.println("  " + a.getCategorie());
            if (a.getImagePublicId() != null) {
                System.out.println("  Image: " + a.getImagePublicId());
            }
        } else {
            System.out.println("✗ Non trouve");
        }
    }

    private static void modifierArticle(IArticleRepository repo, IImageStorageService imageService) {
        System.out.print("\nCode article: ");
        String code = scanner.nextLine().trim();

        Optional<Article> opt = repo.findByCode(code);
        if (!opt.isPresent()) {
            System.out.println("✗ Non trouve");
            return;
        }

        Article article = opt.get();
        System.out.println("Article:  " + article.getLibelle());

        System.out.print("Nouveau libelle (ENTER pour garder): ");
        String libelle = scanner.nextLine().trim();
        if (!libelle.isEmpty()) {
            article.modifierLibelle(libelle);
        }

        System.out.print("Changer image?  (o/n): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("o")) {
            System.out.print("Chemin:  ");
            String chemin = scanner.nextLine().trim();

            try {
                if (article.getImagePublicId() != null) {
                    imageService.deleteImage(article.getImagePublicId());
                }

                String folder = CloudinaryFolders.getSubfolderForArticle(article.getCategorie().name());
                ImageInfo img = imageService.uploadImage(chemin, folder);
                article.setImagePublicId(img.getPublicId());
                System.out.println("  ✓ Image changee");
            } catch (Exception e) {
                System.out.println("  ✗ " + e.getMessage());
            }
        }

        repo.save(article);
        System.out.println("✓ Modifie");
    }

    private static void supprimerArticle(IArticleRepository repo, IImageStorageService imageService) {
        System.out.print("\nCode article: ");
        String code = scanner.nextLine().trim();

        Optional<Article> opt = repo.findByCode(code);
        if (!opt.isPresent()) {
            System.out.println("✗ Non trouve");
            return;
        }

        Article article = opt.get();
        System.out.print("Confirmer suppression de '" + article.getLibelle() + "' ?  (o/n): ");

        if (scanner.nextLine().trim().equalsIgnoreCase("o")) {
            if (article.getImagePublicId() != null) {
                imageService.deleteImage(article.getImagePublicId());
            }
            repo.delete(article.getId());
            System.out.println("✓ Supprime");
        }
    }

    private static void listerImages(IImageStorageService imageService) {
        System.out.println("\n1.Burgers");
        System.out.println("2.Menus");
        System.out.println("3.Complements");
        System.out.print("Choix: ");

        String folder;
        switch (scanner.nextLine().trim()) {
            case "1":  folder = CloudinaryFolders.getBurgers(); break;
            case "2": folder = CloudinaryFolders.getMenus(); break;
            case "3": folder = CloudinaryFolders.getComplements(); break;
            default: folder = CloudinaryFolders.getArticlesBase();
        }

        List<ImageInfo> images = imageService.listImages(folder);
        System.out.println("\n" + images.size() + " image(s):");
        images.forEach(img -> System.out.println("  - " + img.getPublicId()));
    }
}