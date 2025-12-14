package com.brasilburger.presentation.validators;

import com.brasilburger.domain.entities.Article;
import com.brasilburger.domain.entities.Burger;
import com.brasilburger.domain.entities.Complement;
import com.brasilburger.domain.entities.Menu;
import com.brasilburger.presentation.console.utils.ValidationHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Validateur pour l'entité Article et ses sous-types
 * Centralise toutes les règles de validation métier pour les articles
 */
public class ArticleValidator {

    /**
     * Valide un article complet (délègue aux validateurs spécifiques)
     * @param article Article à valider
     * @return Liste des erreurs de validation (vide si valide)
     */
    public static List<String> valider(Article article) {
        if (article == null) {
            List<String> erreurs = new ArrayList<>();
            erreurs.add("L'article ne peut pas être null");
            return erreurs;
        }

        // Validation commune
        List<String> erreurs = validerCommun(article);

        // Validation spécifique selon le type
        if (article instanceof Burger) {
            erreurs.addAll(validerBurger((Burger) article));
        } else if (article instanceof Menu) {
            erreurs.addAll(validerMenu((Menu) article));
        } else if (article instanceof Complement) {
            erreurs.addAll(validerComplement((Complement) article));
        }

        return erreurs;
    }

    /**
     * Valide les attributs communs à tous les articles
     */
    private static List<String> validerCommun(Article article) {
        List<String> erreurs = new ArrayList<>();

        // Validation du code
        erreurs.addAll(validerCode(article.getCode()));

        // Validation du libellé
        erreurs.addAll(validerLibelle(article.getLibelle()));

        // Validation de l'image (obligatoire)
        erreurs.addAll(validerImagePublicId(article.getImagePublicId()));

        return erreurs;
    }

    /**
     * Valide un burger
     */
    public static List<String> validerBurger(Burger burger) {
        List<String> erreurs = new ArrayList<>();

        if (burger == null) {
            erreurs.add("Le burger ne peut pas être null");
            return erreurs;
        }

        // Validation de la description
        erreurs.addAll(validerDescription(burger.getDescription()));

        // Validation du prix
        erreurs.addAll(validerPrix(burger.getPrix()));

        return erreurs;
    }

    /**
     * Valide un menu
     */
    public static List<String> validerMenu(Menu menu) {
        List<String> erreurs = new ArrayList<>();

        if (menu == null) {
            erreurs.add("Le menu ne peut pas être null");
            return erreurs;
        }

        // Validation de la description
        erreurs.addAll(validerDescription(menu.getDescription()));

        return erreurs;
    }

    /**
     * Valide un complément
     */
    public static List<String> validerComplement(Complement complement) {
        List<String> erreurs = new ArrayList<>();

        if (complement == null) {
            erreurs.add("Le complément ne peut pas être null");
            return erreurs;
        }

        // Validation du type
        if (complement.getType() == null) {
            erreurs.add("Le type du complément ne peut pas être null");
        }

        // Validation du prix
        erreurs.addAll(validerPrix(complement.getPrix()));

        return erreurs;
    }

    /**
     * Valide le code d'un article
     */
    public static List<String> validerCode(String code) {
        List<String> erreurs = new ArrayList<>();

        if (code == null || code.trim().isEmpty()) {
            erreurs.add("Le code de l'article ne peut pas être vide");
            return erreurs;
        }

        String codeTrim = code.trim();

        if (! ValidationHelper.estCodeArticleValide(codeTrim)) {
            erreurs.add("Le code de l'article doit être au format BRG001, MNU001 ou CMP001");
        }

        return erreurs;
    }

    /**
     * Valide le libellé d'un article
     */
    public static List<String> validerLibelle(String libelle) {
        List<String> erreurs = new ArrayList<>();

        if (libelle == null || libelle.trim().isEmpty()) {
            erreurs.add("Le libellé de l'article ne peut pas être vide");
            return erreurs;
        }

        String libelleTrim = libelle.trim();

        if (libelleTrim.length() < 3) {
            erreurs.add("Le libellé de l'article doit contenir au moins 3 caractères");
        }

        if (libelleTrim.length() > 150) {
            erreurs.add("Le libellé de l'article ne peut pas dépasser 150 caractères");
        }

        return erreurs;
    }

    /**
     * Valide la description d'un article
     */
    public static List<String> validerDescription(String description) {
        List<String> erreurs = new ArrayList<>();

        if (description == null || description.trim().isEmpty()) {
            erreurs.add("La description ne peut pas être vide");
            return erreurs;
        }

        String descriptionTrim = description.trim();

        if (descriptionTrim.length() < 5) {
            erreurs.add("La description doit contenir au moins 5 caractères");
        }

        if (descriptionTrim.length() > 500) {
            erreurs.add("La description ne peut pas dépasser 500 caractères");
        }

        return erreurs;
    }

    /**
     * Valide le prix d'un article
     */
    public static List<String> validerPrix(Integer prix) {
        List<String> erreurs = new ArrayList<>();

        if (prix == null) {
            erreurs.add("Le prix ne peut pas être null");
            return erreurs;
        }

        if (! ValidationHelper.estPrixValide(prix)) {
            erreurs.add(ValidationHelper.getMessageErreurPrix());
        }

        return erreurs;
    }

    /**
     * Valide l'image public ID (obligatoire)
     */
    public static List<String> validerImagePublicId(String imagePublicId) {
        List<String> erreurs = new ArrayList<>();

        if (imagePublicId == null || imagePublicId.trim().isEmpty()) {
            erreurs.add("L'image est obligatoire pour un article");
            return erreurs;
        }

        String imageTrim = imagePublicId.trim();

        if (imageTrim.length() < 5) {
            erreurs.add("L'ID de l'image doit contenir au moins 5 caractères");
        }

        if (imageTrim.length() > 255) {
            erreurs.add("L'ID de l'image ne peut pas dépasser 255 caractères");
        }

        return erreurs;
    }

    /**
     * Vérifie si un article est valide
     */
    public static boolean estValide(Article article) {
        return valider(article).isEmpty();
    }

    /**
     * Lance une exception si l'article est invalide
     */
    public static void validerOuLancerException(Article article) {
        List<String> erreurs = valider(article);
        if (!erreurs.isEmpty()) {
            throw new IllegalArgumentException("Article invalide: " + String.join(", ", erreurs));
        }
    }

    /**
     * Empêche l'instantiation
     */
    private ArticleValidator() {
        throw new UnsupportedOperationException("Classe utilitaire - pas d'instantiation");
    }
}