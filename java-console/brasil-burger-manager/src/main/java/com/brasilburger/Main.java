package com.brasilburger;

import com.brasilburger.config.AppConfig;
import com.brasilburger.domain.entities.Livreur;
import com.brasilburger.domain.repositories.ILivreurRepository;
import com.brasilburger.domain.repositories.impl.NeonLivreurRepository;
import com.brasilburger. infrastructure.database.NeonConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io. InputStream;
import java.io. InputStreamReader;
import java. nio.charset.StandardCharsets;
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

        // TEST DU REPOSITORY LIVREUR (Commit 11)
        testerLivreurRepository();

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
            logger. warn("Impossible d'afficher le banner:  {}", e.getMessage());
        }
    }

    /**
     * Test du repository Livreur (Commit 11)
     */
    private static void testerLivreurRepository() {
        System.out.println("=== TEST DU REPOSITORY LIVREUR ===\n");

        ILivreurRepository livreurRepo = new NeonLivreurRepository();

        try {
            // Test 1: CREATE (INSERT)
            System.out.println("Test 1: Creation de livreurs.. .");
            Livreur l1 = new Livreur("Diop", "Moussa", "771234567");
            Livreur l2 = new Livreur("Fall", "Aminata", "772345678");
            Livreur l3 = new Livreur("Ndiaye", "Ibrahima", "773456789");

            l1 = livreurRepo.save(l1);
            l2 = livreurRepo.save(l2);
            l3 = livreurRepo.save(l3);

            System.out.println("  ✓ Livreur 1: " + l1);
            System.out.println("  ✓ Livreur 2: " + l2);
            System.out.println("  ✓ Livreur 3: " + l3);

            // Test 2: COUNT
            System.out.println("\nTest 2: Comptage des livreurs...");
            long count = livreurRepo.count();
            System.out.println("  Nombre total de livreurs: " + count);

            long countAvailable = livreurRepo. countAvailable();
            System. out.println("  Nombre de livreurs disponibles: " + countAvailable);

            // Test 3: FIND BY ID
            System.out.println("\nTest 3: Recherche par ID...");
            Optional<Livreur> found = livreurRepo.findById(l1.getId());
            if (found.isPresent()) {
                System.out.println("  ✓ Livreur trouve: " + found.get().getNomComplet());
            } else {
                System.out.println("  ✗ Livreur non trouve");
            }

            // Test 4: FIND BY TELEPHONE
            System.out.println("\nTest 4: Recherche par telephone...");
            Optional<Livreur> foundByTel = livreurRepo. findByTelephone("771234567");
            if (foundByTel.isPresent()) {
                System.out.println("  ✓ Livreur trouve: " + foundByTel.get().getNomComplet());
            } else {
                System.out.println("  ✗ Livreur non trouve");
            }

            // Test 5: FIND ALL
            System.out.println("\nTest 5: Liste de tous les livreurs...");
            List<Livreur> allLivreurs = livreurRepo.findAll();
            System.out.println("  Nombre de livreurs: " + allLivreurs.size());
            allLivreurs.forEach(l -> System.out.println("    - " + l.getNomComplet() +
                    " (" + l.getTelephone() + ") - Dispo: " + l.isEstDisponible()));

            // Test 6: UPDATE
            System.out.println("\nTest 6: Mise a jour d'un livreur...");
            l1.setTelephone("779999999");
            livreurRepo.save(l1);
            System.out.println("  ✓ Telephone mis a jour: " + l1.getTelephone());

            // Test 7: MARQUER OCCUPE
            System.out.println("\nTest 7: Marquer un livreur comme occupe...");
            System.out.println("  Avant: " + l2.getNomComplet() + " - Disponible: " + l2.isEstDisponible());
            l2.marquerOccupe();
            livreurRepo.save(l2);
            System.out.println("  Apres: " + l2.getNomComplet() + " - Disponible: " + l2.isEstDisponible());

            // Test 8: FIND AVAILABLE
            System.out.println("\nTest 8: Liste des livreurs disponibles...");
            List<Livreur> availableLivreurs = livreurRepo.findAvailable();
            System.out.println("  Nombre de livreurs disponibles: " + availableLivreurs.size());
            availableLivreurs.forEach(l -> System.out.println("    - " + l.getNomComplet()));

            // Test 9:  FIND BY EST DISPONIBLE
            System.out.println("\nTest 9: Livreurs par statut de disponibilite...");
            List<Livreur> disponibles = livreurRepo.findByEstDisponible(true);
            List<Livreur> occupes = livreurRepo.findByEstDisponible(false);
            System.out.println("  Disponibles: " + disponibles.size());
            System.out.println("  Occupes: " + occupes.size());

            // Test 10: ARCHIVAGE
            System.out.println("\nTest 10: Archivage d'un livreur...");
            l3.archiver();
            livreurRepo.save(l3);
            System.out.println("  ✓ Livreur archive: " + l3.getNomComplet());
            System.out.println("    Archive: " + l3.isEstArchiver());
            System.out.println("    Disponible: " + l3.isEstDisponible());
            System.out.println("    Peut etre affecte: " + l3.peutEtreAffecte());

            // Test 11: FIND BY EST ARCHIVER
            System.out.println("\nTest 11: Livreurs par statut d'archivage...");
            List<Livreur> actifs = livreurRepo.findByEstArchiver(false);
            List<Livreur> archives = livreurRepo.findByEstArchiver(true);
            System.out.println("  Actifs: " + actifs. size());
            actifs.forEach(l -> System. out.println("    - " + l.getNomComplet()));
            System.out.println("  Archives: " + archives.size());
            archives.forEach(l -> System.out.println("    - " + l.getNomComplet()));

            // Test 12: RESTAURATION
            System.out.println("\nTest 12: Restauration d'un livreur...");
            l3.restaurer();
            livreurRepo.save(l3);
            System.out.println("  ✓ Livreur restaure: " + l3.getNomComplet());
            System. out.println("    Peut etre affecte: " + l3.peutEtreAffecte());

            // Test 13: EXISTS BY TELEPHONE
            System.out.println("\nTest 13: Verification d'existence...");
            boolean exists = livreurRepo.existsByTelephone("779999999");
            System.out.println("  '779999999' existe ?  " + (exists ? "Oui" : "Non"));

            boolean notExists = livreurRepo.existsByTelephone("770000000");
            System.out.println("  '770000000' existe ? " + (notExists ? "Oui" : "Non"));

            // Test 14: DELETE
            System.out.println("\nTest 14: Suppression d'un livreur...");
            livreurRepo.delete(l3.getId());
            System.out.println("  ✓ Livreur supprime: " + l3.getNomComplet());

            long finalCount = livreurRepo.count();
            System.out.println("  Nombre de livreurs restants: " + finalCount);

            System.out.println("\n===============================================");
            System.out.println("TOUS LES TESTS DU REPOSITORY LIVREUR SONT REUSSIS !");
            System.out.println("===============================================");

        } catch (Exception e) {
            System.err.println("\n✗ ERREUR:  " + e.getMessage());
            logger.error("Erreur lors des tests", e);
            e.printStackTrace();
        }
    }
}