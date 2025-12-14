package com.brasilburger.presentation.console.controllers;

import com.brasilburger.domain.entities.*;
import com.brasilburger.domain.entities.enums.CategorieArticle;
import com.brasilburger.domain.entities.enums.TypeComplement;
import com.brasilburger.domain.services.IArticleService;
import com.brasilburger.domain.services.IImageStorageService;
import com.brasilburger.domain.valueobjects.ImageInfo;
import com.brasilburger.factories.ImageStorageFactory;
import com.brasilburger.factories.ServiceFactory;
import com.brasilburger.infrastructure.cloudinary.CloudinaryFolders;
import com.brasilburger.presentation.console.utils.ConsoleInput;
import com.brasilburger.presentation.console.utils.ConsoleOutput;
import com.brasilburger.presentation.console.utils.TableFormatter;
import com.brasilburger.presentation.validators.ArticleValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * Contrôleur pour la gestion des articles
 * Gère l'interaction utilisateur et la logique de présentation
 */
public class ArticleController {

    private static final Logger logger = LoggerFactory.getLogger(ArticleController.class);
    private final IArticleService articleService;
    private final IImageStorageService imageService;

    public ArticleController() {
        this.articleService = ServiceFactory.createArticleService();
        this.imageService = ImageStorageFactory.createImageStorageService();
    }

    /**
     * Affiche le menu de gestion des articles
     */
    public void afficherMenu() {
        boolean continuer = true;

        while (continuer) {
            try {
                ConsoleOutput.titre("GESTION DES ARTICLES");

                System.out.println("=== Création ===");
                System.out.println("1.Créer un burger");
                System.out.println("2.Créer un menu");
                System.out.println("3.Créer un complément");

                System.out.println("\n=== Consultation ===");
                System.out.println("4.Lister tous les articles");
                System.out.println("5.Lister les burgers");
                System.out.println("6.Lister les menus");
                System.out.println("7.Lister les compléments");
                System.out.println("8.Rechercher un article");

                System.out.println("\n=== Modification ===");
                System.out.println("9.Modifier un article");
                System.out.println("10.Archiver un article");
                System.out.println("11.Restaurer un article");
                System.out.println("12.Supprimer un article");

                System.out.println("\n0.Retour au menu principal");

                int choix = ConsoleInput.lireChoixMenu(12);

                switch (choix) {
                    case 1:
                        creerBurger();
                        break;
                    case 2:
                        creerMenu();
                        break;
                    case 3:
                        creerComplement();
                        break;
                    case 4:
                        listerTousLesArticles();
                        break;
                    case 5:
                        listerBurgers();
                        break;
                    case 6:
                        listerMenus();
                        break;
                    case 7:
                        listerComplements();
                        break;
                    case 8:
                        rechercherArticle();
                        break;
                    case 9:
                        modifierArticle();
                        break;
                    case 10:
                        archiverArticle();
                        break;
                    case 11:
                        restaurerArticle();
                        break;
                    case 12:
                        supprimerArticle();
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
                logger.error("Erreur dans le menu article", e);
                ConsoleInput.attendreEntree();
            }
        }
    }

    /**
     * Crée un nouveau burger
     */
    private void creerBurger() {
        ConsoleOutput.sousTitre("Création d'un burger");

        try {
            // Saisie du libellé
            String libelle = ConsoleInput.lireTexteNonVide("Libellé:  ");

            // Validation du libellé
            List<String> erreursLibelle = ArticleValidator.validerLibelle(libelle);
            if (!erreursLibelle.isEmpty()) {
                ConsoleOutput.erreur("Libellé invalide:");
                erreursLibelle.forEach(e -> System.out.println("  - " + e));
                return;
            }

            // Saisie de la description
            String description = ConsoleInput.lireTexteNonVide("Description: ");

            // Validation de la description
            List<String> erreursDesc = ArticleValidator.validerDescription(description);
            if (!erreursDesc.isEmpty()) {
                ConsoleOutput.erreur("Description invalide:");
                erreursDesc.forEach(e -> System.out.println("  - " + e));
                return;
            }

            // Saisie du prix
            int prix = ConsoleInput.lireEntierPositif("Prix (FCFA): ");

            // Validation du prix
            List<String> erreursPrix = ArticleValidator.validerPrix(prix);
            if (!erreursPrix.isEmpty()) {
                ConsoleOutput.erreur("Prix invalide:");
                erreursPrix.forEach(e -> System.out.println("  - " + e));
                return;
            }

            // Upload de l'image (obligatoire)
            ConsoleOutput.info("L'image est OBLIGATOIRE");
            String cheminImage = ConsoleInput.lireTexteNonVide("Chemin de l'image: ");

            ConsoleOutput.chargement("Upload de l'image");
            ImageInfo imageInfo = imageService.uploadImage(cheminImage, CloudinaryFolders.getBurgers());
            ConsoleOutput.effacerLigne();
            ConsoleOutput.succes("Image uploadée:  " + imageInfo.getPublicId());

            // Confirmation
            ConsoleOutput.info("Burger à créer:");
            System.out.println("  Libellé:       " + libelle);
            System.out.println("  Description:  " + description);
            System.out.println("  Prix:        " + prix + " FCFA");
            System.out.println("  Image:       " + imageInfo.getPublicId());

            if (! ConsoleInput.lireConfirmation("Confirmer la création")) {
                // Supprimer l'image uploadée
                imageService.deleteImage(imageInfo.getPublicId());
                ConsoleOutput.avertissement("Création annulée - Image supprimée");
                return;
            }

            // Création
            Burger burger = articleService.creerBurger(libelle, description, prix, imageInfo.getPublicId());

            ConsoleOutput.succes("Burger créé avec succès!");
            afficherDetailsArticle(burger);

        } catch (Exception e) {
            ConsoleOutput.erreur("Erreur lors de la création: " + e.getMessage());
            logger.error("Erreur création burger", e);
        }
    }

    /**
     * Crée un nouveau menu
     */
    private void creerMenu() {
        ConsoleOutput.sousTitre("Création d'un menu");

        try {
            // Saisie du libellé
            String libelle = ConsoleInput.lireTexteNonVide("Libellé:  ");

            // Validation du libellé
            List<String> erreursLibelle = ArticleValidator.validerLibelle(libelle);
            if (!erreursLibelle.isEmpty()) {
                ConsoleOutput.erreur("Libellé invalide:");
                erreursLibelle.forEach(e -> System.out.println("  - " + e));
                return;
            }

            // Saisie de la description
            String description = ConsoleInput.lireTexteNonVide("Description: ");

            // Validation de la description
            List<String> erreursDesc = ArticleValidator.validerDescription(description);
            if (!erreursDesc.isEmpty()) {
                ConsoleOutput.erreur("Description invalide:");
                erreursDesc.forEach(e -> System.out.println("  - " + e));
                return;
            }

            // Upload de l'image (obligatoire)
            ConsoleOutput.info("L'image est OBLIGATOIRE");
            String cheminImage = ConsoleInput.lireTexteNonVide("Chemin de l'image: ");

            ConsoleOutput.chargement("Upload de l'image");
            ImageInfo imageInfo = imageService.uploadImage(cheminImage, CloudinaryFolders.getMenus());
            ConsoleOutput.effacerLigne();
            ConsoleOutput.succes("Image uploadée: " + imageInfo.getPublicId());

            // Confirmation
            ConsoleOutput.info("Menu à créer:");
            System.out.println("  Libellé:      " + libelle);
            System.out.println("  Description: " + description);
            System.out.println("  Image:       " + imageInfo.getPublicId());

            if (!ConsoleInput.lireConfirmation("Confirmer la création")) {
                imageService.deleteImage(imageInfo.getPublicId());
                ConsoleOutput.avertissement("Création annulée - Image supprimée");
                return;
            }

            // Création
            Menu menu = articleService.creerMenu(libelle, description, imageInfo.getPublicId());

            ConsoleOutput.succes("Menu créé avec succès!");
            afficherDetailsArticle(menu);

        } catch (Exception e) {
            ConsoleOutput.erreur("Erreur lors de la création: " + e.getMessage());
            logger.error("Erreur création menu", e);
        }
    }

    /**
     * Crée un nouveau complément
     */
    private void creerComplement() {
        ConsoleOutput.sousTitre("Création d'un complément");

        try {
            // Saisie du libellé
            String libelle = ConsoleInput.lireTexteNonVide("Libellé: ");

            // Validation du libellé
            List<String> erreursLibelle = ArticleValidator.validerLibelle(libelle);
            if (!erreursLibelle.isEmpty()) {
                ConsoleOutput.erreur("Libellé invalide:");
                erreursLibelle.forEach(e -> System.out.println("  - " + e));
                return;
            }

            // Saisie du type
            System.out.println("\nType de complément:");
            System.out.println("1.Boisson");
            System.out.println("2.Frites");
            int choixType = ConsoleInput.lireEntier("Votre choix: ", 1, 2);
            TypeComplement type = (choixType == 1) ? TypeComplement.BOISSON :  TypeComplement.FRITES;

            // Saisie du prix
            int prix = ConsoleInput.lireEntierPositif("Prix (FCFA): ");

            // Validation du prix
            List<String> erreursPrix = ArticleValidator.validerPrix(prix);
            if (!erreursPrix.isEmpty()) {
                ConsoleOutput.erreur("Prix invalide:");
                erreursPrix.forEach(e -> System.out.println("  - " + e));
                return;
            }

            // Upload de l'image (obligatoire)
            ConsoleOutput.info("L'image est OBLIGATOIRE");
            String cheminImage = ConsoleInput.lireTexteNonVide("Chemin de l'image: ");

            ConsoleOutput.chargement("Upload de l'image");
            ImageInfo imageInfo = imageService.uploadImage(cheminImage, CloudinaryFolders.getComplements());
            ConsoleOutput.effacerLigne();
            ConsoleOutput.succes("Image uploadée: " + imageInfo.getPublicId());

            // Confirmation
            ConsoleOutput.info("Complément à créer:");
            System.out.println("  Libellé:  " + libelle);
            System.out.println("  Type:    " + type);
            System.out.println("  Prix:    " + prix + " FCFA");
            System.out.println("  Image:   " + imageInfo.getPublicId());

            if (!ConsoleInput.lireConfirmation("Confirmer la création")) {
                imageService.deleteImage(imageInfo.getPublicId());
                ConsoleOutput.avertissement("Création annulée - Image supprimée");
                return;
            }

            // Création
            Complement complement = articleService.creerComplement(libelle, type, prix, imageInfo.getPublicId());

            ConsoleOutput.succes("Complément créé avec succès!");
            afficherDetailsArticle(complement);

        } catch (Exception e) {
            ConsoleOutput.erreur("Erreur lors de la création: " + e.getMessage());
            logger.error("Erreur création complément", e);
        }
    }

    /**
     * Liste tous les articles
     */
    private void listerTousLesArticles() {
        ConsoleOutput.sousTitre("Liste de tous les articles");

        try {
            List<Article> articles = articleService.listerTousLesArticles();

            if (articles.isEmpty()) {
                ConsoleOutput.info("Aucun article trouvé");
                return;
            }

            afficherTableauArticles(articles);

            // Statistiques
            long nbBurgers = articleService.compterArticlesParCategorie(CategorieArticle.BURGER);
            long nbMenus = articleService.compterArticlesParCategorie(CategorieArticle.MENU);
            long nbComplements = articleService.compterArticlesParCategorie(CategorieArticle.COMPLEMENT);

            System.out.println("\nStatistiques:");
            System.out.println("  Burgers:      " + nbBurgers);
            System.out.println("  Menus:       " + nbMenus);
            System.out.println("  Compléments: " + nbComplements);
            System.out.println("  Total:       " + articles.size());

        } catch (Exception e) {
            ConsoleOutput.erreur("Erreur lors du listage: " + e.getMessage());
            logger.error("Erreur listage articles", e);
        }
    }

    /**
     * Liste les burgers
     */
    private void listerBurgers() {
        ConsoleOutput.sousTitre("Liste des burgers");

        try {
            List<Burger> burgers = articleService.listerBurgersDisponibles();

            if (burgers.isEmpty()) {
                ConsoleOutput.info("Aucun burger disponible");
                return;
            }

            afficherTableauBurgers(burgers);

        } catch (Exception e) {
            ConsoleOutput.erreur("Erreur lors du listage:  " + e.getMessage());
            logger.error("Erreur listage burgers", e);
        }
    }

    /**
     * Liste les menus
     */
    private void listerMenus() {
        ConsoleOutput.sousTitre("Liste des menus");

        try {
            List<Menu> menus = articleService.listerMenusDisponibles();

            if (menus.isEmpty()) {
                ConsoleOutput.info("Aucun menu disponible");
                return;
            }

            afficherTableauMenus(menus);

        } catch (Exception e) {
            ConsoleOutput.erreur("Erreur lors du listage: " + e.getMessage());
            logger.error("Erreur listage menus", e);
        }
    }

    /**
     * Liste les compléments
     */
    private void listerComplements() {
        ConsoleOutput.sousTitre("Liste des compléments");

        try {
            List<Complement> complements = articleService.listerComplementsDisponibles();

            if (complements.isEmpty()) {
                ConsoleOutput.info("Aucun complément disponible");
                return;
            }

            afficherTableauComplements(complements);

        } catch (Exception e) {
            ConsoleOutput.erreur("Erreur lors du listage: " + e.getMessage());
            logger.error("Erreur listage compléments", e);
        }
    }

    /**
     * Recherche un article par code ou ID
     */
    private void rechercherArticle() {
        ConsoleOutput.sousTitre("Recherche d'article");

        try {
            System.out.println("1.Rechercher par ID");
            System.out.println("2.Rechercher par code");

            int choix = ConsoleInput.lireEntier("Votre choix:  ", 1, 2);

            Optional<Article> articleOpt;

            if (choix == 1) {
                long id = ConsoleInput.lireLong("ID de l'article: ");
                articleOpt = articleService.obtenirArticleParId(id);
            } else {
                String code = ConsoleInput.lireTexteNonVide("Code de l'article (ex: BRG001): ");
                articleOpt = articleService.obtenirArticleParCode(code);
            }

            if (articleOpt.isPresent()) {
                ConsoleOutput.succes("Article trouvé:");
                afficherDetailsArticle(articleOpt.get());
            } else {
                ConsoleOutput.avertissement("Article non trouvé");
            }

        } catch (Exception e) {
            ConsoleOutput.erreur("Erreur lors de la recherche: " + e.getMessage());
            logger.error("Erreur recherche article", e);
        }
    }

    /**
     * Modifie un article
     */
    private void modifierArticle() {
        ConsoleOutput.sousTitre("Modification d'un article");

        try {
            // Sélection de l'article
            String code = ConsoleInput.lireTexteNonVide("Code de l'article à modifier: ");
            Optional<Article> articleOpt = articleService.obtenirArticleParCode(code);

            if (! articleOpt.isPresent()) {
                ConsoleOutput.erreur("Article non trouvé");
                return;
            }

            Article article = articleOpt.get();
            ConsoleOutput.info("Article actuel:");
            afficherDetailsArticle(article);

            // Saisie des nouvelles valeurs
            System.out.println("\nLaissez vide pour conserver la valeur actuelle");

            String nouveauLibelle = ConsoleInput.lireTexteOptional("Nouveau libellé [" + article.getLibelle() + "]: ");
            if (! nouveauLibelle.isEmpty()) {
                List<String> erreurs = ArticleValidator.validerLibelle(nouveauLibelle);
                if (!erreurs.isEmpty()) {
                    ConsoleOutput.erreur("Libellé invalide:");
                    erreurs.forEach(e -> System.out.println("  - " + e));
                    return;
                }
            }

            // Changement d'image
            boolean changerImage = ConsoleInput.lireConfirmation("Changer l'image");
            String nouvelleImageId = null;

            if (changerImage) {
                String cheminImage = ConsoleInput.lireTexteNonVide("Chemin de la nouvelle image: ");

                ConsoleOutput.chargement("Upload de la nouvelle image");
                String folder = CloudinaryFolders.getSubfolderForArticle(article.getCategorie().name());
                ImageInfo imageInfo = imageService.uploadImage(cheminImage, folder);
                ConsoleOutput.effacerLigne();
                ConsoleOutput.succes("Nouvelle image uploadée: " + imageInfo.getPublicId());

                nouvelleImageId = imageInfo.getPublicId();
            }

            // Vérifier si des modifications ont été faites
            if (nouveauLibelle.isEmpty() && !changerImage) {
                ConsoleOutput.avertissement("Aucune modification effectuée");
                return;
            }

            // Confirmation
            if (! ConsoleInput.lireConfirmation("Confirmer les modifications")) {
                if (nouvelleImageId != null) {
                    imageService.deleteImage(nouvelleImageId);
                    ConsoleOutput.info("Nouvelle image supprimée");
                }
                ConsoleOutput.avertissement("Modification annulée");
                return;
            }

            // Supprimer l'ancienne image si changement
            if (nouvelleImageId != null) {
                imageService.deleteImage(article.getImagePublicId());
                ConsoleOutput.info("Ancienne image supprimée");
            }

            // Modification
            Article articleModifie = articleService.modifierArticle(
                    article.getId(),
                    nouveauLibelle.isEmpty() ? null : nouveauLibelle,
                    nouvelleImageId
            );

            ConsoleOutput.succes("Article modifié avec succès!");
            afficherDetailsArticle(articleModifie);

        } catch (Exception e) {
            ConsoleOutput.erreur("Erreur lors de la modification: " + e.getMessage());
            logger.error("Erreur modification article", e);
        }
    }

    /**
     * Archive un article
     */
    private void archiverArticle() {
        ConsoleOutput.sousTitre("Archivage d'un article");

        try {
            String code = ConsoleInput.lireTexteNonVide("Code de l'article à archiver:  ");
            Optional<Article> articleOpt = articleService.obtenirArticleParCode(code);

            if (!articleOpt.isPresent()) {
                ConsoleOutput.erreur("Article non trouvé");
                return;
            }

            Article article = articleOpt.get();

            if (article.isEstArchiver()) {
                ConsoleOutput.avertissement("Cet article est déjà archivé");
                return;
            }

            ConsoleOutput.info("Article à archiver:");
            afficherDetailsArticle(article);

            if (!ConsoleInput.lireConfirmation("Confirmer l'archivage")) {
                ConsoleOutput.avertissement("Archivage annulé");
                return;
            }

            articleService.archiverArticle(article.getId());
            ConsoleOutput.succes("Article archivé avec succès");

        } catch (Exception e) {
            ConsoleOutput.erreur("Erreur lors de l'archivage: " + e.getMessage());
            logger.error("Erreur archivage article", e);
        }
    }

    /**
     * Restaure un article archivé
     */
    private void restaurerArticle() {
        ConsoleOutput.sousTitre("Restauration d'un article");

        try {
            String code = ConsoleInput.lireTexteNonVide("Code de l'article à restaurer: ");
            Optional<Article> articleOpt = articleService.obtenirArticleParCode(code);

            if (!articleOpt.isPresent()) {
                ConsoleOutput.erreur("Article non trouvé");
                return;
            }

            Article article = articleOpt.get();

            if (!article.isEstArchiver()) {
                ConsoleOutput.avertissement("Cet article n'est pas archivé");
                return;
            }

            if (! ConsoleInput.lireConfirmation("Confirmer la restauration")) {
                ConsoleOutput.avertissement("Restauration annulée");
                return;
            }

            articleService.restaurerArticle(article.getId());
            ConsoleOutput.succes("Article restauré avec succès");

        } catch (Exception e) {
            ConsoleOutput.erreur("Erreur lors de la restauration: " + e.getMessage());
            logger.error("Erreur restauration article", e);
        }
    }

    /**
     * Supprime un article
     */
    private void supprimerArticle() {
        ConsoleOutput.sousTitre("Suppression d'un article");

        try {
            String code = ConsoleInput.lireTexteNonVide("Code de l'article à supprimer: ");
            Optional<Article> articleOpt = articleService.obtenirArticleParCode(code);

            if (! articleOpt.isPresent()) {
                ConsoleOutput.erreur("Article non trouvé");
                return;
            }

            Article article = articleOpt.get();

            ConsoleOutput.avertissement("Article à supprimer:");
            afficherDetailsArticle(article);

            ConsoleOutput.avertissement("ATTENTION: Cette action est irréversible!");
            ConsoleOutput.avertissement("L'image sera également supprimée de Cloudinary");

            if (!ConsoleInput.lireConfirmation("Confirmer la suppression")) {
                ConsoleOutput.avertissement("Suppression annulée");
                return;
            }

            // Double confirmation
            if (!ConsoleInput.lireConfirmation("Êtes-vous vraiment sûr")) {
                ConsoleOutput.avertissement("Suppression annulée");
                return;
            }

            // Supprimer l'image
            imageService.deleteImage(article.getImagePublicId());
            ConsoleOutput.info("Image supprimée de Cloudinary");

            // Supprimer l'article
            articleService.supprimerArticle(article.getId());
            ConsoleOutput.succes("Article supprimé avec succès");

        } catch (Exception e) {
            ConsoleOutput.erreur("Erreur lors de la suppression: " + e.getMessage());
            logger.error("Erreur suppression article", e);
        }
    }

    /**
     * Affiche les détails d'un article
     */
    private void afficherDetailsArticle(Article article) {
        ConsoleOutput.separateur();
        System.out.println("  Code:         " + article.getCode());
        System.out.println("  Libellé:     " + article.getLibelle());
        System.out.println("  Catégorie:   " + article.getCategorie());

        if (article instanceof Burger) {
            Burger burger = (Burger) article;
            System.out.println("  Description: " + burger.getDescription());
            System.out.println("  Prix:        " + burger.getPrix() + " FCFA");
        } else if (article instanceof Menu) {
            Menu menu = (Menu) article;
            System.out.println("  Description: " + menu.getDescription());
        } else if (article instanceof Complement) {
            Complement complement = (Complement) article;
            System.out.println("  Type:        " + complement.getType());
            System.out.println("  Prix:        " + complement.getPrix() + " FCFA");
        }

        System.out.println("  Image:       " + article.getImagePublicId());
        System.out.println("  Statut:      " + (article.isEstArchiver() ? "Archivé" : "Disponible"));
    }

    /**
     * Affiche un tableau d'articles
     */
    private void afficherTableauArticles(List<Article> articles) {
        TableFormatter table = new TableFormatter(
                "Code",
                "Libellé",
                "Catégorie",
                "Prix",
                "Statut"
        );

        for (Article article : articles) {
            String prix = "-";
            if (article instanceof Burger) {
                prix = ((Burger) article).getPrix() + " FCFA";
            } else if (article instanceof Complement) {
                prix = ((Complement) article).getPrix() + " FCFA";
            }

            table.ajouterLigne(
                    article.getCode(),
                    article.getLibelle(),
                    article.getCategorie().toString(),
                    prix,
                    article.isEstArchiver() ? "Archivé" :  "Disponible"
            );
        }

        table.afficher();
    }

    /**
     * Affiche un tableau de burgers
     */
    private void afficherTableauBurgers(List<Burger> burgers) {
        TableFormatter table = new TableFormatter(
                "Code",
                "Libellé",
                "Prix",
                "Description"
        );

        for (Burger burger : burgers) {
            String desc = burger.getDescription();
            if (desc.length() > 40) {
                desc = desc.substring(0, 37) + "...";
            }

            table.ajouterLigne(
                    burger.getCode(),
                    burger.getLibelle(),
                    burger.getPrix() + " FCFA",
                    desc
            );
        }

        table.afficher();
    }

    /**
     * Affiche un tableau de menus
     */
    private void afficherTableauMenus(List<Menu> menus) {
        TableFormatter table = new TableFormatter(
                "Code",
                "Libellé",
                "Description"
        );

        for (Menu menu : menus) {
            String desc = menu.getDescription();
            if (desc.length() > 50) {
                desc = desc.substring(0, 47) + "...";
            }

            table.ajouterLigne(
                    menu.getCode(),
                    menu.getLibelle(),
                    desc
            );
        }

        table.afficher();
    }

    /**
     * Affiche un tableau de compléments
     */
    private void afficherTableauComplements(List<Complement> complements) {
        TableFormatter table = new TableFormatter(
                "Code",
                "Libellé",
                "Type",
                "Prix"
        );

        for (Complement complement : complements) {
            table.ajouterLigne(
                    complement.getCode(),
                    complement.getLibelle(),
                    complement.getType().toString(),
                    complement.getPrix() + " FCFA"
            );
        }

        table.afficher();
    }
}