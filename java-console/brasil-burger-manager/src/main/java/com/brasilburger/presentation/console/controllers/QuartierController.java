package com.brasilburger.presentation.console.controllers;

import com.brasilburger.domain.entities.Quartier;
import com.brasilburger.domain.entities.Zone;
import com.brasilburger.domain.services.IQuartierService;
import com.brasilburger.domain.services.IZoneService;
import com.brasilburger.factories.ServiceFactory;
import com.brasilburger.presentation.console.utils.ConsoleInput;
import com.brasilburger.presentation.console.utils.ConsoleOutput;
import com.brasilburger.presentation.console.utils.TableFormatter;
import com.brasilburger.presentation.validators.QuartierValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * Contrôleur pour la gestion des quartiers
 * Gère l'interaction utilisateur et la logique de présentation
 */
public class QuartierController {

    private static final Logger logger = LoggerFactory.getLogger(QuartierController.class);
    private final IQuartierService quartierService;
    private final IZoneService zoneService;

    public QuartierController() {
        this.quartierService = ServiceFactory.createQuartierService();
        this.zoneService = ServiceFactory.createZoneService();
    }

    /**
     * Affiche le menu de gestion des quartiers
     */
    public void afficherMenu() {
        boolean continuer = true;

        while (continuer) {
            try {
                ConsoleOutput.titre("GESTION DES QUARTIERS");

                System.out.println("1.Créer un quartier");
                System.out.println("2.Lister tous les quartiers");
                System.out.println("3.Lister les quartiers par zone");
                System.out.println("4.Rechercher un quartier");
                System.out.println("5.Modifier un quartier");
                System.out.println("6.Changer la zone d'un quartier");
                System.out.println("7.Supprimer un quartier");
                System.out.println("0.Retour au menu principal");

                int choix = ConsoleInput.lireChoixMenu(7);

                switch (choix) {
                    case 1:
                        creerQuartier();
                        break;
                    case 2:
                        listerTousLesQuartiers();
                        break;
                    case 3:
                        listerQuartiersParZone();
                        break;
                    case 4:
                        rechercherQuartier();
                        break;
                    case 5:
                        modifierQuartier();
                        break;
                    case 6:
                        changerZoneQuartier();
                        break;
                    case 7:
                        supprimerQuartier();
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
                logger.error("Erreur dans le menu quartier", e);
                ConsoleInput.attendreEntree();
            }
        }
    }

    /**
     * Crée un nouveau quartier
     */
    private void creerQuartier() {
        ConsoleOutput.sousTitre("Création d'un quartier");

        try {
            // Afficher les zones disponibles
            List<Zone> zonesActives = zoneService.listerZonesActives();

            if (zonesActives.isEmpty()) {
                ConsoleOutput.erreur("Aucune zone active disponible.  Créez d'abord une zone.");
                return;
            }

            ConsoleOutput.info("Zones disponibles:");
            afficherTableauZones(zonesActives);

            // Saisie du nom
            String nom = ConsoleInput.lireTexteNonVide("Nom du quartier: ");

            // Validation du nom
            List<String> erreursNom = QuartierValidator.validerNom(nom);
            if (!erreursNom.isEmpty()) {
                ConsoleOutput.erreur("Nom invalide:");
                erreursNom.forEach(e -> System.out.println("  - " + e));
                return;
            }

            // Saisie de la zone
            long zoneId = ConsoleInput.lireLong("ID de la zone: ");

            // Vérifier que la zone existe
            Optional<Zone> zoneOpt = zoneService.obtenirZoneParId(zoneId);
            if (!zoneOpt.isPresent()) {
                ConsoleOutput.erreur("Zone non trouvée avec l'ID: " + zoneId);
                return;
            }

            Zone zone = zoneOpt.get();

            // Confirmation
            ConsoleOutput.info("Quartier à créer:");
            System.out.println("  Nom: " + nom);
            System.out.println("  Zone: " + zone.getNom() + " (" + zone.getPrixLivraison() + " FCFA)");

            if (!ConsoleInput.lireConfirmation("Confirmer la création")) {
                ConsoleOutput.avertissement("Création annulée");
                return;
            }

            // Création
            Quartier quartier = quartierService.creerQuartier(nom, zoneId);

            ConsoleOutput.succes("Quartier créé avec succès!");
            afficherDetailsQuartier(quartier);

        } catch (Exception e) {
            ConsoleOutput.erreur("Erreur lors de la création: " + e.getMessage());
            logger.error("Erreur création quartier", e);
        }
    }

    /**
     * Liste tous les quartiers
     */
    private void listerTousLesQuartiers() {
        ConsoleOutput.sousTitre("Liste de tous les quartiers");

        try {
            List<Quartier> quartiers = quartierService.listerTousLesQuartiers();

            if (quartiers.isEmpty()) {
                ConsoleOutput.info("Aucun quartier trouvé");
                return;
            }

            afficherTableauQuartiers(quartiers);

        } catch (Exception e) {
            ConsoleOutput.erreur("Erreur lors du listage: " + e.getMessage());
            logger.error("Erreur listage quartiers", e);
        }
    }

    /**
     * Liste les quartiers par zone
     */
    private void listerQuartiersParZone() {
        ConsoleOutput.sousTitre("Liste des quartiers par zone");

        try {
            // Afficher les zones
            List<Zone> zones = zoneService.listerToutesLesZones();

            if (zones.isEmpty()) {
                ConsoleOutput.info("Aucune zone disponible");
                return;
            }

            ConsoleOutput.info("Zones disponibles:");
            afficherTableauZones(zones);

            long zoneId = ConsoleInput.lireLong("\nID de la zone: ");

            // Vérifier que la zone existe
            Optional<Zone> zoneOpt = zoneService.obtenirZoneParId(zoneId);
            if (!zoneOpt.isPresent()) {
                ConsoleOutput.erreur("Zone non trouvée");
                return;
            }

            Zone zone = zoneOpt.get();
            List<Quartier> quartiers = quartierService.listerQuartiersParZone(zoneId);

            if (quartiers.isEmpty()) {
                ConsoleOutput.info("Aucun quartier dans la zone:  " + zone.getNom());
                return;
            }

            ConsoleOutput.info("Quartiers de la zone:  " + zone.getNom());
            afficherTableauQuartiers(quartiers);

            // Afficher le comptage
            long count = quartierService.compterQuartiersParZone(zoneId);
            System.out.println("\nTotal:  " + count + " quartier(s)");

        } catch (Exception e) {
            ConsoleOutput.erreur("Erreur lors du listage: " + e.getMessage());
            logger.error("Erreur listage quartiers par zone", e);
        }
    }

    /**
     * Recherche un quartier par nom ou ID
     */
    private void rechercherQuartier() {
        ConsoleOutput.sousTitre("Recherche de quartier");

        try {
            System.out.println("1.Rechercher par ID");
            System.out.println("2.Rechercher par nom");

            int choix = ConsoleInput.lireEntier("Votre choix: ", 1, 2);

            Optional<Quartier> quartierOpt;

            if (choix == 1) {
                long id = ConsoleInput.lireLong("ID du quartier: ");
                quartierOpt = quartierService.obtenirQuartierParId(id);
            } else {
                String nom = ConsoleInput.lireTexteNonVide("Nom du quartier: ");
                quartierOpt = quartierService.obtenirQuartierParNom(nom);
            }

            if (quartierOpt.isPresent()) {
                ConsoleOutput.succes("Quartier trouvé:");
                afficherDetailsQuartier(quartierOpt.get());
            } else {
                ConsoleOutput.avertissement("Quartier non trouvé");
            }

        } catch (Exception e) {
            ConsoleOutput.erreur("Erreur lors de la recherche: " + e.getMessage());
            logger.error("Erreur recherche quartier", e);
        }
    }

    /**
     * Modifie un quartier
     */
    private void modifierQuartier() {
        ConsoleOutput.sousTitre("Modification d'un quartier");

        try {
            // Sélection du quartier
            long id = ConsoleInput.lireLong("ID du quartier à modifier: ");
            Optional<Quartier> quartierOpt = quartierService.obtenirQuartierParId(id);

            if (!quartierOpt.isPresent()) {
                ConsoleOutput.erreur("Quartier non trouvé");
                return;
            }

            Quartier quartier = quartierOpt.get();
            ConsoleOutput.info("Quartier actuel:");
            afficherDetailsQuartier(quartier);

            // Saisie du nouveau nom
            System.out.println("\nLaissez vide pour conserver la valeur actuelle");
            String nouveauNom = ConsoleInput.lireTexteOptional("Nouveau nom [" + quartier.getNom() + "]: ");

            if (! nouveauNom.isEmpty()) {
                List<String> erreurs = QuartierValidator.validerNom(nouveauNom);
                if (!erreurs.isEmpty()) {
                    ConsoleOutput.erreur("Nom invalide:");
                    erreurs.forEach(e -> System.out.println("  - " + e));
                    return;
                }
            }

            // Vérifier si une modification a été faite
            if (nouveauNom.isEmpty()) {
                ConsoleOutput.avertissement("Aucune modification effectuée");
                return;
            }

            // Confirmation
            if (!ConsoleInput.lireConfirmation("Confirmer la modification")) {
                ConsoleOutput.avertissement("Modification annulée");
                return;
            }

            // Modification
            Quartier quartierModifie = quartierService.modifierQuartier(id, nouveauNom);

            ConsoleOutput.succes("Quartier modifié avec succès!");
            afficherDetailsQuartier(quartierModifie);

        } catch (Exception e) {
            ConsoleOutput.erreur("Erreur lors de la modification: " + e.getMessage());
            logger.error("Erreur modification quartier", e);
        }
    }

    /**
     * Change la zone d'un quartier
     */
    private void changerZoneQuartier() {
        ConsoleOutput.sousTitre("Changement de zone d'un quartier");

        try {
            // Sélection du quartier
            long quartierId = ConsoleInput.lireLong("ID du quartier:  ");
            Optional<Quartier> quartierOpt = quartierService.obtenirQuartierParId(quartierId);

            if (!quartierOpt.isPresent()) {
                ConsoleOutput.erreur("Quartier non trouvé");
                return;
            }

            Quartier quartier = quartierOpt.get();
            ConsoleOutput.info("Quartier sélectionné:");
            afficherDetailsQuartier(quartier);

            // Afficher les zones disponibles
            List<Zone> zones = zoneService.listerZonesActives();

            if (zones.isEmpty()) {
                ConsoleOutput.erreur("Aucune zone active disponible");
                return;
            }

            ConsoleOutput.info("Zones disponibles:");
            afficherTableauZones(zones);

            // Sélection de la nouvelle zone
            long nouvelleZoneId = ConsoleInput.lireLong("\nNouvelle zone ID: ");

            // Vérifier que la zone existe
            Optional<Zone> zoneOpt = zoneService.obtenirZoneParId(nouvelleZoneId);
            if (!zoneOpt.isPresent()) {
                ConsoleOutput.erreur("Zone non trouvée");
                return;
            }

            Zone nouvelleZone = zoneOpt.get();

            // Vérifier si c'est la même zone
            if (quartier.getIdZone().equals(nouvelleZoneId)) {
                ConsoleOutput.avertissement("Le quartier est déjà dans cette zone");
                return;
            }

            // Confirmation
            ConsoleOutput.info("Changement:");
            System.out.println("  Quartier: " + quartier.getNom());
            Optional<Zone> ancienneZoneOpt = zoneService.obtenirZoneParId(quartier.getIdZone());
            if (ancienneZoneOpt.isPresent()) {
                System.out.println("  Ancienne zone: " + ancienneZoneOpt.get().getNom());
            }
            System.out.println("  Nouvelle zone: " + nouvelleZone.getNom());

            if (!ConsoleInput.lireConfirmation("Confirmer le changement")) {
                ConsoleOutput.avertissement("Changement annulé");
                return;
            }

            // Changement
            quartierService.changerZoneQuartier(quartierId, nouvelleZoneId);

            ConsoleOutput.succes("Zone changée avec succès!");

        } catch (Exception e) {
            ConsoleOutput.erreur("Erreur lors du changement de zone: " + e.getMessage());
            logger.error("Erreur changement zone quartier", e);
        }
    }

    /**
     * Supprime un quartier
     */
    private void supprimerQuartier() {
        ConsoleOutput.sousTitre("Suppression d'un quartier");

        try {
            long id = ConsoleInput.lireLong("ID du quartier à supprimer: ");
            Optional<Quartier> quartierOpt = quartierService.obtenirQuartierParId(id);

            if (!quartierOpt.isPresent()) {
                ConsoleOutput.erreur("Quartier non trouvé");
                return;
            }

            Quartier quartier = quartierOpt.get();

            ConsoleOutput.avertissement("Quartier à supprimer:");
            afficherDetailsQuartier(quartier);

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

            quartierService.supprimerQuartier(id);
            ConsoleOutput.succes("Quartier supprimé avec succès");

        } catch (Exception e) {
            ConsoleOutput.erreur("Erreur lors de la suppression: " + e.getMessage());
            logger.error("Erreur suppression quartier", e);
        }
    }

    /**
     * Affiche les détails d'un quartier
     */
    private void afficherDetailsQuartier(Quartier quartier) {
        ConsoleOutput.separateur();
        System.out.println("  ID:               " + quartier.getId());
        System.out.println("  Nom:            " + quartier.getNom());

        // Récupérer et afficher la zone
        Optional<Zone> zoneOpt = zoneService.obtenirZoneParId(quartier.getIdZone());
        if (zoneOpt.isPresent()) {
            Zone zone = zoneOpt.get();
            System.out.println("  Zone:            " + zone.getNom());
            System.out.println("  Prix livraison:  " + zone.getPrixLivraison() + " FCFA");
        } else {
            System.out.println("  Zone ID:         " + quartier.getIdZone());
        }

        ConsoleOutput.separateur();
    }

    /**
     * Affiche un tableau de quartiers
     */
    private void afficherTableauQuartiers(List<Quartier> quartiers) {
        TableFormatter table = new TableFormatter(
                "ID",
                "Nom",
                "Zone",
                "Prix Livraison"
        );

        for (Quartier quartier : quartiers) {
            Optional<Zone> zoneOpt = zoneService.obtenirZoneParId(quartier.getIdZone());
            String nomZone = zoneOpt.isPresent() ? zoneOpt.get().getNom() : "N/A";
            String prixLivraison = zoneOpt.isPresent() ? zoneOpt.get().getPrixLivraison() + " FCFA" : "N/A";

            table.ajouterLigne(
                    String.valueOf(quartier.getId()),
                    quartier.getNom(),
                    nomZone,
                    prixLivraison
            );
        }

        table.afficher();
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