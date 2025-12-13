package com.brasilburger;

import com.brasilburger.config.AppConfig;
import com. brasilburger.domain.entities.Quartier;
import com.brasilburger.domain. entities.Zone;
import com.brasilburger.domain.repositories. IQuartierRepository;
import com.brasilburger.domain.repositories.IZoneRepository;
import com. brasilburger.domain.repositories.impl.NeonQuartierRepository;
import com. brasilburger.domain.repositories.impl.NeonZoneRepository;
import com.brasilburger.infrastructure.database. NeonConnectionManager;
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

        // TEST DU REPOSITORY QUARTIER (Commit 10)
        testerQuartierRepository();

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
     * Test du repository Quartier (Commit 10)
     */
    private static void testerQuartierRepository() {
        System.out.println("=== TEST DU REPOSITORY QUARTIER ===\n");

        IZoneRepository zoneRepo = new NeonZoneRepository();
        IQuartierRepository quartierRepo = new NeonQuartierRepository();

        try {
            // Preparation:  Creer des zones de test
            System.out.println("Preparation:  Creation de zones de test...");
            Zone zonePlateau = new Zone("Plateau Test", 2000);
            Zone zoneParcelles = new Zone("Parcelles Test", 3000);

            zonePlateau = zoneRepo.save(zonePlateau);
            zoneParcelles = zoneRepo.save(zoneParcelles);

            System.out.println("  ✓ Zone 1:  " + zonePlateau.getNom() + " (ID=" + zonePlateau.getId() + ")");
            System.out.println("  ✓ Zone 2: " + zoneParcelles.getNom() + " (ID=" + zoneParcelles.getId() + ")");

            // Test 1: CREATE (INSERT)
            System.out.println("\nTest 1: Creation de quartiers...");
            Quartier q1 = new Quartier("Mermoz", zonePlateau. getId());
            Quartier q2 = new Quartier("Point E", zonePlateau.getId());
            Quartier q3 = new Quartier("Unite 10", zoneParcelles.getId());
            Quartier q4 = new Quartier("Unite 15", zoneParcelles.getId());

            q1 = quartierRepo.save(q1);
            q2 = quartierRepo.save(q2);
            q3 = quartierRepo.save(q3);
            q4 = quartierRepo.save(q4);

            System.out.println("  ✓ Quartier 1: " + q1);
            System.out.println("  ✓ Quartier 2: " + q2);
            System.out.println("  ✓ Quartier 3: " + q3);
            System.out.println("  ✓ Quartier 4: " + q4);

            // Test 2: COUNT
            System.out.println("\nTest 2: Comptage des quartiers...");
            long count = quartierRepo.count();
            System.out.println("  Nombre total de quartiers: " + count);

            // Test 3: FIND BY ID
            System. out.println("\nTest 3: Recherche par ID...");
            Optional<Quartier> found = quartierRepo.findById(q1.getId());
            if (found.isPresent()) {
                System.out.println("  ✓ Quartier trouve: " + found.get());
            } else {
                System. out.println("  ✗ Quartier non trouve");
            }

            // Test 4: FIND BY NOM
            System.out.println("\nTest 4: Recherche par nom...");
            Optional<Quartier> foundByNom = quartierRepo. findByNom("Mermoz");
            if (foundByNom.isPresent()) {
                System.out.println("  ✓ Quartier trouve: " + foundByNom.get());
            } else {
                System.out.println("  ✗ Quartier non trouve");
            }

            // Test 5: FIND ALL
            System.out.println("\nTest 5: Liste de tous les quartiers...");
            List<Quartier> allQuartiers = quartierRepo.findAll();
            System.out.println("  Nombre de quartiers: " + allQuartiers.size());
            allQuartiers.forEach(q -> System.out.println("    - " + q.getNom() + " (Zone ID:  " + q.getIdZone() + ")"));

            // Test 6: FIND BY ZONE ID
            System.out.println("\nTest 6: Quartiers de la zone Plateau.. .");
            List<Quartier> quartiersPlateau = quartierRepo.findByZoneId(zonePlateau.getId());
            System.out.println("  Nombre de quartiers:  " + quartiersPlateau. size());
            quartiersPlateau.forEach(q -> System. out.println("    - " + q.getNom()));

            System.out.println("\n  Quartiers de la zone Parcelles.. .");
            List<Quartier> quartiersParcelles = quartierRepo.findByZoneId(zoneParcelles.getId());
            System.out.println("  Nombre de quartiers: " + quartiersParcelles.size());
            quartiersParcelles.forEach(q -> System.out.println("    - " + q.getNom()));

            // Test 7: COUNT BY ZONE ID
            System. out.println("\nTest 7: Comptage par zone...");
            long countPlateau = quartierRepo.countByZoneId(zonePlateau.getId());
            long countParcelles = quartierRepo.countByZoneId(zoneParcelles.getId());
            System.out.println("  Quartiers dans Plateau: " + countPlateau);
            System.out.println("  Quartiers dans Parcelles: " + countParcelles);

            // Test 8: UPDATE
            System.out.println("\nTest 8: Mise a jour d'un quartier...");
            q1.setNom("Mermoz Pyrotechnie");
            quartierRepo. save(q1);
            System.out.println("  ✓ Quartier mis a jour: " + q1);

            // Test 9: CHANGER DE ZONE
            System.out. println("\nTest 9: Changement de zone...");
            System.out.println("  Avant: " + q2.getNom() + " appartient a la zone " + q2.getIdZone());
            q2.changerZone(zoneParcelles.getId());
            quartierRepo.save(q2);
            System.out.println("  Apres: " + q2.getNom() + " appartient a la zone " + q2.getIdZone());

            // Test 10: EXISTS BY NOM
            System.out.println("\nTest 10: Verification d'existence...");
            boolean exists = quartierRepo. existsByNom("Mermoz Pyrotechnie");
            System.out.println("  'Mermoz Pyrotechnie' existe ?  " + (exists ? "Oui" :  "Non"));

            boolean notExists = quartierRepo.existsByNom("Quartier Inexistant");
            System.out.println("  'Quartier Inexistant' existe ? " + (notExists ? "Oui" : "Non"));

            // Test 11: DELETE
            System.out.println("\nTest 11: Suppression d'un quartier...");
            quartierRepo.delete(q4.getId());
            System.out.println("  ✓ Quartier supprime: " + q4.getNom());

            long finalCount = quartierRepo.count();
            System.out.println("  Nombre de quartiers restants: " + finalCount);

            System.out.println("\n===============================================");
            System.out.println("TOUS LES TESTS DU REPOSITORY QUARTIER SONT REUSSIS !");
            System. out.println("===============================================");

        } catch (Exception e) {
            System.err.println("\n✗ ERREUR: " + e.getMessage());
            logger.error("Erreur lors des tests", e);
            e.printStackTrace();
        }
    }
}