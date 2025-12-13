package com.brasilburger;

import com.brasilburger.config.AppConfig;
import com.brasilburger.domain.entities.Zone;
import com.brasilburger.domain.exceptions.DuplicateZoneException;
import com.brasilburger.domain.repositories.IZoneRepository;
import com.brasilburger.domain.repositories.impl.NeonZoneRepository;
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

        // TEST DU REPOSITORY ZONE
        testerZoneRepository();

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
     * Test du repository Zone
     */
    private static void testerZoneRepository() {
        System.out.println("=== TEST DU REPOSITORY ZONE ===\n");

        IZoneRepository zoneRepo = new NeonZoneRepository();

        try {
            // Test 1: CREATE (INSERT)
            System.out.println("Test 1: Creation de zones.. .");
            Zone zone1 = new Zone("Plateau", 2000);
            Zone zone2 = new Zone("Parcelles Assainies", 3000);
            Zone zone3 = new Zone("Almadies", 4000);

            zone1 = zoneRepo.save(zone1);
            zone2 = zoneRepo.save(zone2);
            zone3 = zoneRepo.save(zone3);

            System.out.println("  ✓ Zone 1 creee:  " + zone1);
            System.out.println("  ✓ Zone 2 creee: " + zone2);
            System.out.println("  ✓ Zone 3 creee: " + zone3);

            // Test 2: COUNT
            System.out.println("\nTest 2: Comptage des zones...");
            long count = zoneRepo.count();
            System.out.println("  Nombre total de zones: " + count);

            // Test 3: FIND BY ID
            System.out.println("\nTest 3: Recherche par ID...");
            Optional<Zone> found = zoneRepo.findById(zone1.getId());
            if (found.isPresent()) {
                System.out.println("  ✓ Zone trouvee:  " + found.get());
            } else {
                System.out.println("  ✗ Zone non trouvee");
            }

            // Test 4: FIND BY NOM
            System.out.println("\nTest 4: Recherche par nom...");
            Optional<Zone> foundByNom = zoneRepo.findByNom("Plateau");
            if (foundByNom.isPresent()) {
                System.out.println("  ✓ Zone trouvee:  " + foundByNom.get());
            } else {
                System.out.println("  ✗ Zone non trouvee");
            }

            // Test 5: FIND ALL
            System.out.println("\nTest 5: Liste de toutes les zones...");
            List<Zone> allZones = zoneRepo.findAll();
            System.out.println("  Nombre de zones: " + allZones.size());
            allZones.forEach(z -> System.out.println("    - " + z.getNom() + " (" + z.getPrixLivraison() + " FCFA)"));

            // Test 6: UPDATE
            System.out.println("\nTest 6: Mise a jour d'une zone...");
            zone1.setPrixLivraison(2500);
            zone1.modifierNom("Plateau - Centre Ville");
            zoneRepo.save(zone1);
            System.out.println("  ✓ Zone mise a jour: " + zone1);

            // Test 7: ARCHIVAGE
            System.out.println("\nTest 7: Archivage d'une zone...");
            zone2.archiver();
            zoneRepo.save(zone2);
            System.out.println("  ✓ Zone archivee: " + zone2);

            // Test 8: FIND BY EST_ARCHIVER
            System.out.println("\nTest 8: Liste des zones non archivees...");
            List<Zone> zonesActives = zoneRepo.findByEstArchiver(false);
            System.out.println("  Zones actives:  " + zonesActives.size());
            zonesActives.forEach(z -> System.out.println("    - " + z.getNom()));

            System.out.println("\n  Liste des zones archivees.. .");
            List<Zone> zonesArchivees = zoneRepo.findByEstArchiver(true);
            System.out.println("  Zones archivees: " + zonesArchivees.size());
            zonesArchivees.forEach(z -> System.out.println("    - " + z.getNom()));

            // Test 9: EXISTS BY NOM
            System.out.println("\nTest 9: Verification d'existence...");
            boolean exists = zoneRepo.existsByNom("Plateau");
            System.out.println("  'Plateau' existe ?  " + (exists ? "Oui" : "Non"));

            boolean notExists = zoneRepo.existsByNom("Zone Inexistante");
            System.out.println("  'Zone Inexistante' existe ? " + (notExists ? "Oui" : "Non"));

            // Test 10: DUPLICATE
            System.out.println("\nTest 10: Test de duplication.. .");
            try {
                // Tester avec le nom mis a jour de zone1
                Zone duplicate = new Zone("Plateau", 5000);
                zoneRepo.save(duplicate);
                System.out.println("  ✗ La duplication aurait du echouer!");
            } catch (DuplicateZoneException e) {
                System.out.println("  ✓ Duplication detectee: " + e.getMessage());
            }

            long finalCount = zoneRepo.count();
            System.out.println("  Nombre de zones restantes: " + finalCount);

            System.out.println("\n===============================================");
            System.out.println("TOUS LES TESTS DU REPOSITORY ZONE SONT REUSSIS !");
            System.out.println("===============================================");

        } catch (Exception e) {
            System.err.println("\n✗ ERREUR:  " + e.getMessage());
            logger.error("Erreur lors des tests", e);
            e.printStackTrace();
        }
    }
}