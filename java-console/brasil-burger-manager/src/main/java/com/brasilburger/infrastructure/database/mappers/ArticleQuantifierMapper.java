package com.brasilburger.infrastructure.database.mappers;

import com.brasilburger.domain.entities.Article;
import com.brasilburger.domain.entities.ArticleQuantifier;
import com.brasilburger.domain.entities.enums.CategorieArticleQuantifier;
import com.brasilburger.domain.repositories.IArticleRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Mapper pour convertir les ResultSet en ArticleQuantifier
 */
public class ArticleQuantifierMapper {

    /**
     * Mappe un ResultSet vers un ArticleQuantifier (sans charger l'article)
     * Utilisé quand on ne veut pas charger l'article associé
     */
    public static ArticleQuantifier mapResultSetToArticleQuantifier(ResultSet rs) throws SQLException {
        ArticleQuantifier aq = new ArticleQuantifier();

        aq.setId(rs.getLong("id"));
        aq.setQuantite(rs.getInt("quantite"));
        aq.setMontant(rs.getInt("montant"));

        // Catégorie
        String categorieStr = rs.getString("categorie_article_quantifier");
        if (categorieStr != null) {
            aq.setCategorieArticleQuantifier(CategorieArticleQuantifier.valueOf(categorieStr));
        }

        // IDs de référence
        long idMenu = rs.getLong("id_menu");
        aq.setIdMenu(rs.wasNull() ? null : idMenu);

        long idPanier = rs.getLong("id_panier");
        aq.setIdPanier(rs.wasNull() ? null : idPanier);

        long idArticle = rs.getLong("id_article");
        aq.setIdArticle(idArticle);

        return aq;
    }

    /**
     * Mappe un ResultSet vers un ArticleQuantifier en chargeant l'article associé
     * Utilisé quand on a besoin de l'article complet (prix, libellé, etc.)
     */
    public static ArticleQuantifier mapResultSetToArticleQuantifierWithArticle(
            ResultSet rs,
            IArticleRepository articleRepository) throws SQLException {

        ArticleQuantifier aq = mapResultSetToArticleQuantifier(rs);

        // Charger l'article associé
        if (aq.getIdArticle() != null) {
            Optional<Article> articleOpt = articleRepository.findById(aq.getIdArticle());
            articleOpt.ifPresent(aq::setArticle);
        }

        return aq;
    }

    /**
     * Mappe un ResultSet qui contient une jointure avec article
     * Colonne attendues : aq.*, a.* (avec alias)
     */
    public static ArticleQuantifier mapResultSetToArticleQuantifierWithJoin(ResultSet rs) throws SQLException {
        ArticleQuantifier aq = new ArticleQuantifier();

        // Colonnes ArticleQuantifier
        aq.setId(rs.getLong("aq_id"));
        aq.setQuantite(rs.getInt("aq_quantite"));
        aq.setMontant(rs.getInt("aq_montant"));

        String categorieStr = rs.getString("aq_categorie");
        if (categorieStr != null) {
            aq.setCategorieArticleQuantifier(CategorieArticleQuantifier.valueOf(categorieStr));
        }

        long idMenu = rs.getLong("aq_id_menu");
        aq.setIdMenu(rs.wasNull() ? null : idMenu);

        long idPanier = rs.getLong("aq_id_panier");
        aq.setIdPanier(rs.wasNull() ? null : idPanier);

        long idArticle = rs.getLong("aq_id_article");
        aq.setIdArticle(idArticle);

        // Colonnes Article (si présentes dans la jointure)
        try {
            Article article = ArticleMapper.mapResultSetToArticle(rs, "a_");
            aq.setArticle(article);
        } catch (SQLException e) {
            // Si les colonnes article ne sont pas présentes, on ignore
        }

        return aq;
    }
}