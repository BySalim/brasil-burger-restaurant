package com.brasilburger.domain.entities;

import com.brasilburger.domain.entities.enums.CategorieArticle;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Entite Menu
 * Le prix d'un menu est calcule via les ArticleQuantifier
 * IMPORTANT : Les articles archivés ne sont pas comptés dans le prix
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
     * Le prix d'un menu est la somme des prix de ses composants DISPONIBLES
     * IMPORTANT : Les articles archivés sont exclus du calcul
     */
    @Override
    public Integer getPrix() {
        if (articles == null || articles.isEmpty()) {
            return 0;
        }

        return articles.stream()
                .filter(aq -> aq != null)                          // ArticleQuantifier non null
                .filter(aq -> aq.getArticle() != null)             // Article chargé
                .filter(aq -> aq.getArticle().estDisponible())     // Article NON archivé
                .mapToInt(ArticleQuantifier:: getMontant)
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

        // Vérifier que l'article n'est pas un menu
        if (articleQuantifier.getArticle() != null &&
                articleQuantifier.getArticle() instanceof Menu) {
            throw new IllegalArgumentException("Un menu ne peut pas contenir un autre menu");
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
     * Retire un article du menu par son ID
     */
    public boolean retirerArticleParId(Long articleQuantifierId) {
        if (articleQuantifierId == null) {
            return false;
        }
        return this.articles.removeIf(aq -> articleQuantifierId.equals(aq.getId()));
    }

    /**
     * Vide tous les composants du menu
     */
    public void viderComposants() {
        this.articles.clear();
    }

    /**
     * Obtient le nombre d'articles dans le menu
     */
    public int getNombreArticles() {
        return articles != null ? articles.size() : 0;
    }

    /**
     * Obtient le nombre d'articles DISPONIBLES dans le menu
     */
    public int getNombreArticlesDisponibles() {
        if (articles == null) {
            return 0;
        }
        return (int) articles.stream()
                .filter(aq -> aq != null)
                .filter(aq -> aq.getArticle() != null)
                .filter(aq -> aq.getArticle().estDisponible())
                .count();
    }

    /**
     * Verifie si le menu contient des articles
     */
    public boolean aDesArticles() {
        return articles != null && !articles.isEmpty();
    }

    /**
     * Vérifie si le menu contient des articles disponibles
     */
    public boolean aDesArticlesDisponibles() {
        return getNombreArticlesDisponibles() > 0;
    }

    /**
     * Obtient uniquement les composants avec articles disponibles
     */
    public List<ArticleQuantifier> getArticlesDisponibles() {
        if (articles == null) {
            return new ArrayList<>();
        }
        return articles.stream()
                .filter(aq -> aq != null)
                .filter(aq -> aq.getArticle() != null)
                .filter(aq -> aq.getArticle().estDisponible())
                .collect(Collectors.toList());
    }

    /**
     * Obtient uniquement les composants avec articles archivés
     */
    public List<ArticleQuantifier> getArticlesArchives() {
        if (articles == null) {
            return new ArrayList<>();
        }
        return articles.stream()
                .filter(aq -> aq != null)
                .filter(aq -> aq.getArticle() != null)
                .filter(aq -> aq.getArticle().isEstArchiver())
                .collect(Collectors.toList());
    }

    /**
     * Trouve un ArticleQuantifier par son ID
     */
    public ArticleQuantifier trouverComposantParId(Long id) {
        if (articles == null || id == null) {
            return null;
        }
        return articles.stream()
                .filter(aq -> id.equals(aq.getId()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Vérifie si le menu est valide (a au moins un article disponible)
     */
    public boolean estValide() {
        return aDesArticlesDisponibles();
    }

    /**
     * Obtient un résumé textuel du menu
     */
    public String getResume() {
        StringBuilder sb = new StringBuilder();
        sb.append(getLibelle()).append(" (").append(getPrix()).append(" FCFA)\n");
        sb.append("Composants :  ").append(getNombreArticlesDisponibles());

        int nbArchives = articles != null ? articles.size() - getNombreArticlesDisponibles() : 0;
        if (nbArchives > 0) {
            sb.append(" + ").append(nbArchives).append(" archivé(s)");
        }

        return sb.toString();
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
        return articles != null ? new ArrayList<>(articles) : new ArrayList<>();
    }

    public void setArticles(List<ArticleQuantifier> articles) {
        this.articles = articles != null ? new ArrayList<>(articles) : new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + getId() +
                ", code='" + getCode() + '\'' +
                ", libelle='" + getLibelle() + '\'' +
                ", description='" + description + '\'' +
                ", nbComposants=" + getNombreArticles() +
                ", nbDisponibles=" + getNombreArticlesDisponibles() +
                ", prix=" + getPrix() + " FCFA" +
                ", estArchiver=" + isEstArchiver() +
                '}';
    }
}