package com.brasilburger.presentation.console.controllers;

import com.brasilburger.domain.entities.Zone;
import com.brasilburger.domain.services.IZoneService;
import com.brasilburger.factories.ServiceFactory;
import com.brasilburger.presentation.console.utils.ConsoleInput;
import com.brasilburger.presentation.console.utils.ConsoleOutput;
import com.brasilburger.presentation.console.utils.TableFormatter;
import com.brasilburger.presentation.validators.ZoneValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * Contrôleur pour la gestion des zones
 * Gère l'interaction utilisateur et la logique de présentation
 */
public class ZoneController {

    private static final Logger logger = LoggerFactory.getLogger(ZoneController.class);
    private final IZoneService zoneService;

    public ZoneController() {
        this.zoneService = ServiceFactory.createZoneService();
    }

    /**
     * Affiche le menu de gestion des zones
     */
    public void afficherMenu() {
        boolean continuer = true;

        while (continuer) {
            try {
                ConsoleOutput.titre("GESTION DES ZONES");

                System.out.println("1.Créer une zone");
                System.out.println("2.Lister toutes les zones");
                System.out.println("3.Lister les zones actives");
                System.out.println("4.Rechercher une zone");
                System.out.println("5.Modifier une zone");
                System.out.println("6.Archiver une zone");
                System.out.println("7.Restaurer une zone");
                System.out.println("8.Supprimer une zone");
                System.out.println("0.Retour au menu principal");

                int choix = ConsoleInput.lireChoixMenu(8);

                switch (choix) {
                    case 1:
                        creerZone();
                        break;
                    case 2:
                        listerToutesLesZones();
                        break;
                    case 3:
                        listerZonesActives();
                        break;
                    case 4:
                        rechercherZone();
                        break;
                    case 5:
                        modifierZone();
                        break;
                    case 6:
                        archiverZone();
                        break;
                    case 7:
                        restaurerZone();
                        break;
                    case 8:
                        supprimerZone();
                        break;
                    case 0:
                        continuer = false;
                        break;
                    default:
                        ConsoleOutput.erreur("Choix invalide");
                }

                if (continuer && choix != 0) {
                    ConsoleInput.attendreEntree();
                }

            } catch (Exception e) {
                ConsoleOutput.erreur("Erreur:  " + e.getMessage());
                logger.error("Erreur dans le menu zone", e);
                ConsoleInput.attendreEntree();
            }
        }
    }

    /**
     * Crée une nouvelle zone
     */
    private void creerZone() {
        ConsoleOutput.sousTitre("Création d'une zone");

        try {
            // Saisie du nom
            String nom = ConsoleInput.lireTexteNonVide("Nom de la zone:  ");

            // Validation du nom
            List<String> erreursNom = ZoneValidator.validerNom(nom);
            if (!erreursNom.isEmpty()) {
                ConsoleOutput.erreur("Nom invalide:");
                erreursNom.forEach(e -> System.out.println("  - " + e));
                return;
            }

            // Saisie du prix
            int prixLivraison = ConsoleInput.lireEntierPositif("Prix de livraison (FCFA): ");

            // Validation du prix
            List<String> erreursPrix = ZoneValidator.validerPrixLivraison(prixLivraison);
            if (!erreursPrix.isEmpty()) {
                ConsoleOutput.erreur("Prix invalide:");
                erreursPrix.forEach(e -> System.out.println("  - " + e));
                return;
            }

            // Confirmation
            ConsoleOutput.info("Zone à créer:");
            System.out.println("  Nom: " + nom);
            System.out.println("  Prix de livraison: " + prixLivraison + " FCFA");

            if (! ConsoleInput.lireConfirmation("Confirmer la création")) {
                ConsoleOutput.avertissement("Création annulée");
                return;
            }

            // Création
            Zone zone = zoneService.creerZone(nom, prixLivraison);

            ConsoleOutput.succes("Zone créée avec succès!");
            afficherDetailsZone(zone);

        } catch (Exception e) {
            ConsoleOutput.erreur("Erreur lors de la création: " + e.getMessage());
            logger.error("Erreur création zone", e);
        }
    }

    /**
     * Liste toutes les zones
     */
    private void listerToutesLesZones() {
        ConsoleOutput.sousTitre("Liste de toutes les zones");

        try {
            List<Zone> zones = zoneService.listerToutesLesZones();

            if (zones.isEmpty()) {
                ConsoleOutput.info("Aucune zone trouvée");
                return;
            }

            afficherTableauZones(zones);

        } catch (Exception e) {
            ConsoleOutput.erreur("Erreur lors du listage: " + e.getMessage());
            logger.error("Erreur listage zones", e);
        }
    }

    /**
     * Liste les zones actives
     */
    private void listerZonesActives() {
        ConsoleOutput.sousTitre("Liste des zones actives");

        try {
            List<Zone> zones = zoneService.listerZonesActives();

            if (zones.isEmpty()) {
                ConsoleOutput.info("Aucune zone active trouvée");
                return;
            }

            afficherTableauZones(zones);

        } catch (Exception e) {
            ConsoleOutput.erreur("Erreur lors du listage: " + e.getMessage());
            logger.error("Erreur listage zones actives", e);
        }
    }

    /**
     * Recherche une zone par nom ou ID
     */
    private void rechercherZone() {
        ConsoleOutput.sousTitre("Recherche de zone");

        try {
            System.out.println("1.Rechercher par ID");
            System.out.println("2.Rechercher par nom");

            int choix = ConsoleInput.lireEntier("Votre choix: ", 1, 2);

            Optional<Zone> zoneOpt;

            if (choix == 1) {
                long id = ConsoleInput.lireLong("ID de la zone: ");
                zoneOpt = zoneService.obtenirZoneParId(id);
            } else {
                String nom = ConsoleInput.lireTexteNonVide("Nom de la zone: ");
                zoneOpt = zoneService.obtenirZoneParNom(nom);
            }

            if (zoneOpt.isPresent()) {
                ConsoleOutput.succes("Zone trouvée:");
                afficherDetailsZone(zoneOpt.get());
            } else {
                ConsoleOutput.avertissement("Zone non trouvée");
            }

        } catch (Exception e) {
            ConsoleOutput.erreur("Erreur lors de la recherche: " + e.getMessage());
            logger.error("Erreur recherche zone", e);
        }
    }

    /**
     * Modifie une zone
     */
    private void modifierZone() {
        ConsoleOutput.sousTitre("Modification d'une zone");

        try {
            // Sélection de la zone
            long id = ConsoleInput.lireLong("ID de la zone à modifier: ");
            Optional<Zone> zoneOpt = zoneService.obtenirZoneParId(id);

            if (!zoneOpt.isPresent()) {
                ConsoleOutput.erreur("Zone non trouvée");
                return;
            }

            Zone zone = zoneOpt.get();
            ConsoleOutput.info("Zone actuelle:");
            afficherDetailsZone(zone);

            // Saisie des nouvelles valeurs
            System.out.println("\nLaissez vide pour conserver la valeur actuelle");

            String nouveauNom = ConsoleInput.lireTexteOptional("Nouveau nom [" + zone.getNom() + "]: ");
            if (! nouveauNom.isEmpty()) {
                List<String> erreurs = ZoneValidator.validerNom(nouveauNom);
                if (!erreurs.isEmpty()) {
                    ConsoleOutput.erreur("Nom invalide:");
                    erreurs.forEach(e -> System.out.println("  - " + e));
                    return;
                }
            }

            String prixStr = ConsoleInput.lireTexteOptional("Nouveau prix [" + zone.getPrixLivraison() + " FCFA]: ");
            Integer nouveauPrix = null;
            if (!prixStr.isEmpty()) {
                try {
                    nouveauPrix = Integer.parseInt(prixStr);
                    List<String> erreurs = ZoneValidator.validerPrixLivraison(nouveauPrix);
                    if (!erreurs.isEmpty()) {
                        ConsoleOutput.erreur("Prix invalide:");
                        erreurs.forEach(e -> System.out.println("  - " + e));
                        return;
                    }
                } catch (NumberFormatException e) {
                    ConsoleOutput.erreur("Prix invalide");
                    return;
                }
            }

            // Vérifier si des modifications ont été faites
            if (nouveauNom.isEmpty() && nouveauPrix == null) {
                ConsoleOutput.avertissement("Aucune modification effectuée");
                return;
            }

            // Confirmation
            if (!ConsoleInput.lireConfirmation("Confirmer les modifications")) {
                ConsoleOutput.avertissement("Modification annulée");
                return;
            }

            // Modification
            Zone zoneModifiee = zoneService.modifierZone(
                    id,
                    nouveauNom.isEmpty() ? null : nouveauNom,
                    nouveauPrix
            );

            ConsoleOutput.succes("Zone modifiée avec succès!");
            afficherDetailsZone(zoneModifiee);

        } catch (Exception e) {
            ConsoleOutput.erreur("Erreur lors de la modification: " + e.getMessage());
            logger.error("Erreur modification zone", e);
        }
    }

    /**
     * Archive une zone
     */
    private void archiverZone() {
        ConsoleOutput.sousTitre("Archivage d'une zone");

        try {
            long id = ConsoleInput.lireLong("ID de la zone à archiver: ");
            Optional<Zone> zoneOpt = zoneService.obtenirZoneParId(id);

            if (!zoneOpt.isPresent()) {
                ConsoleOutput.erreur("Zone non trouvée");
                return;
            }

            Zone zone = zoneOpt.get();

            if (zone.isEstArchiver()) {
                ConsoleOutput.avertissement("Cette zone est déjà archivée");
                return;
            }

            ConsoleOutput.info("Zone à archiver:");
            afficherDetailsZone(zone);

            if (!ConsoleInput.lireConfirmation("Confirmer l'archivage")) {
                ConsoleOutput.avertissement("Archivage annulé");
                return;
            }

            zoneService.archiverZone(id);
            ConsoleOutput.succes("Zone archivée avec succès");

        } catch (Exception e) {
            ConsoleOutput.erreur("Erreur lors de l'archivage: " + e.getMessage());
            logger.error("Erreur archivage zone", e);
        }
    }

    /**
     * Restaure une zone archivée
     */
    private void restaurerZone() {
        ConsoleOutput.sousTitre("Restauration d'une zone");

        try {
            // Afficher les zones archivées
            List<Zone> zonesArchivees = zoneService.listerZonesArchivees();

            if (zonesArchivees.isEmpty()) {
                ConsoleOutput.info("Aucune zone archivée à restaurer");
                return;
            }

            ConsoleOutput.info("Zones archivées:");
            afficherTableauZones(zonesArchivees);

            long id = ConsoleInput.lireLong("\nID de la zone à restaurer: ");
            Optional<Zone> zoneOpt = zoneService.obtenirZoneParId(id);

            if (!zoneOpt.isPresent()) {
                ConsoleOutput.erreur("Zone non trouvée");
                return;
            }

            Zone zone = zoneOpt.get();

            if (!zone.isEstArchiver()) {
                ConsoleOutput.avertissement("Cette zone n'est pas archivée");
                return;
            }

            if (!ConsoleInput.lireConfirmation("Confirmer la restauration")) {
                ConsoleOutput.avertissement("Restauration annulée");
                return;
            }

            zoneService.restaurerZone(id);
            ConsoleOutput.succes("Zone restaurée avec succès");

        } catch (Exception e) {
            ConsoleOutput.erreur("Erreur lors de la restauration: " + e.getMessage());
            logger.error("Erreur restauration zone", e);
        }
    }

    /**
     * Supprime une zone
     */
    private void supprimerZone() {
        ConsoleOutput.sousTitre("Suppression d'une zone");

        try {
            long id = ConsoleInput.lireLong("ID de la zone à supprimer: ");
            Optional<Zone> zoneOpt = zoneService.obtenirZoneParId(id);

            if (!zoneOpt.isPresent()) {
                ConsoleOutput.erreur("Zone non trouvée");
                return;
            }

            Zone zone = zoneOpt.get();

            ConsoleOutput.avertissement("Zone à supprimer:");
            afficherDetailsZone(zone);

            ConsoleOutput.avertissement("ATTENTION: Cette action est irréversible!");

            if (!ConsoleInput.lireConfirmation("Confirmer la suppression")) {
                ConsoleOutput.avertissement("Suppression annulée");
                return;
            }

            // Double confirmation
            if (!ConsoleInput.lireConfirmation("Êtes-vous vraiment sûr")) {
                ConsoleOutput.avertissement("Suppression annulée");
                return;
            }

            zoneService.supprimerZone(id);
            ConsoleOutput.succes("Zone supprimée avec succès");

        } catch (Exception e) {
            ConsoleOutput.erreur("Erreur lors de la suppression: " + e.getMessage());
            logger.error("Erreur suppression zone", e);
        }
    }

    /**
     * Affiche les détails d'une zone
     */
    private void afficherDetailsZone(Zone zone) {
        ConsoleOutput.separateur();
        System.out.println("  ID:                " + zone.getId());
        System.out.println("  Nom:              " + zone.getNom());
        System.out.println("  Prix livraison:   " + zone.getPrixLivraison() + " FCFA");
        System.out.println("  Statut:            " + (zone.isEstArchiver() ? "Archivée" : "Active"));
        ConsoleOutput.separateur();
    }

    /**
     * Affiche un tableau de zones
     */
    private void afficherTableauZones(List<Zone> zones) {
        TableFormatter table = new TableFormatter(
                "ID",
                "Nom",
                "Prix Livraison",
                "Statut"
        );

        for (Zone zone : zones) {
            table.ajouterLigne(
                    String.valueOf(zone.getId()),
                    zone.getNom(),
                    zone.getPrixLivraison() + " FCFA",
                    zone.isEstArchiver() ? "Archivée" : "Active"
            );
        }

        table.afficher();
    }
}