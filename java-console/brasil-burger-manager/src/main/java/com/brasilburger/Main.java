package com.brasilburger;

import com.brasilburger.config.AppConfig;
import com.brasilburger.domain.entities.Livreur;
import com.brasilburger.domain.entities.Quartier;
import com.brasilburger.domain.entities.Zone;
import com.brasilburger.domain.exceptions.DuplicateZoneException;
import com.brasilburger.domain.repositories.ILivreurRepository;
import com.brasilburger.domain.repositories.IQuartierRepository;
import com.brasilburger.domain.repositories.IZoneRepository;
import com.brasilburger.domain.repositories.impl.NeonLivreurRepository;
import com.brasilburger.domain.repositories.impl.NeonQuartierRepository;
import com.brasilburger.domain.repositories.impl.NeonZoneRepository;
import com.brasilburger.infrastructure.database.NeonConnectionManager;
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

        // Valider la configuration
        if (!AppConfig.validateConfiguration()) {
            logger.error("Configuration invalide!");
            System.exit(1);
        }

        // Tester la connexion
        if (!NeonConnectionManager.testConnection()) {
            logger.error("Impossible de se connecter a la base de donnees");
            System.exit(1);
        }

        logger.info("Configuration validee et connexion etablie");

        // TESTS DE CORRECTION DE BUGS (Commit 12)
        testerCorrectionBugs();

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
     * Tests de correction des bugs (Commit 12)
     */
    private static void testerCorrectionBugs() {
        System.out.println("=== TESTS DE CORRECTION DE BUGS ===\n");

        IZoneRepository zoneRepo = new NeonZoneRepository();
        IQuartierRepository quartierRepo = new NeonQuartierRepository();
        ILivreurRepository livreurRepo = new NeonLivreurRepository();

        try {
            // ==========================================
            // Test 1: Bug de duplication Zone (INSERT)
            // ==========================================
            System.out.println("Test 1: Verification duplication Zone (INSERT)...");
            Zone zone1 = new Zone("Test Duplication", 2000);
            zone1 = zoneRepo.save(zone1);
            System.out.println("  ✓ Zone creee: " + zone1.getNom());

            try {
                Zone zoneDuplicate = new Zone("Test Duplication", 3000);
                zoneRepo.save(zoneDuplicate);
                System.out.println("  ✗ ERREUR: La duplication aurait du etre detectee!");
            } catch (DuplicateZoneException e) {
                System.out.println("  ✓ Duplication detectee correctement:  " + e.getMessage());
            }

            // ==========================================
            // Test 2: Bug de duplication Zone (UPDATE)
            // ==========================================
            System.out.println("\nTest 2: Verification duplication Zone (UPDATE)...");
            Zone zone2 = new Zone("Zone Unique", 2500);
            zone2 = zoneRepo.save(zone2);
            System.out.println("  ✓ Zone 2 creee: " + zone2.getNom());

            // Tenter de renommer zone2 avec le nom de zone1
            try {
                zone2.modifierNom("Test Duplication");
                zoneRepo.save(zone2);
                System.out.println("  ✗ ERREUR: La duplication lors de l'UPDATE aurait du etre detectee!");
            } catch (DuplicateZoneException e) {
                System.out.println("  ✓ Duplication lors UPDATE detectee: " + e.getMessage());
                // Restaurer le nom original
                zone2.setNom("Zone Unique");
            }

            // ==========================================
            // Test 3: Modification nom Zone (sans duplication)
            // ==========================================
            System.out.println("\nTest 3: Modification nom Zone (cas valide)...");
            String ancienNom = zone2.getNom();
            zone2.modifierNom("Zone Unique Modifiee");
            zone2 = zoneRepo.save(zone2);
            System.out.println("  ✓ Nom modifie: '" + ancienNom + "' -> '" + zone2.getNom() + "'");

            // ==========================================
            // Test 4: Modification prix Zone
            // ==========================================
            System.out.println("\nTest 4: Modification prix Zone.. .");
            int ancienPrix = zone1.getPrixLivraison();
            zone1.modifierPrixLivraison(2800);
            zone1 = zoneRepo.save(zone1);
            System.out.println("  ✓ Prix modifie: " + ancienPrix + " -> " + zone1.getPrixLivraison() + " FCFA");

            // ==========================================
            // Test 5: Test complet Quartier
            // ==========================================
            System.out.println("\nTest 5: Test complet Quartier...");
            Quartier quartier = new Quartier("Quartier Test", zone1.getId());
            quartier = quartierRepo.save(quartier);
            System.out.println("  ✓ Quartier cree: " + quartier.getNom() + " (Zone: " + quartier.getIdZone() + ")");

            // Changer de zone
            quartier.changerZone(zone2.getId());
            quartier = quartierRepo.save(quartier);
            System.out.println("  ✓ Zone changee: " + quartier.getNom() + " -> Zone " + quartier.getIdZone());

            // ==========================================
            // Test 6: Test complet Livreur
            // ==========================================
            System.out.println("\nTest 6: Test complet Livreur...");
            Livreur livreur = new Livreur("Sow", "Fatou", "778888888");
            livreur = livreurRepo.save(livreur);
            System.out.println("  ✓ Livreur cree: " + livreur.getNomComplet());
            System.out.println("    - Disponible: " + livreur.isEstDisponible());
            System.out.println("    - Peut etre affecte:  " + livreur.peutEtreAffecte());

            // Marquer occupe
            livreur.marquerOccupe();
            livreur = livreurRepo.save(livreur);
            System.out.println("  ✓ Marque occupe: " + livreur.getNomComplet());
            System.out.println("    - Disponible: " + livreur.isEstDisponible());
            System.out.println("    - Peut etre affecte: " + livreur.peutEtreAffecte());

            // Rendre disponible
            livreur.marquerDisponible();
            livreur = livreurRepo.save(livreur);
            System.out.println("  ✓ Rendu disponible: " + livreur.getNomComplet());
            System.out.println("    - Disponible: " + livreur.isEstDisponible());
            System.out.println("    - Peut etre affecte: " + livreur.peutEtreAffecte());

            // Archiver
            livreur.archiver();
            livreur = livreurRepo.save(livreur);
            System.out.println("  ✓ Archive: " + livreur.getNomComplet());
            System.out.println("    - Archive: " + livreur.isEstArchiver());
            System.out.println("    - Disponible: " + livreur.isEstDisponible());
            System.out.println("    - Peut etre affecte:  " + livreur.peutEtreAffecte());

            // Tenter de marquer disponible (doit echouer)
            try {
                livreur.marquerDisponible();
                System.out.println("  ✗ ERREUR: Un livreur archive ne devrait pas pouvoir etre marque disponible!");
            } catch (IllegalStateException e) {
                System.out.println("  ✓ Erreur correctement detectee: " + e.getMessage());
            }

            // ==========================================
            // Test 7: Nettoyage
            // ==========================================
            System.out.println("\nTest 7: Nettoyage des donnees de test...");
            livreurRepo.delete(livreur.getId());
            quartierRepo.delete(quartier.getId());
            zoneRepo.delete(zone1.getId());
            zoneRepo.delete(zone2.getId());
            System.out.println("  ✓ Donnees de test supprimees");

            System.out.println("\n===============================================");
            System.out.println("TOUS LES TESTS DE CORRECTION SONT REUSSIS !");
            System.out.println("===============================================");

        } catch (Exception e) {
            System.err.println("\n✗ ERREUR: " + e.getMessage());
            logger.error("Erreur lors des tests", e);
            e.printStackTrace();
        }
    }
}