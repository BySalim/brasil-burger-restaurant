package com.brasilburger.domain.services.impl;

import com.brasilburger.domain.entities.enums.CategorieArticle;
import com.brasilburger.domain.repositories.IArticleRepository;
import com.brasilburger.domain.services.ICodeArticleGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Implementation du generateur de codes uniques pour les articles
 * Format: [PREFIX][NUMERO] (ex: BRG001, MNU001, CMP001)
 */
public class CodeArticleGeneratorImpl implements ICodeArticleGenerator {
    private static final Logger logger = LoggerFactory.getLogger(CodeArticleGeneratorImpl.class);

    private final IArticleRepository articleRepository;

    // Prefixes par categorie
    private static final String PREFIX_BURGER = "BRG";
    private static final String PREFIX_MENU = "MNU";
    private static final String PREFIX_COMPLEMENT = "CMP";

    // Compteurs en memoire (pour optimisation)
    private final AtomicInteger burgerCounter = new AtomicInteger(0);
    private final AtomicInteger menuCounter = new AtomicInteger(0);
    private final AtomicInteger complementCounter = new AtomicInteger(0);

    public CodeArticleGeneratorImpl(IArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
        initialiserCompteurs();
    }

    /**
     * Initialise les compteurs en recuperant le dernier code utilise
     */
    private void initialiserCompteurs() {
        try {
            burgerCounter.set(obtenirDernierNumero(PREFIX_BURGER));
            menuCounter.set(obtenirDernierNumero(PREFIX_MENU));
            complementCounter.set(obtenirDernierNumero(PREFIX_COMPLEMENT));

            logger.info("Compteurs initialises - Burgers: {}, Menus: {}, Complements: {}",
                    burgerCounter.get(), menuCounter.get(), complementCounter.get());
        } catch (Exception e) {
            logger.warn("Impossible d'initialiser les compteurs depuis la BD, demarrage a 0", e);
        }
    }

    @Override
    public String genererCode(CategorieArticle categorie) {
        if (categorie == null) {
            throw new IllegalArgumentException("La categorie ne peut pas etre null");
        }

        String code;
        switch (categorie) {
            case BURGER:
                code = genererCodeAvecPrefix(PREFIX_BURGER, burgerCounter);
                break;
            case MENU:
                code = genererCodeAvecPrefix(PREFIX_MENU, menuCounter);
                break;
            case COMPLEMENT:
                code = genererCodeAvecPrefix(PREFIX_COMPLEMENT, complementCounter);
                break;
            default:
                throw new IllegalArgumentException("Categorie inconnue: " + categorie);
        }

        // Verifier l'unicite dans la BD
        while (articleRepository.existsByCode(code)) {
            logger.warn("Code {} deja existant, generation d'un nouveau", code);
            code = genererCodeAvecPrefix(getPrefixForCategorie(categorie), getCounterForCategorie(categorie));
        }

        logger.info("Code genere:  {} pour categorie {}", code, categorie);
        return code;
    }

    @Override
    public String genererCodeBurger() {
        return genererCode(CategorieArticle.BURGER);
    }

    @Override
    public String genererCodeMenu() {
        return genererCode(CategorieArticle.MENU);
    }

    @Override
    public String genererCodeComplement() {
        return genererCode(CategorieArticle.COMPLEMENT);
    }

    @Override
    public void reinitialiserCompteurs() {
        burgerCounter.set(0);
        menuCounter.set(0);
        complementCounter.set(0);
        initialiserCompteurs();
        logger.info("Compteurs reinitialises");
    }

    /**
     * Genere un code avec prefix et compteur
     * Format: PREFIX + numero sur 3 chiffres (ex: BRG001)
     */
    private String genererCodeAvecPrefix(String prefix, AtomicInteger counter) {
        int numero = counter.incrementAndGet();
        return String.format("%s%03d", prefix, numero);
    }

    /**
     * Recupere le dernier numero utilise pour un prefix donne
     */
    private int obtenirDernierNumero(String prefix) {
        try {
            // Recuperer tous les codes commencant par ce prefix
            long count = articleRepository.count();

            if (count == 0) {
                return 0;
            }

            // Strategie simple:  compter les articles de cette categorie
            CategorieArticle categorie = getCategorieForPrefix(prefix);
            long categorieCount = articleRepository.countByCategorie(categorie);

            return (int) categorieCount;

        } catch (Exception e) {
            logger.error("Erreur lors de la recuperation du dernier numero pour prefix:  {}", prefix, e);
            return 0;
        }
    }

    /**
     * Retourne le prefix selon la categorie
     */
    private String getPrefixForCategorie(CategorieArticle categorie) {
        switch (categorie) {
            case BURGER:  return PREFIX_BURGER;
            case MENU: return PREFIX_MENU;
            case COMPLEMENT:  return PREFIX_COMPLEMENT;
            default: throw new IllegalArgumentException("Categorie inconnue");
        }
    }

    /**
     * Retourne le compteur selon la categorie
     */
    private AtomicInteger getCounterForCategorie(CategorieArticle categorie) {
        switch (categorie) {
            case BURGER: return burgerCounter;
            case MENU: return menuCounter;
            case COMPLEMENT: return complementCounter;
            default: throw new IllegalArgumentException("Categorie inconnue");
        }
    }

    /**
     * Retourne la categorie selon le prefix
     */
    private CategorieArticle getCategorieForPrefix(String prefix) {
        switch (prefix) {
            case PREFIX_BURGER: return CategorieArticle.BURGER;
            case PREFIX_MENU: return CategorieArticle.MENU;
            case PREFIX_COMPLEMENT: return CategorieArticle.COMPLEMENT;
            default: throw new IllegalArgumentException("Prefix inconnu: " + prefix);
        }
    }
}