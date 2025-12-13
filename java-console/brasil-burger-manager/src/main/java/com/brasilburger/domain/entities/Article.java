package com.brasilburger.domain.entities;

import com.brasilburger.domain.entities.enums.CategorieArticle;
import com.brasilburger.domain.valueobjects.ImageInfo;

import java.util.Objects;

/**
 * Classe abstraite Article
 * Classe de base pour Burger, Menu et Complement
 */
public abstract class Article {
    private Long id;
    private String code;
    private String libelle;
    private String imagePublicId;  // Stocke le public_id Cloudinary
    private boolean estArchiver;
    private CategorieArticle categorie;

    /**
     * Constructeur par defaut
     */
    public Article() {
        this.estArchiver = false;
    }

    /**
     * Constructeur avec parametres
     */
    public Article(String code, String libelle, String imagePublicId) {
        this();
        this.code = code;
        this.libelle = libelle;
        this.imagePublicId = imagePublicId;
    }

    // ===================================
    // Methodes abstraites (a implementer par les sous-classes)
    // ===================================

    /**
     * Retourne le prix de l'article
     * (implementé différemment selon le type)
     */
    public abstract Integer getPrix();

    // ===================================
    // Methodes metier communes
    // ===================================

    /**
     * Archive l'article
     */
    public void archiver() {
        this.estArchiver = true;
    }

    /**
     * Restaure l'article
     */
    public void restaurer() {
        this.estArchiver = false;
    }

    /**
     * Verifie si l'article est disponible (non archive)
     */
    public boolean estDisponible() {
        return !estArchiver;
    }

    /**
     * Change l'image de l'article
     */
    public void changerImage(String nouveauPublicId) {
        if (nouveauPublicId == null || nouveauPublicId.trim().isEmpty()) {
            throw new IllegalArgumentException("Le publicId ne peut pas etre vide");
        }
        this.imagePublicId = nouveauPublicId.trim();
    }

    /**
     * Modifie le libelle
     */
    public void modifierLibelle(String nouveauLibelle) {
        if (nouveauLibelle == null || nouveauLibelle.trim().isEmpty()) {
            throw new IllegalArgumentException("Le libelle ne peut pas etre vide");
        }
        this.libelle = nouveauLibelle.trim();
    }

    /**
     * Verifie si c'est un burger
     */
    public boolean estBurger() {
        return categorie == CategorieArticle.BURGER;
    }

    /**
     * Verifie si c'est un menu
     */
    public boolean estMenu() {
        return categorie == CategorieArticle.MENU;
    }

    /**
     * Verifie si c'est un complement
     */
    public boolean estComplement() {
        return categorie == CategorieArticle.COMPLEMENT;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getImagePublicId() {
        return imagePublicId;
    }

    public void setImagePublicId(String imagePublicId) {
        this.imagePublicId = imagePublicId;
    }

    public boolean isEstArchiver() {
        return estArchiver;
    }

    public void setEstArchiver(boolean estArchiver) {
        this.estArchiver = estArchiver;
    }

    public CategorieArticle getCategorie() {
        return categorie;
    }

    protected void setCategorie(CategorieArticle categorie) {
        this.categorie = categorie;
    }

    // ===================================
    // equals, hashCode, toString
    // ===================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return Objects.equals(id, article.id) && Objects.equals(code, article.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", libelle='" + libelle + '\'' +
                ", categorie=" + categorie +
                ", prix=" + getPrix() +
                ", estArchiver=" + estArchiver +
                '}';
    }
}