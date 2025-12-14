package com.brasilburger.domain.services.impl;

import com.brasilburger.domain.entities.*;
import com.brasilburger.domain.entities.enums.CategorieArticle;
import com.brasilburger.domain.entities.enums.CategorieArticleQuantifier;
import com.brasilburger.domain.entities.enums.TypeComplement;
import com.brasilburger.domain.repositories.IArticleQuantifierRepository;
import com.brasilburger.domain.repositories.IArticleRepository;
import com.brasilburger.domain.services.IArticleService;
import com.brasilburger.domain.services.ICodeArticleGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implémentation du service Article
 * Contient la logique métier pour la gestion des articles
 */
public class ArticleServiceImpl implements IArticleService {
    private static final Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);

    private final IArticleRepository articleRepository;
    private final ICodeArticleGenerator codeGenerator;
    private final IArticleQuantifierRepository articleQuantifierRepository;

    public ArticleServiceImpl(IArticleRepository articleRepository,
                              ICodeArticleGenerator codeGenerator,
                              IArticleQuantifierRepository articleQuantifierRepository) {
        this.articleRepository = articleRepository;
        this.codeGenerator = codeGenerator;
        this.articleQuantifierRepository = articleQuantifierRepository;
    }

    @Override
    public Burger creerBurger(String libelle, String description, Integer prix, String imagePublicId) {
        logger.info("Création d'un burger:  libelle={}, prix={}", libelle, prix);

        // Validation
        if (libelle == null || libelle.trim().isEmpty()) {
            throw new IllegalArgumentException("Le libellé du burger ne peut pas être vide");
        }

        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("La description du burger ne peut pas être vide");
        }

        if (prix == null || prix <= 0) {
            throw new IllegalArgumentException("Le prix doit être supérieur à 0");
        }

        // VALIDATION IMAGE OBLIGATOIRE
        if (imagePublicId == null || imagePublicId.trim().isEmpty()) {
            throw new IllegalArgumentException("L'image est obligatoire pour créer un burger");
        }

        // Génération automatique du code
        String code = codeGenerator.genererCodeBurger();

        // Création
        Burger burger = new Burger(code, libelle.trim(), imagePublicId.trim(), description.trim(), prix);
        burger = (Burger) articleRepository.save(burger);

        logger.info("Burger créé avec succès: ID={}, code={}, libelle={}",
                burger.getId(), burger.getCode(), burger.getLibelle());
        return burger;
    }

    @Override
    public Complement creerComplement(String libelle, TypeComplement type, Integer prix, String imagePublicId) {
        logger.info("Création d'un complément: libelle={}, type={}, prix={}", libelle, type, prix);

        // Validation
        if (libelle == null || libelle.trim().isEmpty()) {
            throw new IllegalArgumentException("Le libellé du complément ne peut pas être vide");
        }

        if (type == null) {
            throw new IllegalArgumentException("Le type du complément ne peut pas être null");
        }

        if (prix == null || prix <= 0) {
            throw new IllegalArgumentException("Le prix doit être supérieur à 0");
        }

        // VALIDATION IMAGE OBLIGATOIRE
        if (imagePublicId == null || imagePublicId.trim().isEmpty()) {
            throw new IllegalArgumentException("L'image est obligatoire pour créer un complément");
        }

        // Génération automatique du code
        String code = codeGenerator.genererCodeComplement();

        // Création
        Complement complement = new Complement(code, libelle.trim(), imagePublicId.trim(), type, prix);
        complement = (Complement) articleRepository.save(complement);

        logger.info("Complément créé avec succès: ID={}, code={}, libelle={}",
                complement.getId(), complement.getCode(), complement.getLibelle());
        return complement;
    }

    @Override
    public Menu creerMenu(String libelle, String description, String imagePublicId) {
        logger.info("Création d'un menu: libelle={}", libelle);

        // Validation
        if (libelle == null || libelle.trim().isEmpty()) {
            throw new IllegalArgumentException("Le libellé du menu ne peut pas être vide");
        }

        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("La description du menu ne peut pas être vide");
        }

        // VALIDATION IMAGE OBLIGATOIRE
        if (imagePublicId == null || imagePublicId.trim().isEmpty()) {
            throw new IllegalArgumentException("L'image est obligatoire pour créer un menu");
        }

        // Génération automatique du code
        String code = codeGenerator.genererCodeMenu();

        // Création
        Menu menu = new Menu(code, libelle.trim(), imagePublicId.trim(), description.trim());
        menu = (Menu) articleRepository.save(menu);

        logger.info("Menu créé avec succès: ID={}, code={}, libelle={}",
                menu.getId(), menu.getCode(), menu.getLibelle());
        return menu;
    }

    @Override
    public Article modifierArticle(Long id, String nouveauLibelle, String nouveauImagePublicId) {
        logger.info("Modification de l'article ID={}", id);

        if (id == null) {
            throw new IllegalArgumentException("L'ID ne peut pas être null");
        }

        // Récupérer l'article
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Article non trouvé avec l'ID:  " + id));

        // Modifier le libellé si fourni
        if (nouveauLibelle != null && !nouveauLibelle.trim().isEmpty()) {
            article.modifierLibelle(nouveauLibelle.trim());
        }

        // Modifier l'image si fourni (ne peut pas être vide car obligatoire)
        if (nouveauImagePublicId != null) {
            if (nouveauImagePublicId.trim().isEmpty()) {
                throw new IllegalArgumentException("L'image ne peut pas être vide");
            }
            article.setImagePublicId(nouveauImagePublicId.trim());
        }

        // Sauvegarder
        article = articleRepository.save(article);

        logger.info("Article modifié avec succès: ID={}, code={}", article.getId(), article.getCode());
        return article;
    }

    @Override
    public Article archiverArticle(Long id) {
        logger.info("Archivage de l'article ID={}", id);

        if (id == null) {
            throw new IllegalArgumentException("L'ID ne peut pas être null");
        }

        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Article non trouvé avec l'ID: " + id));

        article.archiver();
        article = articleRepository.save(article);

        logger.info("Article archivé avec succès:   ID={}", article.getId());
        return article;
    }

    @Override
    public Article restaurerArticle(Long id) {
        logger.info("Restauration de l'article ID={}", id);

        if (id == null) {
            throw new IllegalArgumentException("L'ID ne peut pas être null");
        }

        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Article non trouvé avec l'ID: " + id));

        article.restaurer();
        article = articleRepository.save(article);

        logger.info("Article restauré avec succès:  ID={}", article.getId());
        return article;
    }

    @Override
    public void supprimerArticle(Long id) {
        logger.info("Suppression de l'article ID={}", id);

        if (id == null) {
            throw new IllegalArgumentException("L'ID ne peut pas être null");
        }

        // Vérifier l'existence
        if (! articleRepository.findById(id).isPresent()) {
            throw new IllegalArgumentException("Article non trouvé avec l'ID: " + id);
        }

        articleRepository.delete(id);
        logger.info("Article supprimé avec succès: ID={}", id);
    }

    @Override
    public Optional<Article> obtenirArticleParId(Long id) {
        logger.debug("Recherche de l'article par ID:  {}", id);
        return articleRepository.findById(id);
    }

    @Override
    public Optional<Article> obtenirArticleParCode(String code) {
        logger.debug("Recherche de l'article par code: {}", code);
        return articleRepository.findByCode(code);
    }

    @Override
    public List<Article> listerTousLesArticles() {
        logger.debug("Liste de tous les articles");
        return articleRepository.findAll();
    }

    @Override
    public List<Article> listerArticlesParCategorie(CategorieArticle categorie) {
        logger.debug("Liste des articles par catégorie:  {}", categorie);

        if (categorie == null) {
            throw new IllegalArgumentException("La catégorie ne peut pas être null");
        }

        return articleRepository.findByCategorie(categorie);
    }

    @Override
    public List<Article> listerArticlesDisponibles() {
        logger.debug("Liste des articles disponibles");
        return articleRepository.findByEstArchiver(false);
    }

    @Override
    public List<Article> listerArticlesDisponiblesParCategorie(CategorieArticle categorie) {
        logger.debug("Liste des articles disponibles par catégorie:  {}", categorie);

        if (categorie == null) {
            throw new IllegalArgumentException("La catégorie ne peut pas être null");
        }

        return articleRepository.findAvailableByCategorie(categorie);
    }

    @Override
    public List<Burger> listerBurgersDisponibles() {
        logger.debug("Liste des burgers disponibles");
        return articleRepository.findAvailableByCategorie(CategorieArticle.BURGER)
                .stream()
                .map(a -> (Burger) a)
                .collect(Collectors.toList());
    }

    @Override
    public List<Menu> listerMenusDisponibles() {
        logger.debug("Liste des menus disponibles");
        return articleRepository.findAvailableByCategorie(CategorieArticle.MENU)
                .stream()
                .map(a -> (Menu) a)
                .collect(Collectors.toList());
    }

    @Override
    public List<Complement> listerComplementsDisponibles() {
        logger.debug("Liste des compléments disponibles");
        return articleRepository.findAvailableByCategorie(CategorieArticle.COMPLEMENT)
                .stream()
                .map(a -> (Complement) a)
                .collect(Collectors.toList());
    }

    @Override
    public List<Complement> listerComplementsParType(TypeComplement type) {
        logger.debug("Liste des compléments par type: {}", type);

        if (type == null) {
            throw new IllegalArgumentException("Le type ne peut pas être null");
        }

        return listerComplementsDisponibles()
                .stream()
                .filter(c -> c.getType() == type)
                .collect(Collectors.toList());
    }

    @Override
    public long compterArticles() {
        return articleRepository.count();
    }

    @Override
    public long compterArticlesParCategorie(CategorieArticle categorie) {
        if (categorie == null) {
            throw new IllegalArgumentException("La catégorie ne peut pas être null");
        }

        return articleRepository.countByCategorie(categorie);
    }

    @Override
    public boolean articleExiste(String code) {
        return articleRepository.existsByCode(code);
    }

    // ===================================
    // NOUVELLES MÉTHODES - Gestion des composants de Menu
    // ===================================

    @Override
    public Menu ajouterComposantAuMenu(Long menuId, Long articleId, Integer quantite) {
        logger.info("Ajout composant au menu:   menuId={}, articleId={}, quantite={}", menuId, articleId, quantite);

        // Validations
        if (menuId == null) {
            throw new IllegalArgumentException("L'ID du menu ne peut pas être null");
        }
        if (articleId == null) {
            throw new IllegalArgumentException("L'ID de l'article ne peut pas être null");
        }
        if (quantite == null || quantite <= 0) {
            throw new IllegalArgumentException("La quantité doit être supérieure à 0");
        }

        // Récupérer le menu
        Menu menu = (Menu) articleRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("Menu non trouvé avec l'ID: " + menuId));

        // Récupérer l'article
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("Article non trouvé avec l'ID: " + articleId));

        // Vérifier que l'article n'est pas un menu
        if (article instanceof Menu) {
            throw new IllegalArgumentException("Un menu ne peut pas contenir un autre menu");
        }

        // Vérifier que l'article est un Burger ou un Complément
        if (!(article instanceof Burger) && !(article instanceof Complement)) {
            throw new IllegalArgumentException("Seuls les burgers et compléments peuvent être ajoutés à un menu");
        }

        // Créer l'ArticleQuantifier
        ArticleQuantifier aq = new ArticleQuantifier();
        aq.setArticle(article);
        aq.setIdArticle(articleId);
        aq.setQuantite(quantite);
        aq.setIdMenu(menuId);
        aq.setCategorieArticleQuantifier(CategorieArticleQuantifier.MENU);

        // Calculer le montant
        Integer prixUnitaire = article.getPrix();
        aq.setMontant(prixUnitaire * quantite);

        // Sauvegarder en base
        articleQuantifierRepository.save(aq);

        // Ajouter au menu (en mémoire)
        menu.ajouterArticle(aq);

        logger.info("Composant ajouté au menu:  {}", aq);
        return menu;
    }

    @Override
    public Menu retirerComposantDuMenu(Long menuId, Long articleQuantifierId) {
        logger.info("Retrait composant du menu:  menuId={}, aqId={}", menuId, articleQuantifierId);

        if (menuId == null) {
            throw new IllegalArgumentException("L'ID du menu ne peut pas être null");
        }
        if (articleQuantifierId == null) {
            throw new IllegalArgumentException("L'ID de l'ArticleQuantifier ne peut pas être null");
        }

        // Récupérer le menu
        Menu menu = (Menu) articleRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("Menu non trouvé avec l'ID: " + menuId));

        // Vérifier que l'ArticleQuantifier existe
        ArticleQuantifier aq = articleQuantifierRepository.findById(articleQuantifierId)
                .orElseThrow(() -> new IllegalArgumentException("ArticleQuantifier non trouvé avec l'ID: " + articleQuantifierId));

        // Vérifier qu'il appartient bien au menu
        if (!menuId.equals(aq.getIdMenu())) {
            throw new IllegalArgumentException("Cet ArticleQuantifier n'appartient pas à ce menu");
        }

        // Supprimer de la base
        articleQuantifierRepository.delete(articleQuantifierId);

        // Retirer du menu (en mémoire)
        menu.retirerArticleParId(articleQuantifierId);

        logger.info("Composant retiré du menu");
        return menu;
    }

    @Override
    public ArticleQuantifier modifierQuantiteComposant(Long articleQuantifierId, Integer nouvelleQuantite) {
        logger.info("Modification quantité composant: aqId={}, quantite={}", articleQuantifierId, nouvelleQuantite);

        if (articleQuantifierId == null) {
            throw new IllegalArgumentException("L'ID de l'ArticleQuantifier ne peut pas être null");
        }
        if (nouvelleQuantite == null || nouvelleQuantite <= 0) {
            throw new IllegalArgumentException("La quantité doit être supérieure à 0");
        }

        // Récupérer l'ArticleQuantifier
        ArticleQuantifier aq = articleQuantifierRepository.findById(articleQuantifierId)
                .orElseThrow(() -> new IllegalArgumentException("ArticleQuantifier non trouvé avec l'ID:  " + articleQuantifierId));

        // Modifier la quantité (recalcule automatiquement le montant)
        aq.setQuantite(nouvelleQuantite);

        // Sauvegarder
        articleQuantifierRepository.save(aq);

        logger.info("Quantité modifiée:   nouveau montant={}", aq.getMontant());
        return aq;
    }

    @Override
    public List<ArticleQuantifier> obtenirComposantsMenu(Long menuId) {
        logger.debug("Récupération composants menu: menuId={}", menuId);

        if (menuId == null) {
            throw new IllegalArgumentException("L'ID du menu ne peut pas être null");
        }

        return articleQuantifierRepository.findByMenuId(menuId);
    }

    @Override
    public Integer calculerPrixMenu(Long menuId) {
        logger.debug("Calcul prix menu: menuId={}", menuId);

        if (menuId == null) {
            return 0;
        }

        List<ArticleQuantifier> composants = articleQuantifierRepository.findByMenuId(menuId);

        return composants.stream()
                .filter(aq -> aq.getArticle() != null)
                .filter(aq -> aq.getArticle().estDisponible())  // Exclure archivés
                .mapToInt(ArticleQuantifier::getMontant)
                .sum();
    }

    @Override
    public Optional<Menu> chargerMenuAvecComposants(Long menuId) {
        logger.debug("Chargement menu avec composants: menuId={}", menuId);

        if (menuId == null) {
            return Optional.empty();
        }

        // Récupérer le menu
        Optional<Article> articleOpt = articleRepository.findById(menuId);

        if (!articleOpt.isPresent() || !(articleOpt.get() instanceof Menu)) {
            return Optional.empty();
        }

        Menu menu = (Menu) articleOpt.get();

        // Charger les composants
        List<ArticleQuantifier> composants = articleQuantifierRepository.findByMenuId(menuId);
        menu.setArticles(composants);

        logger.debug("Menu chargé avec {} composants", composants.size());
        return Optional.of(menu);
    }

    @Override
    public int viderComposantsMenu(Long menuId) {
        logger.info("Vidage composants menu: menuId={}", menuId);

        if (menuId == null) {
            throw new IllegalArgumentException("L'ID du menu ne peut pas être null");
        }

        int count = articleQuantifierRepository.deleteByMenuId(menuId);
        logger.info("{} composants supprimés", count);
        return count;
    }
}