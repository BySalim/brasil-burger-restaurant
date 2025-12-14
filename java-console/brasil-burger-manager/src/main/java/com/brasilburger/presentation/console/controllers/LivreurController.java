package com.brasilburger.presentation.console.controllers;

import com.brasilburger.domain.entities.Livreur;
import com.brasilburger.domain.services.ILivreurService;
import com.brasilburger.factories.ServiceFactory;
import com.brasilburger.presentation.console.utils.ConsoleInput;
import com.brasilburger.presentation.console.utils.ConsoleOutput;
import com.brasilburger.presentation.console.utils.TableFormatter;
import com.brasilburger.presentation.validators.LivreurValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * Contrôleur pour la gestion des livreurs
 * Gère l'interaction utilisateur et la logique de présentation
 */
public class LivreurController {

    private static final Logger logger = LoggerFactory.getLogger(LivreurController.class);
    private final ILivreurService livreurService;

    public LivreurController() {
        this.livreurService = ServiceFactory.createLivreurService();
    }

    /**
     * Affiche le menu de gestion des livreurs
     */
    public void afficherMenu() {
        boolean continuer = true;

        while (continuer) {
            try {
                ConsoleOutput.titre("GESTION DES LIVREURS");

                System.out.println("1.Créer un livreur");
                System.out.println("2.Lister tous les livreurs");
                System.out.println("3.Lister les livreurs disponibles");
                System.out.println("4.Lister les livreurs occupés");
                System.out.println("5.Rechercher un livreur");
                System.out.println("6.Modifier un livreur");
                System.out.println("7.Marquer disponible/occupé");
                System.out.println("8.Archiver un livreur");
                System.out.println("9.Restaurer un livreur");
                System.out.println("10.Supprimer un livreur");
                System.out.println("0.Retour au menu principal");

                int choix = ConsoleInput.lireChoixMenu(10);

                switch (choix) {
                    case 1:
                        creerLivreur();
                        break;
                    case 2:
                        listerTousLesLivreurs();
                        break;
                    case 3:
                        listerLivreursDisponibles();
                        break;
                    case 4:
                        listerLivreursOccupes();
                        break;
                    case 5:
                        rechercherLivreur();
                        break;
                    case 6:
                        modifierLivreur();
                        break;
                    case 7:
                        changerDisponibilite();
                        break;
                    case 8:
                        archiverLivreur();
                        break;
                    case 9:
                        restaurerLivreur();
                        break;
                    case 10:
                        supprimerLivreur();
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
                logger.error("Erreur dans le menu livreur", e);
                ConsoleInput.attendreEntree();
            }
        }
    }

    /**
     * Crée un nouveau livreur
     */
    private void creerLivreur() {
        ConsoleOutput.sousTitre("Création d'un livreur");

        try {
            // Saisie du nom
            String nom = ConsoleInput.lireTexteNonVide("Nom:  ");

            // Validation du nom
            List<String> erreursNom = LivreurValidator.validerNom(nom);
            if (!erreursNom.isEmpty()) {
                ConsoleOutput.erreur("Nom invalide:");
                erreursNom.forEach(e -> System.out.println("  - " + e));
                return;
            }

            // Saisie du prénom
            String prenom = ConsoleInput.lireTexteNonVide("Prénom: ");

            // Validation du prénom
            List<String> erreursPrenom = LivreurValidator.validerPrenom(prenom);
            if (!erreursPrenom.isEmpty()) {
                ConsoleOutput.erreur("Prénom invalide:");
                erreursPrenom.forEach(e -> System.out.println("  - " + e));
                return;
            }

            // Saisie du téléphone
            String telephone = ConsoleInput.lireTexteNonVide("Téléphone (ex: 771234567): ");

            // Validation du téléphone
            List<String> erreursTel = LivreurValidator.validerTelephone(telephone);
            if (!erreursTel.isEmpty()) {
                ConsoleOutput.erreur("Téléphone invalide:");
                erreursTel.forEach(e -> System.out.println("  - " + e));
                return;
            }

            // Vérifier si le téléphone existe déjà
            if (livreurService.livreurExiste(telephone)) {
                ConsoleOutput.erreur("Un livreur avec ce numéro existe déjà");
                return;
            }

            // Confirmation
            ConsoleOutput.info("Livreur à créer:");
            System.out.println("  Nom:        " + nom);
            System.out.println("  Prénom:    " + prenom);
            System.out.println("  Téléphone: " + telephone);

            if (! ConsoleInput.lireConfirmation("Confirmer la création")) {
                ConsoleOutput.avertissement("Création annulée");
                return;
            }

            // Création
            Livreur livreur = livreurService.creerLivreur(nom, prenom, telephone);

            ConsoleOutput.succes("Livreur créé avec succès!");
            afficherDetailsLivreur(livreur);

        } catch (Exception e) {
            ConsoleOutput.erreur("Erreur lors de la création: " + e.getMessage());
            logger.error("Erreur création livreur", e);
        }
    }

    /**
     * Liste tous les livreurs
     */
    private void listerTousLesLivreurs() {
        ConsoleOutput.sousTitre("Liste de tous les livreurs");

        try {
            List<Livreur> livreurs = livreurService.listerTousLesLivreurs();

            if (livreurs.isEmpty()) {
                ConsoleOutput.info("Aucun livreur trouvé");
                return;
            }

            afficherTableauLivreurs(livreurs);

            // Statistiques
            long nbTotal = livreurService.compterLivreurs();
            long nbDisponibles = livreurService.compterLivreursDisponibles();

            System.out.println("\nStatistiques:");
            System.out.println("  Total:  " + nbTotal);
            System.out.println("  Disponibles: " + nbDisponibles);
            System.out.println("  Occupés: " + (nbTotal - nbDisponibles));

        } catch (Exception e) {
            ConsoleOutput.erreur("Erreur lors du listage: " + e.getMessage());
            logger.error("Erreur listage livreurs", e);
        }
    }

    /**
     * Liste les livreurs disponibles
     */
    private void listerLivreursDisponibles() {
        ConsoleOutput.sousTitre("Liste des livreurs disponibles");

        try {
            List<Livreur> livreurs = livreurService.listerLivreursDisponibles();

            if (livreurs.isEmpty()) {
                ConsoleOutput.info("Aucun livreur disponible");
                return;
            }

            afficherTableauLivreurs(livreurs);

        } catch (Exception e) {
            ConsoleOutput.erreur("Erreur lors du listage:  " + e.getMessage());
            logger.error("Erreur listage livreurs disponibles", e);
        }
    }

    /**
     * Liste les livreurs occupés
     */
    private void listerLivreursOccupes() {
        ConsoleOutput.sousTitre("Liste des livreurs occupés");

        try {
            List<Livreur> livreurs = livreurService.listerLivreursOccupes();

            if (livreurs.isEmpty()) {
                ConsoleOutput.info("Aucun livreur occupé");
                return;
            }

            afficherTableauLivreurs(livreurs);

        } catch (Exception e) {
            ConsoleOutput.erreur("Erreur lors du listage: " + e.getMessage());
            logger.error("Erreur listage livreurs occupés", e);
        }
    }

    /**
     * Recherche un livreur par téléphone ou ID
     */
    private void rechercherLivreur() {
        ConsoleOutput.sousTitre("Recherche de livreur");

        try {
            System.out.println("1.Rechercher par ID");
            System.out.println("2.Rechercher par téléphone");

            int choix = ConsoleInput.lireEntier("Votre choix:  ", 1, 2);

            Optional<Livreur> livreurOpt;

            if (choix == 1) {
                long id = ConsoleInput.lireLong("ID du livreur: ");
                livreurOpt = livreurService.obtenirLivreurParId(id);
            } else {
                String telephone = ConsoleInput.lireTexteNonVide("Téléphone: ");
                livreurOpt = livreurService.obtenirLivreurParTelephone(telephone);
            }

            if (livreurOpt.isPresent()) {
                ConsoleOutput.succes("Livreur trouvé:");
                afficherDetailsLivreur(livreurOpt.get());
            } else {
                ConsoleOutput.avertissement("Livreur non trouvé");
            }

        } catch (Exception e) {
            ConsoleOutput.erreur("Erreur lors de la recherche: " + e.getMessage());
            logger.error("Erreur recherche livreur", e);
        }
    }

    /**
     * Modifie un livreur
     */
    private void modifierLivreur() {
        ConsoleOutput.sousTitre("Modification d'un livreur");

        try {
            // Sélection du livreur
            long id = ConsoleInput.lireLong("ID du livreur à modifier: ");
            Optional<Livreur> livreurOpt = livreurService.obtenirLivreurParId(id);

            if (!livreurOpt.isPresent()) {
                ConsoleOutput.erreur("Livreur non trouvé");
                return;
            }

            Livreur livreur = livreurOpt.get();
            ConsoleOutput.info("Livreur actuel:");
            afficherDetailsLivreur(livreur);

            // Saisie des nouvelles valeurs
            System.out.println("\nLaissez vide pour conserver la valeur actuelle");

            String nouveauNom = ConsoleInput.lireTexteOptional("Nouveau nom [" + livreur.getNom() + "]: ");
            if (! nouveauNom.isEmpty()) {
                List<String> erreurs = LivreurValidator.validerNom(nouveauNom);
                if (!erreurs.isEmpty()) {
                    ConsoleOutput.erreur("Nom invalide:");
                    erreurs.forEach(e -> System.out.println("  - " + e));
                    return;
                }
            }

            String nouveauPrenom = ConsoleInput.lireTexteOptional("Nouveau prénom [" + livreur.getPrenom() + "]: ");
            if (!nouveauPrenom.isEmpty()) {
                List<String> erreurs = LivreurValidator.validerPrenom(nouveauPrenom);
                if (!erreurs.isEmpty()) {
                    ConsoleOutput.erreur("Prénom invalide:");
                    erreurs.forEach(e -> System.out.println("  - " + e));
                    return;
                }
            }

            String nouveauTel = ConsoleInput.lireTexteOptional("Nouveau téléphone [" + livreur.getTelephone() + "]: ");
            if (!nouveauTel.isEmpty()) {
                List<String> erreurs = LivreurValidator.validerTelephone(nouveauTel);
                if (!erreurs.isEmpty()) {
                    ConsoleOutput.erreur("Téléphone invalide:");
                    erreurs.forEach(e -> System.out.println("  - " + e));
                    return;
                }

                // Vérifier si le nouveau téléphone existe déjà (sauf si c'est le même)
                if (!nouveauTel.equals(livreur.getTelephone()) && livreurService.livreurExiste(nouveauTel)) {
                    ConsoleOutput.erreur("Ce numéro est déjà utilisé par un autre livreur");
                    return;
                }
            }

            // Vérifier si des modifications ont été faites
            if (nouveauNom.isEmpty() && nouveauPrenom.isEmpty() && nouveauTel.isEmpty()) {
                ConsoleOutput.avertissement("Aucune modification effectuée");
                return;
            }

            // Confirmation
            if (!ConsoleInput.lireConfirmation("Confirmer les modifications")) {
                ConsoleOutput.avertissement("Modification annulée");
                return;
            }

            // Modification
            Livreur livreurModifie = livreurService.modifierLivreur(
                    id,
                    nouveauNom.isEmpty() ? null : nouveauNom,
                    nouveauPrenom.isEmpty() ? null : nouveauPrenom,
                    nouveauTel.isEmpty() ? null : nouveauTel
            );

            ConsoleOutput.succes("Livreur modifié avec succès!");
            afficherDetailsLivreur(livreurModifie);

        } catch (Exception e) {
            ConsoleOutput.erreur("Erreur lors de la modification: " + e.getMessage());
            logger.error("Erreur modification livreur", e);
        }
    }

    /**
     * Change la disponibilité d'un livreur
     */
    private void changerDisponibilite() {
        ConsoleOutput.sousTitre("Changement de disponibilité");

        try {
            long id = ConsoleInput.lireLong("ID du livreur: ");
            Optional<Livreur> livreurOpt = livreurService.obtenirLivreurParId(id);

            if (!livreurOpt.isPresent()) {
                ConsoleOutput.erreur("Livreur non trouvé");
                return;
            }

            Livreur livreur = livreurOpt.get();

            if (livreur.isEstArchiver()) {
                ConsoleOutput.erreur("Impossible de modifier la disponibilité d'un livreur archivé");
                return;
            }

            ConsoleOutput.info("Livreur:");
            System.out.println("  " + livreur.getNomComplet());
            System.out.println("  Statut actuel: " + (livreur.isEstDisponible() ? "Disponible" : "Occupé"));

            System.out.println("\n1.Marquer disponible");
            System.out.println("2.Marquer occupé");

            int choix = ConsoleInput.lireEntier("Votre choix:  ", 1, 2);

            if (choix == 1) {
                if (livreur.isEstDisponible()) {
                    ConsoleOutput.avertissement("Le livreur est déjà disponible");
                    return;
                }
                livreurService.marquerDisponible(id);
                ConsoleOutput.succes("Livreur marqué comme disponible");
            } else {
                if (! livreur.isEstDisponible()) {
                    ConsoleOutput.avertissement("Le livreur est déjà occupé");
                    return;
                }
                livreurService.marquerOccupe(id);
                ConsoleOutput.succes("Livreur marqué comme occupé");
            }

        } catch (Exception e) {
            ConsoleOutput.erreur("Erreur lors du changement: " + e.getMessage());
            logger.error("Erreur changement disponibilité", e);
        }
    }

    /**
     * Archive un livreur
     */
    private void archiverLivreur() {
        ConsoleOutput.sousTitre("Archivage d'un livreur");

        try {
            long id = ConsoleInput.lireLong("ID du livreur à archiver: ");
            Optional<Livreur> livreurOpt = livreurService.obtenirLivreurParId(id);

            if (!livreurOpt.isPresent()) {
                ConsoleOutput.erreur("Livreur non trouvé");
                return;
            }

            Livreur livreur = livreurOpt.get();

            if (livreur.isEstArchiver()) {
                ConsoleOutput.avertissement("Ce livreur est déjà archivé");
                return;
            }

            ConsoleOutput.info("Livreur à archiver:");
            afficherDetailsLivreur(livreur);

            if (!ConsoleInput.lireConfirmation("Confirmer l'archivage")) {
                ConsoleOutput.avertissement("Archivage annulé");
                return;
            }

            livreurService.archiverLivreur(id);
            ConsoleOutput.succes("Livreur archivé avec succès");

        } catch (Exception e) {
            ConsoleOutput.erreur("Erreur lors de l'archivage: " + e.getMessage());
            logger.error("Erreur archivage livreur", e);
        }
    }

    /**
     * Restaure un livreur archivé
     */
    private void restaurerLivreur() {
        ConsoleOutput.sousTitre("Restauration d'un livreur");

        try {
            // Afficher les livreurs archivés
            List<Livreur> livreursArchives = livreurService.listerLivreursArchives();

            if (livreursArchives.isEmpty()) {
                ConsoleOutput.info("Aucun livreur archivé à restaurer");
                return;
            }

            ConsoleOutput.info("Livreurs archivés:");
            afficherTableauLivreurs(livreursArchives);

            long id = ConsoleInput.lireLong("\nID du livreur à restaurer: ");
            Optional<Livreur> livreurOpt = livreurService.obtenirLivreurParId(id);

            if (!livreurOpt.isPresent()) {
                ConsoleOutput.erreur("Livreur non trouvé");
                return;
            }

            Livreur livreur = livreurOpt.get();

            if (! livreur.isEstArchiver()) {
                ConsoleOutput.avertissement("Ce livreur n'est pas archivé");
                return;
            }

            if (!ConsoleInput.lireConfirmation("Confirmer la restauration")) {
                ConsoleOutput.avertissement("Restauration annulée");
                return;
            }

            livreurService.restaurerLivreur(id);
            ConsoleOutput.succes("Livreur restauré avec succès");

        } catch (Exception e) {
            ConsoleOutput.erreur("Erreur lors de la restauration: " + e.getMessage());
            logger.error("Erreur restauration livreur", e);
        }
    }

    /**
     * Supprime un livreur
     */
    private void supprimerLivreur() {
        ConsoleOutput.sousTitre("Suppression d'un livreur");

        try {
            long id = ConsoleInput.lireLong("ID du livreur à supprimer: ");
            Optional<Livreur> livreurOpt = livreurService.obtenirLivreurParId(id);

            if (!livreurOpt.isPresent()) {
                ConsoleOutput.erreur("Livreur non trouvé");
                return;
            }

            Livreur livreur = livreurOpt.get();

            ConsoleOutput.avertissement("Livreur à supprimer:");
            afficherDetailsLivreur(livreur);

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

            livreurService.supprimerLivreur(id);
            ConsoleOutput.succes("Livreur supprimé avec succès");

        } catch (Exception e) {
            ConsoleOutput.erreur("Erreur lors de la suppression: " + e.getMessage());
            logger.error("Erreur suppression livreur", e);
        }
    }

    /**
     * Affiche les détails d'un livreur
     */
    private void afficherDetailsLivreur(Livreur livreur) {
        ConsoleOutput.separateur();
        System.out.println("  ID:             " + livreur.getId());
        System.out.println("  Nom complet:   " + livreur.getNomComplet());
        System.out.println("  Téléphone:     " + livreur.getTelephone());
        System.out.println("  Disponibilité: " + (livreur.isEstDisponible() ? "Disponible" : "Occupé"));
        System.out.println("  Statut:        " + (livreur.isEstArchiver() ? "Archivé" : "Actif"));
        System.out.println("  Peut livrer:   " + (livreur.peutEtreAffecte() ? "Oui" : "Non"));
        ConsoleOutput.separateur();
    }

    /**
     * Affiche un tableau de livreurs
     */
    private void afficherTableauLivreurs(List<Livreur> livreurs) {
        TableFormatter table = new TableFormatter(
                "ID",
                "Nom complet",
                "Téléphone",
                "Disponibilité",
                "Statut"
        );

        for (Livreur livreur : livreurs) {
            table.ajouterLigne(
                    String.valueOf(livreur.getId()),
                    livreur.getNomComplet(),
                    livreur.getTelephone(),
                    livreur.isEstDisponible() ? "Disponible" : "Occupé",
                    livreur.isEstArchiver() ? "Archivé" : "Actif"
            );
        }

        table.afficher();
    }
}