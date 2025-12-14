package com.brasilburger.domain.services.impl;

import com.brasilburger.domain.entities.*;
import com.brasilburger.domain.entities.enums.CategorieArticle;
import com.brasilburger.domain.entities.enums.TypeComplement;
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

    public ArticleServiceImpl(IArticleRepository articleRepository, ICodeArticleGenerator codeGenerator) {
        this.articleRepository = articleRepository;
        this.codeGenerator = codeGenerator;
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

        logger.info("Article archivé avec succès:  ID={}", article.getId());
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
        logger.debug("Liste des articles par catégorie: {}", categorie);

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
}