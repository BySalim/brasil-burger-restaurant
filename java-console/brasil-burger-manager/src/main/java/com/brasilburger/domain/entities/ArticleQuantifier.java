package com.brasilburger.domain.entities;

import com.brasilburger.domain.entities.enums.CategorieArticleQuantifier;

/**
 * Entite ArticleQuantifier
 */
public class ArticleQuantifier {
    private Long id;
    private Integer quantite;
    private Integer montant;
    private CategorieArticleQuantifier categorieArticleQuantifier;
    private Long idMenu;      // NULL si c'est pour une commande
    private Long idPanier;
    private Long idArticle;

    // Référence vers l'article (pour faciliter l'affichage)
    private Article article;

    /**
     * Constructeur par defaut
     */
    public ArticleQuantifier() {
        this.quantite = 1;
    }

    /**
     * Constructeur avec parametres
     */
    public ArticleQuantifier(Integer quantite, Integer montant, Long idArticle) {
        this. quantite = quantite;
        this.montant = montant;
        this.idArticle = idArticle;
    }

    // ===================================
    // Methodes metier
    // ===================================

    /**
     * Calcule le montant total (quantite * prix unitaire)
     */
    public void calculerMontant(Integer prixUnitaire) {
        if (prixUnitaire != null && quantite != null) {
            this.montant = prixUnitaire * quantite;
        }
    }

    /**
     * Augmente la quantite
     */
    public void augmenterQuantite(int increment) {
        if (increment <= 0) {
            throw new IllegalArgumentException("L'increment doit etre positif");
        }
        this.quantite += increment;
    }

    // ===================================
    // Getters et Setters
    // ===================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        if (quantite != null && quantite < 0) {
            throw new IllegalArgumentException("La quantite ne peut pas etre negative");
        }
        this.quantite = quantite;
    }

    public Integer getMontant() {
        return montant;
    }

    public void setMontant(Integer montant) {
        this.montant = montant;
    }

    public CategorieArticleQuantifier getCategorieArticleQuantifier() {
        return categorieArticleQuantifier;
    }

    public void setCategorieArticleQuantifier(CategorieArticleQuantifier categorieArticleQuantifier) {
        this.categorieArticleQuantifier = categorieArticleQuantifier;
    }

    public Long getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(Long idMenu) {
        this.idMenu = idMenu;
    }

    public Long getIdPanier() {
        return idPanier;
    }

    public void setIdPanier(Long idPanier) {
        this.idPanier = idPanier;
    }

    public Long getIdArticle() {
        return idArticle;
    }

    public void setIdArticle(Long idArticle) {
        this.idArticle = idArticle;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
        if (article != null) {
            this.idArticle = article.getId();
        }
    }

    public void setCategorie(CategorieArticleQuantifier categorie) {
        this.categorieArticleQuantifier = categorie;
    }

    @Override
    public String toString() {
        return "ArticleQuantifier{" +
                "id=" + id +
                ", quantite=" + quantite +
                ", montant=" + montant +
                ", idArticle=" + idArticle +
                '}';
    }
}