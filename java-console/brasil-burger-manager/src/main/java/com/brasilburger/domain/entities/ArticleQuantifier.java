package com.brasilburger.domain.entities;

import com.brasilburger.domain.entities.enums.CategorieArticleQuantifier;

/**
 * Entite ArticleQuantifier
 * Représente une quantité d'article dans un menu ou un panier (commande)
 */
public class ArticleQuantifier {
    private Long id;
    private Integer quantite;
    private Integer montant;
    private CategorieArticleQuantifier categorieArticleQuantifier;
    private Long idMenu;      // NULL si c'est pour un panier
    private Long idPanier;    // NULL si c'est pour un menu
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
        this();
        this.quantite = quantite;
        this.montant = montant;
        this.idArticle = idArticle;
    }

    /**
     * Constructeur complet pour Menu
     */
    public ArticleQuantifier(Article article, Integer quantite, Long idMenu) {
        this();
        this.article = article;
        this.idArticle = article != null ? article.getId() : null;
        this.quantite = quantite;
        this.idMenu = idMenu;
        this.categorieArticleQuantifier = CategorieArticleQuantifier.MENU;

        // Calculer le montant automatiquement
        if (article != null && article.getPrix() != null) {
            calculerMontant(article.getPrix());
        }
    }

    /**
     * Constructeur complet pour Panier (Commande)
     */
    public ArticleQuantifier(Article article, Integer quantite, Long idPanier, boolean isCommande) {
        this();
        this.article = article;
        this.idArticle = article != null ? article.getId() : null;
        this.quantite = quantite;
        this.idPanier = idPanier;
        this.categorieArticleQuantifier = CategorieArticleQuantifier.COMMANDE;

        // Calculer le montant automatiquement
        if (article != null && article.getPrix() != null) {
            calculerMontant(article.getPrix());
        }
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
     * Recalcule le montant à partir de l'article associé
     */
    public void recalculerMontant() {
        if (article != null && article.getPrix() != null) {
            calculerMontant(article.getPrix());
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
        recalculerMontant();
    }

    /**
     * Diminue la quantité
     */
    public void diminuerQuantite(int decrement) {
        if (decrement <= 0) {
            throw new IllegalArgumentException("Le decrement doit etre positif");
        }
        if (this.quantite - decrement < 0) {
            throw new IllegalArgumentException("La quantite ne peut pas etre negative");
        }
        this.quantite -= decrement;
        recalculerMontant();
    }

    /**
     * Vérifie si l'article associé est disponible (non archivé)
     */
    public boolean estArticleDisponible() {
        return article != null && article.estDisponible();
    }

    /**
     * Vérifie si c'est un composant de menu
     */
    public boolean estComposantMenu() {
        return categorieArticleQuantifier == CategorieArticleQuantifier.MENU;
    }

    /**
     * Vérifie si c'est un élément de commande (panier)
     */
    public boolean estElementCommande() {
        return categorieArticleQuantifier == CategorieArticleQuantifier.COMMANDE;
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
        recalculerMontant();
    }

    public Integer getMontant() {
        return montant != null ? montant : 0;
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
            recalculerMontant();
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
                ", article=" + (article != null ? article.getLibelle() : "null") +
                ", categorie=" + categorieArticleQuantifier +
                '}';
    }
}