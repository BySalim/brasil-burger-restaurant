package com.brasilburger.infrastructure.database.mappers;

import com.brasilburger.domain.entities.Article;
import com.brasilburger.domain.entities.Burger;
import com.brasilburger.domain.entities.Complement;
import com.brasilburger.domain.entities.Menu;
import com.brasilburger.domain.entities.enums.CategorieArticle;
import com.brasilburger.domain.entities.enums.TypeComplement;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Mapper pour convertir entre ResultSet SQL et entites Article
 * Gere le polymorphisme (Burger, Menu, Complement)
 * STRUCTURE:  Une seule table article avec tous les champs
 */
public class ArticleMapper {

    /**
     * Convertit un ResultSet en entite Article (avec polymorphisme)
     */
    public static Article mapResultSetToArticle(ResultSet rs) throws SQLException {
        // Lire la categorie pour determiner le type
        String categorieStr = rs.getString("categorie");
        CategorieArticle categorie = CategorieArticle.valueOf(categorieStr);

        Article article;

        // Creer l'instance appropriee selon la categorie
        switch (categorie) {
            case BURGER:
                article = mapToBurger(rs);
                break;
            case MENU:
                article = mapToMenu(rs);
                break;
            case COMPLEMENT:
                article = mapToComplement(rs);
                break;
            default:
                throw new IllegalArgumentException("Categorie inconnue: " + categorie);
        }

        // Mapper les attributs communs
        article.setId(rs.getLong("id"));
        article.setCode(rs.getString("code"));
        article.setLibelle(rs.getString("libelle"));
        article.setImagePublicId(rs.getString("image_public_id"));
        article.setEstArchiver(rs.getBoolean("est_archiver"));

        return article;
    }

    /**
     * Mappe vers un Burger
     */
    private static Burger mapToBurger(ResultSet rs) throws SQLException {
        Burger burger = new Burger();

        // Attributs specifiques Burger (depuis la table article)
        String description = rs.getString("description");
        Integer prix = (Integer) rs.getObject("prix");

        burger.setDescription(description);
        if (prix != null) {
            burger.setPrix(prix);
        }

        return burger;
    }

    /**
     * Mappe vers un Menu
     */
    private static Menu mapToMenu(ResultSet rs) throws SQLException {
        Menu menu = new Menu();

        // Attributs specifiques Menu (depuis la table article)
        String description = rs.getString("description");
        menu.setDescription(description);

        // Note: Les articles du menu seront charges separement via ArticleQuantifier

        return menu;
    }

    /**
     * Mappe vers un Complement
     */
    private static Complement mapToComplement(ResultSet rs) throws SQLException {
        Complement complement = new Complement();

        // Attributs specifiques Complement (depuis la table article)
        String typeStr = rs.getString("type_complement");
        Integer prix = (Integer) rs.getObject("prix");

        if (typeStr != null) {
            complement.setType(TypeComplement.valueOf(typeStr));
        }

        if (prix != null) {
            complement.setPrix(prix);
        }

        return complement;
    }

    /**
     * Empeche l'instantiation
     */
    private ArticleMapper() {
        throw new UnsupportedOperationException("Classe utilitaire - pas d'instantiation");
    }
}