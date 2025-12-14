package com.brasilburger.domain.repositories.impl;

import com.brasilburger.domain.entities.ArticleQuantifier;
import com.brasilburger.domain.repositories.IArticleQuantifierRepository;
import com.brasilburger.domain.repositories.IArticleRepository;
import com.brasilburger.factories.RepositoryFactory;
import com.brasilburger.infrastructure.database.NeonConnectionManager;
import com.brasilburger.infrastructure.database.mappers.ArticleQuantifierMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation du repository ArticleQuantifier pour Neon (PostgreSQL)
 */
public class NeonArticleQuantifierRepository implements IArticleQuantifierRepository {

    private static final Logger logger = LoggerFactory.getLogger(NeonArticleQuantifierRepository.class);
    private final IArticleRepository articleRepository;

    // Requêtes SQL
    private static final String INSERT_AQ =
            "INSERT INTO article_quantifier (quantite, montant, categorie_article_quantifier, id_menu, id_panier, id_article) " +
                    "VALUES (?, ?, ? ::text, ?, ?, ?) RETURNING id";

    private static final String UPDATE_AQ =
            "UPDATE article_quantifier SET quantite = ?, montant = ?, categorie_article_quantifier = ? ::text, " +
                    "id_menu = ?, id_panier = ?, id_article = ? WHERE id = ?";

    private static final String FIND_BY_ID =
            "SELECT id, quantite, montant, categorie_article_quantifier, id_menu, id_panier, id_article " +
                    "FROM article_quantifier WHERE id = ?";

    private static final String FIND_BY_MENU_ID =
            "SELECT id, quantite, montant, categorie_article_quantifier, id_menu, id_panier, id_article " +
                    "FROM article_quantifier WHERE id_menu = ? ORDER BY id";

    private static final String FIND_BY_PANIER_ID =
            "SELECT id, quantite, montant, categorie_article_quantifier, id_menu, id_panier, id_article " +
                    "FROM article_quantifier WHERE id_panier = ? ORDER BY id";

    private static final String FIND_ALL =
            "SELECT id, quantite, montant, categorie_article_quantifier, id_menu, id_panier, id_article " +
                    "FROM article_quantifier ORDER BY id";

    private static final String DELETE_BY_ID =
            "DELETE FROM article_quantifier WHERE id = ?";

    private static final String DELETE_BY_MENU_ID =
            "DELETE FROM article_quantifier WHERE id_menu = ?";

    private static final String DELETE_BY_PANIER_ID =
            "DELETE FROM article_quantifier WHERE id_panier = ?";

    private static final String COUNT_BY_MENU_ID =
            "SELECT COUNT(*) FROM article_quantifier WHERE id_menu = ?";

    private static final String COUNT_BY_PANIER_ID =
            "SELECT COUNT(*) FROM article_quantifier WHERE id_panier = ?";

    public NeonArticleQuantifierRepository() {
        this.articleRepository = RepositoryFactory.createArticleRepository();
    }

    @Override
    public ArticleQuantifier save(ArticleQuantifier articleQuantifier) {
        if (articleQuantifier == null) {
            throw new IllegalArgumentException("ArticleQuantifier ne peut pas etre null");
        }

        if (articleQuantifier.getId() == null) {
            return insert(articleQuantifier);
        } else {
            return update(articleQuantifier);
        }
    }

    /**
     * Insère un nouvel ArticleQuantifier
     */
    private ArticleQuantifier insert(ArticleQuantifier aq) {
        try (Connection conn = NeonConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_AQ)) {

            setParameters(stmt, aq);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                aq.setId(rs.getLong("id"));
                logger.info("ArticleQuantifier cree:   ID={}, Article={}, Quantite={}",
                        aq.getId(), aq.getIdArticle(), aq.getQuantite());
            }

            return aq;

        } catch (SQLException e) {
            logger.error("Erreur lors de l'insertion de ArticleQuantifier", e);
            throw new RuntimeException("Erreur lors de la creation de ArticleQuantifier", e);
        }
    }

    /**
     * Met à jour un ArticleQuantifier existant
     */
    private ArticleQuantifier update(ArticleQuantifier aq) {
        try (Connection conn = NeonConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_AQ)) {

            setParameters(stmt, aq);
            stmt.setLong(7, aq.getId());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                logger.warn("Aucun ArticleQuantifier trouve avec l'ID:  {}", aq.getId());
            } else {
                logger.info("ArticleQuantifier mis a jour:  ID={}", aq.getId());
            }

            return aq;

        } catch (SQLException e) {
            logger.error("Erreur lors de la mise a jour de ArticleQuantifier:  ID={}", aq.getId(), e);
            throw new RuntimeException("Erreur lors de la mise a jour", e);
        }
    }

    /**
     * Définit les paramètres du PreparedStatement
     */
    private void setParameters(PreparedStatement stmt, ArticleQuantifier aq) throws SQLException {
        stmt.setInt(1, aq.getQuantite());
        stmt.setInt(2, aq.getMontant());
        stmt.setString(3, aq.getCategorieArticleQuantifier() != null ?
                aq.getCategorieArticleQuantifier().name() : null);

        // id_menu (peut être null)
        if (aq.getIdMenu() != null) {
            stmt.setLong(4, aq.getIdMenu());
        } else {
            stmt.setNull(4, Types.BIGINT);
        }

        // id_panier (peut être null)
        if (aq.getIdPanier() != null) {
            stmt.setLong(5, aq.getIdPanier());
        } else {
            stmt.setNull(5, Types.BIGINT);
        }

        stmt.setLong(6, aq.getIdArticle());
    }

    @Override
    public Optional<ArticleQuantifier> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }

        try (Connection conn = NeonConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_BY_ID)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                ArticleQuantifier aq = ArticleQuantifierMapper.mapResultSetToArticleQuantifierWithArticle(rs, articleRepository);
                logger.debug("ArticleQuantifier trouve:  ID={}", id);
                return Optional.of(aq);
            }

            logger.debug("Aucun ArticleQuantifier trouve avec l'ID: {}", id);
            return Optional.empty();

        } catch (SQLException e) {
            logger.error("Erreur lors de la recherche de ArticleQuantifier:  ID={}", id, e);
            throw new RuntimeException("Erreur lors de la recherche", e);
        }
    }

    @Override
    public List<ArticleQuantifier> findByMenuId(Long menuId) {
        if (menuId == null) {
            return new ArrayList<>();
        }

        List<ArticleQuantifier> results = new ArrayList<>();

        try (Connection conn = NeonConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_BY_MENU_ID)) {

            stmt.setLong(1, menuId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ArticleQuantifier aq = ArticleQuantifierMapper.mapResultSetToArticleQuantifierWithArticle(rs, articleRepository);
                results.add(aq);
            }

            logger.debug("ArticleQuantifier trouves pour menu ID={}:  {}", menuId, results.size());
            return results;

        } catch (SQLException e) {
            logger.error("Erreur lors de la recherche des ArticleQuantifier du menu:  ID={}", menuId, e);
            throw new RuntimeException("Erreur lors de la recherche", e);
        }
    }

    @Override
    public List<ArticleQuantifier> findByPanierId(Long panierId) {
        if (panierId == null) {
            return new ArrayList<>();
        }

        List<ArticleQuantifier> results = new ArrayList<>();

        try (Connection conn = NeonConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_BY_PANIER_ID)) {

            stmt.setLong(1, panierId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ArticleQuantifier aq = ArticleQuantifierMapper.mapResultSetToArticleQuantifierWithArticle(rs, articleRepository);
                results.add(aq);
            }

            logger.debug("ArticleQuantifier trouves pour panier ID={}: {}", panierId, results.size());
            return results;

        } catch (SQLException e) {
            logger.error("Erreur lors de la recherche des ArticleQuantifier du panier: ID={}", panierId, e);
            throw new RuntimeException("Erreur lors de la recherche", e);
        }
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("L'ID ne peut pas etre null");
        }

        try (Connection conn = NeonConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_BY_ID)) {

            stmt.setLong(1, id);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                logger.warn("Aucun ArticleQuantifier supprime:  ID={}", id);
            } else {
                logger.info("ArticleQuantifier supprime: ID={}", id);
            }

        } catch (SQLException e) {
            logger.error("Erreur lors de la suppression de ArticleQuantifier: ID={}", id, e);
            throw new RuntimeException("Erreur lors de la suppression", e);
        }
    }

    @Override
    public int deleteByMenuId(Long menuId) {
        if (menuId == null) {
            throw new IllegalArgumentException("L'ID du menu ne peut pas etre null");
        }

        try (Connection conn = NeonConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_BY_MENU_ID)) {

            stmt.setLong(1, menuId);
            int rowsAffected = stmt.executeUpdate();

            logger.info("ArticleQuantifier supprimes pour menu ID={}: {}", menuId, rowsAffected);
            return rowsAffected;

        } catch (SQLException e) {
            logger.error("Erreur lors de la suppression des ArticleQuantifier du menu: ID={}", menuId, e);
            throw new RuntimeException("Erreur lors de la suppression", e);
        }
    }

    @Override
    public int deleteByPanierId(Long panierId) {
        if (panierId == null) {
            throw new IllegalArgumentException("L'ID du panier ne peut pas etre null");
        }

        try (Connection conn = NeonConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_BY_PANIER_ID)) {

            stmt.setLong(1, panierId);
            int rowsAffected = stmt.executeUpdate();

            logger.info("ArticleQuantifier supprimes pour panier ID={}: {}", panierId, rowsAffected);
            return rowsAffected;

        } catch (SQLException e) {
            logger.error("Erreur lors de la suppression des ArticleQuantifier du panier: ID={}", panierId, e);
            throw new RuntimeException("Erreur lors de la suppression", e);
        }
    }

    @Override
    public long countByMenuId(Long menuId) {
        if (menuId == null) {
            return 0;
        }

        try (Connection conn = NeonConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(COUNT_BY_MENU_ID)) {

            stmt.setLong(1, menuId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                long count = rs.getLong(1);
                logger.debug("Nombre d'ArticleQuantifier pour menu ID={}: {}", menuId, count);
                return count;
            }

            return 0;

        } catch (SQLException e) {
            logger.error("Erreur lors du comptage des ArticleQuantifier du menu: ID={}", menuId, e);
            throw new RuntimeException("Erreur lors du comptage", e);
        }
    }

    @Override
    public long countByPanierId(Long panierId) {
        if (panierId == null) {
            return 0;
        }

        try (Connection conn = NeonConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(COUNT_BY_PANIER_ID)) {

            stmt.setLong(1, panierId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                long count = rs.getLong(1);
                logger.debug("Nombre d'ArticleQuantifier pour panier ID={}: {}", panierId, count);
                return count;
            }

            return 0;

        } catch (SQLException e) {
            logger.error("Erreur lors du comptage des ArticleQuantifier du panier: ID={}", panierId, e);
            throw new RuntimeException("Erreur lors du comptage", e);
        }
    }

    @Override
    public List<ArticleQuantifier> findAll() {
        List<ArticleQuantifier> results = new ArrayList<>();

        try (Connection conn = NeonConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(FIND_ALL)) {

            while (rs.next()) {
                ArticleQuantifier aq = ArticleQuantifierMapper.mapResultSetToArticleQuantifierWithArticle(rs, articleRepository);
                results.add(aq);
            }

            logger.debug("Nombre total d'ArticleQuantifier: {}", results.size());
            return results;

        } catch (SQLException e) {
            logger.error("Erreur lors de la recuperation de tous les ArticleQuantifier", e);
            throw new RuntimeException("Erreur lors de la recuperation", e);
        }
    }
}