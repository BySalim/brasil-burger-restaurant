package com.brasilburger;

import com.brasilburger.config.AppConfig;
import com.brasilburger.domain.entities.*;
import com.brasilburger.domain.entities.enums.TypeComplement;
import com.brasilburger.domain.repositories.IArticleRepository;
import com.brasilburger.domain.repositories.ILivreurRepository;
import com.brasilburger.domain.repositories.IQuartierRepository;
import com.brasilburger.domain.repositories.IZoneRepository;
import com.brasilburger.domain.repositories.impl.NeonArticleRepository;
import com.brasilburger.domain.repositories.impl.NeonLivreurRepository;
import com.brasilburger.domain.repositories.impl.NeonQuartierRepository;
import com.brasilburger.domain.repositories.impl.NeonZoneRepository;
import com.brasilburger.domain.services.ICodeArticleGenerator;
import com.brasilburger.domain.services.IImageStorageService;
import com.brasilburger.domain.services.ILivreurService;
import com.brasilburger.domain.services.IQuartierService;
import com.brasilburger.domain.services.IZoneService;
import com.brasilburger.domain.services.impl.CodeArticleGeneratorImpl;
import com.brasilburger.domain.services.impl.CloudinaryImageStorageService;
import com.brasilburger.domain.services.impl.LivreurServiceImpl;
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
        testerServiceLivreur();

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
     * Test du service Livreur
     */
    private static void testerServiceLivreur() {
        System.out.println("\n=== TESTS : SERVICE LIVREUR ===\n");

        ILivreurRepository livreurRepo = new NeonLivreurRepository();
        ILivreurService livreurService = new LivreurServiceImpl(livreurRepo);

        try {
            // Test 1: Création de livreurs
            System.out.println("Test 1: Creation de livreurs via service...");
            Livreur l1 = livreurService.creerLivreur("Diop", "Moussa", "771234567");
            Livreur l2 = livreurService.creerLivreur("Fall", "Aminata", "772345678");
            Livreur l3 = livreurService.creerLivreur("Ndiaye", "Ibrahima", "773456789");
            System.out.println("  ✓ Livreurs crees: " + l1.getNomComplet() + ", " +
                    l2.getNomComplet() + ", " + l3.getNomComplet());

            // Test 2: Liste des livreurs disponibles
            System.out.println("\nTest 2: Liste des livreurs disponibles...");
            List<Livreur> disponibles = livreurService.listerLivreursDisponibles();
            System.out.println("  Nombre de livreurs disponibles: " + disponibles.size());
            disponibles.forEach(l -> System.out.println("    - " + l.getNomComplet()));

            // Test 3: Marquer un livreur comme occupé
            System.out.println("\nTest 3: Marquer un livreur comme occupe...");
            livreurService.marquerOccupe(l1.getId());
            System.out.println("  ✓ Livreur marque occupe:  " + l1.getNomComplet());

            // Test 4: Vérification disponibilité
            System.out.println("\nTest 4: Verification disponibilite...");
            disponibles = livreurService.listerLivreursDisponibles();
            List<Livreur> occupes = livreurService.listerLivreursOccupes();
            System.out.println("  Disponibles: " + disponibles.size());
            System.out.println("  Occupes: " + occupes.size());

            // Test 5: Rendre disponible
            System.out.println("\nTest 5: Rendre un livreur disponible...");
            livreurService.marquerDisponible(l1.getId());
            System.out.println("  ✓ Livreur marque disponible: " + l1.getNomComplet());

            // Test 6: Modification d'un livreur
            System.out.println("\nTest 6: Modification d'un livreur...");
            livreurService.modifierLivreur(l2.getId(), null, null, "779999999");
            Livreur l2Modifie = livreurService.obtenirLivreurParId(l2.getId()).get();
            System.out.println("  ✓ Telephone modifie: " + l2Modifie.getTelephone());

            // Test 7: Archivage
            System.out.println("\nTest 7: Archivage d'un livreur...");
            livreurService.archiverLivreur(l3.getId());
            System.out.println("  ✓ Livreur archive:  " + l3.getNomComplet());

            // Test 8: Vérification peut être affecté
            System.out.println("\nTest 8: Verification affectation possible...");
            boolean l1PeutEtreAffecte = livreurService.peutEtreAffecte(l1.getId());
            boolean l3PeutEtreAffecte = livreurService.peutEtreAffecte(l3.getId());
            System.out.println("  " + l1.getNomComplet() + " peut etre affecte ? " + l1PeutEtreAffecte);
            System.out.println("  " + l3.getNomComplet() + " peut etre affecte ? " + l3PeutEtreAffecte);

            // Test 9: Tentative de marquer disponible un livreur archivé
            System.out.println("\nTest 9: Tentative marquer disponible un livreur archive...");
            try {
                livreurService.marquerDisponible(l3.getId());
                System.out.println("  ✗ ERREUR: devrait lancer une exception");
            } catch (IllegalStateException e) {
                System.out.println("  ✓ Exception correctement levee: " + e.getMessage());
            }

            // Test 10: Restauration
            System.out.println("\nTest 10: Restauration d'un livreur...");
            livreurService.restaurerLivreur(l3.getId());
            Livreur l3Restaure = livreurService.obtenirLivreurParId(l3.getId()).get();
            System.out.println("  ✓ Livreur restaure: " + l3Restaure.getNomComplet());
            System.out.println("    Peut etre affecte maintenant ? " + l3Restaure.peutEtreAffecte());

            // Test 11: Recherche par téléphone
            System.out.println("\nTest 11: Recherche par telephone...");
            Optional<Livreur> trouve = livreurService.obtenirLivreurParTelephone("779999999");
            if (trouve.isPresent()) {
                System.out.println("  ✓ Livreur trouve:  " + trouve.get().getNomComplet());
            }

            // Test 12: Vérification existence
            System.out.println("\nTest 12: Verification existence...");
            boolean existe = livreurService.livreurExiste("779999999");
            boolean nexistePas = livreurService.livreurExiste("770000000");
            System.out.println("  779999999 existe ? " + existe);
            System.out.println("  770000000 existe ? " + nexistePas);

            // Test 13: Comptage
            System.out.println("\nTest 13: Comptage...");
            long nbTotal = livreurService.compterLivreurs();
            long nbDisponibles = livreurService.compterLivreursDisponibles();
            System.out.println("  Nombre total de livreurs: " + nbTotal);
            System.out.println("  Nombre de livreurs disponibles: " + nbDisponibles);

            // Test 14: Tentative de créer avec téléphone existant
            System.out.println("\nTest 14: Tentative creation avec telephone existant...");
            try {
                livreurService.creerLivreur("Test", "Test", "771234567");
                System.out.println("  ✗ ERREUR: devrait lancer une exception");
            } catch (IllegalArgumentException e) {
                System.out.println("  ✓ Exception correctement levee: " + e.getMessage());
            }

            // Test 15: Validation format téléphone
            System.out.println("\nTest 15: Validation format telephone...");
            try {
                livreurService.creerLivreur("Test", "Test", "123");
                System.out.println("  ✗ ERREUR: devrait lancer une exception");
            } catch (IllegalArgumentException e) {
                System.out.println("  ✓ Exception correctement levee: " + e.getMessage());
            }

            // Test 16: Nettoyage
            System.out.println("\nTest 16: Nettoyage...");
            livreurService.supprimerLivreur(l1.getId());
            livreurService.supprimerLivreur(l2.getId());
            livreurService.supprimerLivreur(l3.getId());
            System.out.println("  ✓ Nettoyage effectue");

            System.out.println("\n✓ TOUS LES TESTS SERVICE LIVREUR REUSSIS\n");

        } catch (Exception e) {
            System.err.println("✗ ERREUR: " + e.getMessage());
            logger.error("Erreur tests service livreur", e);
            e.printStackTrace();
        }
    }

}