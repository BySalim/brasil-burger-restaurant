package com.brasilburger.domain.entities;

import com.brasilburger.domain.entities.enums.CategorieArticle;

import java.util.ArrayList;
import java.util.List;

/**
 * Entite Menu
 * Le prix d'un menu est calcule via les ArticleQuantifier
 */
public class Menu extends Article {
    private String description;
    private List<ArticleQuantifier> articles;

    /**
     * Constructeur par defaut
     */
    public Menu() {
        super();
        this.setCategorie(CategorieArticle.MENU);
        this.articles = new ArrayList<>();
    }

    /**
     * Constructeur avec parametres
     */
    public Menu(String code, String libelle, String imagePublicId, String description) {
        super(code, libelle, imagePublicId);
        this.setCategorie(CategorieArticle.MENU);
        this.description = description;
        this.articles = new ArrayList<>();
    }

    // ===================================
    // Implementation de la methode abstraite
    // ===================================

    /**
     * Le prix d'un menu est la somme des prix de ses composants
     */
    @Override
    public Integer getPrix() {
        return articles.stream()
                .mapToInt(ArticleQuantifier::getMontant)
                .sum();
    }

    // ===================================
    // Methodes metier
    // ===================================

    /**
     * Ajoute un article au menu via ArticleQuantifier
     */
    public void ajouterArticle(ArticleQuantifier articleQuantifier) {
        if (articleQuantifier == null) {
            throw new IllegalArgumentException("ArticleQuantifier ne peut pas etre null");
        }
        this.articles.add(articleQuantifier);
    }

    /**
     * Retire un article du menu
     */
    public void retirerArticle(ArticleQuantifier articleQuantifier) {
        this.articles.remove(articleQuantifier);
    }

    /**
     * Obtient le nombre d'articles dans le menu
     */
    public int getNombreArticles() {
        return articles.size();
    }

    /**
     * Verifie si le menu contient des articles
     */
    public boolean aDesArticles() {
        return !articles.isEmpty();
    }

    // ===================================
    // Getters et Setters
    // ===================================

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ArticleQuantifier> getArticles() {
        return new ArrayList<>(articles);
    }

    public void setArticles(List<ArticleQuantifier> articles) {
        this.articles = articles != null ? new ArrayList<>(articles) : new ArrayList<>();
    }
}