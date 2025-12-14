package com.brasilburger;

import com.brasilburger.config.AppConfig;
import com.brasilburger.domain.entities.*;
import com.brasilburger.domain.entities.enums.TypeComplement;
import com.brasilburger.domain.repositories.IArticleRepository;
import com.brasilburger.domain.repositories.IQuartierRepository;
import com.brasilburger.domain.repositories.IZoneRepository;
import com.brasilburger.domain.repositories.impl.NeonArticleRepository;
import com.brasilburger.domain.repositories.impl.NeonQuartierRepository;
import com.brasilburger.domain.repositories.impl.NeonZoneRepository;
import com.brasilburger.domain.services.ICodeArticleGenerator;
import com.brasilburger.domain.services.IImageStorageService;
import com.brasilburger.domain.services.IQuartierService;
import com.brasilburger.domain.services.IZoneService;
import com.brasilburger.domain.services.impl.CodeArticleGeneratorImpl;
import com.brasilburger.domain.services.impl.CloudinaryImageStorageService;
import com.brasilburger.domain.services.impl.QuartierServiceImpl;
import com.brasilburger.domain.services.impl.ZoneServiceImpl;
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
        testerServicesZoneEtQuartier();

        // Tests interactifs
        System.out.print("\nVoulez-vous lancer les tests interactifs ?  (o/n): ");
        String reponse = scanner.nextLine().trim().toLowerCase();
        if (reponse.equals("o") || reponse.equals("oui")) {
            testerArticlesAvecImages();
        }

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
            System.out.println("  URL: " + url);
            System.out.println("  ✓ URL OK");

            System.out.println("\nTest 5: Validation format.. .");
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

    /**
     * Test des services Zone et Quartier
     */
    private static void testerServicesZoneEtQuartier() {
        System.out.println("\n=== TESTS :  SERVICES ZONE ET QUARTIER ===\n");

        IZoneRepository zoneRepo = new NeonZoneRepository();
        IQuartierRepository quartierRepo = new NeonQuartierRepository();

        IZoneService zoneService = new ZoneServiceImpl(zoneRepo);
        IQuartierService quartierService = new QuartierServiceImpl(quartierRepo, zoneRepo);

        try {
            // Test Zone Service
            System.out.println("Test 1: Creation de zones via service.. .");
            Zone zone1 = zoneService.creerZone("Plateau Service", 2000);
            Zone zone2 = zoneService.creerZone("Parcelles Service", 3000);
            System.out.println("  ✓ Zones creees:  " + zone1.getNom() + ", " + zone2.getNom());

            System.out.println("\nTest 2: Liste des zones actives...");
            List<Zone> zonesActives = zoneService.listerZonesActives();
            System.out.println("  Nombre de zones actives: " + zonesActives.size());

            System.out.println("\nTest 3: Archivage d'une zone.. .");
            zoneService.archiverZone(zone2.getId());
            System.out.println("  ✓ Zone archivee");

            System.out.println("\nTest 4: Restauration d'une zone...");
            zoneService.restaurerZone(zone2.getId());
            System.out.println("  ✓ Zone restauree");

            System.out.println("\nTest 5: Modification d'une zone...");
            zoneService.modifierZone(zone1.getId(), "Plateau Modifie", 2500);
            Zone zoneModifiee = zoneService.obtenirZoneParId(zone1.getId()).get();
            System.out.println("  ✓ Zone modifiee:  " + zoneModifiee.getNom() + " - " + zoneModifiee.getPrixLivraison() + " FCFA");

            System.out.println("\nTest 6: Creation de quartiers via service...");
            Quartier q1 = quartierService.creerQuartier("Mermoz Service", zone1.getId());
            Quartier q2 = quartierService.creerQuartier("Point E Service", zone1.getId());
            Quartier q3 = quartierService.creerQuartier("Unite 10 Service", zone2.getId());
            System.out.println("  ✓ Quartiers crees: " + q1.getNom() + ", " + q2.getNom() + ", " + q3.getNom());

            System.out.println("\nTest 7: Quartiers par zone...");
            List<Quartier> quartiersZone1 = quartierService.listerQuartiersParZone(zone1.getId());
            List<Quartier> quartiersZone2 = quartierService.listerQuartiersParZone(zone2.getId());
            System.out.println("  Quartiers dans " + zone1.getNom() + ": " + quartiersZone1.size());
            System.out.println("  Quartiers dans " + zone2.getNom() + ": " + quartiersZone2.size());

            System.out.println("\nTest 8: Changement de zone.. .");
            quartierService.changerZoneQuartier(q2.getId(), zone2.getId());
            System.out.println("  ✓ Quartier deplace vers " + zone2.getNom());

            // Vérification
            quartiersZone1 = quartierService.listerQuartiersParZone(zone1.getId());
            quartiersZone2 = quartierService.listerQuartiersParZone(zone2.getId());
            System.out.println("  Quartiers dans " + zone1.getNom() + ": " + quartiersZone1.size());
            System.out.println("  Quartiers dans " + zone2.getNom() + ": " + quartiersZone2.size());

            System.out.println("\nTest 9: Modification d'un quartier...");
            quartierService.modifierQuartier(q1.getId(), "Mermoz Pyrotechnie Service");
            Quartier quartierModifie = quartierService.obtenirQuartierParId(q1.getId()).get();
            System.out.println("  ✓ Quartier modifie: " + quartierModifie.getNom());

            System.out.println("\nTest 10: Verification existence...");
            boolean zoneExiste = zoneService.zoneExiste(zone1.getNom());
            boolean quartierExiste = quartierService.quartierExiste(q1.getNom());
            System.out.println("  Zone existe ?  " + zoneExiste);
            System.out.println("  Quartier existe ? " + quartierExiste);

            System.out.println("\nTest 11: Comptage.. .");
            long nbZones = zoneService.compterZones();
            long nbQuartiers = quartierService.compterQuartiers();
            long nbQuartiersZone1 = quartierService.compterQuartiersParZone(zone1.getId());
            System.out.println("  Nombre total de zones: " + nbZones);
            System.out.println("  Nombre total de quartiers: " + nbQuartiers);
            System.out.println("  Quartiers dans zone 1: " + nbQuartiersZone1);

            System.out.println("\nTest 12: Nettoyage...");
            quartierService.supprimerQuartier(q1.getId());
            quartierService.supprimerQuartier(q2.getId());
            quartierService.supprimerQuartier(q3.getId());
            zoneService.supprimerZone(zone1.getId());
            zoneService.supprimerZone(zone2.getId());
            System.out.println("  ✓ Nettoyage effectue");

            System.out.println("\n✓ TOUS LES TESTS SERVICES REUSSIS\n");

        } catch (Exception e) {
            System.err.println("✗ ERREUR: " + e.getMessage());
            logger.error("Erreur tests services", e);
            e.printStackTrace();
        }
    }

    /**
     * Tests interactifs :  Articles avec images
     */
    private static void testerArticlesAvecImages() {
        System.out.println("\n=== TESTS INTERACTIFS ===\n");

        IArticleRepository articleRepo = new NeonArticleRepository();
        IImageStorageService imageService = new CloudinaryImageStorageService();
        ICodeArticleGenerator codeGenerator = new CodeArticleGeneratorImpl(articleRepo);

        while (true) {
            System.out.println("\n=== MENU ===");
            System.out.println("1.Creer burger (code auto)");
            System.out.println("2.Creer complement (code auto)");
            System.out.println("3.Creer menu (code auto)");
            System.out.println("4.Lister articles");
            System.out.println("5.Rechercher article");
            System.out.println("6.Modifier article");
            System.out.println("7.Supprimer article");
            System.out.println("8.Lister images");
            System.out.println("0.Quitter");
            System.out.print("\nChoix: ");

            String choix = scanner.nextLine().trim();

            switch (choix) {
                case "1":  creerBurger(articleRepo, imageService, codeGenerator); break;
                case "2": creerComplement(articleRepo, imageService, codeGenerator); break;
                case "3": creerMenu(articleRepo, imageService, codeGenerator); break;
                case "4": listerArticles(articleRepo); break;
                case "5":  rechercherArticle(articleRepo); break;
                case "6":  modifierArticle(articleRepo, imageService); break;
                case "7":  supprimerArticle(articleRepo, imageService); break;
                case "8": listerImages(imageService); break;
                case "0": return;
                default: System.out.println("Choix invalide!");
            }
        }
    }

    private static void creerBurger(IArticleRepository repo, IImageStorageService imageService,
                                    ICodeArticleGenerator codeGenerator) {
        System.out.println("\n=== CREER BURGER ===");

        try {
            String code = codeGenerator.genererCodeBurger();
            System.out.println("Code genere: " + code);

            System.out.print("Libelle: ");
            String libelle = scanner.nextLine().trim();

            System.out.print("Description: ");
            String description = scanner.nextLine().trim();

            System.out.print("Prix (FCFA): ");
            int prix = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Chemin image (ENTER pour sauter): ");
            String chemin = scanner.nextLine().trim();

            String publicId = null;
            if (! chemin.isEmpty()) {
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
            System.out.println("\n✓ BURGER CREE:  " + code + " - " + libelle);

        } catch (Exception e) {
            System.out.println("✗ ERREUR: " + e.getMessage());
        }
    }

    private static void creerComplement(IArticleRepository repo, IImageStorageService imageService,
                                        ICodeArticleGenerator codeGenerator) {
        System.out.println("\n=== CREER COMPLEMENT ===");

        try {
            String code = codeGenerator.genererCodeComplement();
            System.out.println("Code genere: " + code);

            System.out.print("Libelle: ");
            String libelle = scanner.nextLine().trim();

            System.out.print("Type (1=Boisson, 2=Frites): ");
            TypeComplement type = scanner.nextLine().trim().equals("1") ?
                    TypeComplement.BOISSON : TypeComplement.FRITES;

            System.out.print("Prix (FCFA): ");
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
            System.out.println("\n✓ COMPLEMENT CREE:  " + code + " - " + libelle);

        } catch (Exception e) {
            System.out.println("✗ ERREUR: " + e.getMessage());
        }
    }

    private static void creerMenu(IArticleRepository repo, IImageStorageService imageService,
                                  ICodeArticleGenerator codeGenerator) {
        System.out.println("\n=== CREER MENU ===");

        try {
            String code = codeGenerator.genererCodeMenu();
            System.out.println("Code genere: " + code);

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
            System.out.println("\n✓ MENU CREE: " + code + " - " + libelle);

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

        System.out.println("\nTotal:  " + articles.size() + " article(s)\n");

        for (Article a : articles) {
            System.out.println(a.getCode() + " - " + a.getLibelle());
            System.out.println("  Type: " + a.getCategorie());
            if (a.getImagePublicId() != null) {
                System.out.println("  Image:  Oui");
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
            System.out.println("  Code: " + a.getCode());
            System.out.println("  Libelle: " + a.getLibelle());
            System.out.println("  Type: " + a.getCategorie());
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
            case "1": folder = CloudinaryFolders.getBurgers(); break;
            case "2": folder = CloudinaryFolders.getMenus(); break;
            case "3": folder = CloudinaryFolders.getComplements(); break;
            default: folder = CloudinaryFolders.getArticlesBase();
        }

        List<ImageInfo> images = imageService.listImages(folder);
        System.out.println("\n" + images.size() + " image(s):");
        images.forEach(img -> System.out.println("  - " + img.getPublicId()));
    }
}