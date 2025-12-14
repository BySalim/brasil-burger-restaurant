package com.brasilburger.domain.repositories.impl;

import com.brasilburger.domain.entities.*;
import com.brasilburger.domain.entities.enums.CategorieArticle;
import com.brasilburger.domain.exceptions.DuplicateCodeArticleException;
import com.brasilburger.domain.repositories.IArticleRepository;
import com.brasilburger.infrastructure.database.NeonConnectionManager;
import com.brasilburger.infrastructure.database.mappers.ArticleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation du repository Article pour Neon (PostgreSQL)
 */
public class NeonArticleRepository implements IArticleRepository {
    private static final Logger logger = LoggerFactory.getLogger(NeonArticleRepository.class);

    // Requetes SQL
    private static final String INSERT_ARTICLE =
            "INSERT INTO article (code, libelle, image_public_id, est_archiver, categorie, description, prix, type_complement) " +
                    "VALUES (?, ?, ?, ?, ? ::text, ?, ?, ? ::text) RETURNING id";

    private static final String UPDATE_ARTICLE =
            "UPDATE article SET code = ?, libelle = ?, image_public_id = ?, est_archiver = ?, " +
                    "categorie = ? ::text, description = ?, prix = ?, type_complement = ? ::text " +
                    "WHERE id = ?";

    private static final String FIND_BY_ID =
            "SELECT id, code, libelle, image_public_id, est_archiver, categorie, description, prix, type_complement " +
                    "FROM article WHERE id = ?";

    private static final String FIND_BY_CODE =
            "SELECT id, code, libelle, image_public_id, est_archiver, categorie, description, prix, type_complement " +
                    "FROM article WHERE code = ?";

    private static final String FIND_ALL =
            "SELECT id, code, libelle, image_public_id, est_archiver, categorie, description, prix, type_complement " +
                    "FROM article ORDER BY libelle";

    private static final String FIND_BY_CATEGORIE =
            "SELECT id, code, libelle, image_public_id, est_archiver, categorie, description, prix, type_complement " +
                    "FROM article WHERE categorie = ?  ORDER BY libelle";

    private static final String FIND_BY_EST_ARCHIVER =
            "SELECT id, code, libelle, image_public_id, est_archiver, categorie, description, prix, type_complement " +
                    "FROM article WHERE est_archiver = ?  ORDER BY libelle";

    private static final String FIND_AVAILABLE_BY_CATEGORIE =
            "SELECT id, code, libelle, image_public_id, est_archiver, categorie, description, prix, type_complement " +
                    "FROM article WHERE categorie = ? AND est_archiver = false ORDER BY libelle";

    private static final String DELETE_ARTICLE =
            "DELETE FROM article WHERE id = ?";

    private static final String EXISTS_BY_CODE =
            "SELECT COUNT(*) FROM article WHERE code = ? ";

    private static final String COUNT_ALL =
            "SELECT COUNT(*) FROM article";

    private static final String COUNT_BY_CATEGORIE =
            "SELECT COUNT(*) FROM article WHERE categorie = ?";

    @Override
    public Article save(Article article) {
        if (article == null) {
            throw new IllegalArgumentException("L'article ne peut pas etre null");
        }

        // Verifier si le code existe deja
        Optional<Article> existingByCode = findByCode(article.getCode());

        if (article.getId() == null) {
            // Mode INSERT :  verifier qu'aucun article avec ce code n'existe
            if (existingByCode.isPresent()) {
                throw new DuplicateCodeArticleException(article.getCode());
            }
            return insert(article);
        } else {
            // Mode UPDATE : verifier qu'aucun AUTRE article avec ce code n'existe
            if (existingByCode.isPresent() && !existingByCode.get().getId().equals(article.getId())) {
                throw new DuplicateCodeArticleException(article.getCode());
            }
            return update(article);
        }
    }

    /**
     * Insere un nouvel article
     */
    private Article insert(Article article) {
        try (Connection conn = NeonConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_ARTICLE)) {

            setArticleParameters(stmt, article);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                article.setId(rs.getLong("id"));
                logger.info("Article cree avec succes: ID={}, Code={}, Type={}",
                        article.getId(), article.getCode(), article.getCategorie());
            }

            return article;

        } catch (SQLException e) {
            logger.error("Erreur lors de l'insertion de l'article:  {}", article.getCode(), e);
            throw new RuntimeException("Erreur lors de la creation de l'article", e);
        }
    }

    /**
     * Met a jour un article existant
     */
    private Article update(Article article) {
        try (Connection conn = NeonConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_ARTICLE)) {

            setArticleParameters(stmt, article);
            stmt.setLong(9, article.getId()); // ID en dernier pour le WHERE

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                logger.warn("Aucun article trouve avec l'ID: {}", article.getId());
            } else {
                logger.info("Article mis a jour avec succes: ID={}, Code={}", article.getId(), article.getCode());
            }

            return article;

        } catch (SQLException e) {
            logger.error("Erreur lors de la mise a jour de l'article: ID={}", article.getId(), e);
            throw new RuntimeException("Erreur lors de la mise a jour de l'article", e);
        }
    }

    /**
     * Definit les parametres du PreparedStatement selon le type d'article
     */
    private void setArticleParameters(PreparedStatement stmt, Article article) throws SQLException {
        stmt.setString(1, article.getCode());
        stmt.setString(2, article.getLibelle());
        stmt.setString(3, article.getImagePublicId());
        stmt.setBoolean(4, article.isEstArchiver());
        stmt.setString(5, article.getCategorie().name());

        // Parametres specifiques selon le type
        if (article instanceof Burger) {
            Burger burger = (Burger) article;
            stmt.setString(6, burger.getDescription());
            stmt.setObject(7, burger.getPrix());
            stmt.setObject(8, null); // type_complement = NULL

        } else if (article instanceof Menu) {
            Menu menu = (Menu) article;
            stmt.setString(6, menu.getDescription());
            stmt.setObject(7, null); // prix = NULL pour menu
            stmt.setObject(8, null); // type_complement = NULL

        } else if (article instanceof Complement) {
            Complement complement = (Complement) article;
            stmt.setObject(6, null); // description = NULL
            stmt.setObject(7, complement.getPrix());
            stmt.setString(8, complement.getType() != null ? complement.getType().name() : null);

        } else {
            // Article générique (ne devrait pas arriver)
            stmt.setObject(6, null);
            stmt.setObject(7, null);
            stmt.setObject(8, null);
        }
    }

    @Override
    public Optional<Article> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }

        try (Connection conn = NeonConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_BY_ID)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Article article = ArticleMapper.mapResultSetToArticle(rs);
                logger.debug("Article trouve:  ID={}, Code={}", article.getId(), article.getCode());
                return Optional.of(article);
            }

            logger.debug("Aucun article trouve avec l'ID: {}", id);
            return Optional.empty();

        } catch (SQLException e) {
            logger.error("Erreur lors de la recherche de l'article par ID: {}", id, e);
            throw new RuntimeException("Erreur lors de la recherche de l'article", e);
        }
    }

    @Override
    public Optional<Article> findByCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return Optional.empty();
        }

        try (Connection conn = NeonConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_BY_CODE)) {

            stmt.setString(1, code.trim());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Article article = ArticleMapper.mapResultSetToArticle(rs);
                logger.debug("Article trouve: Code={}", code);
                return Optional.of(article);
            }

            logger.debug("Aucun article trouve avec le code: {}", code);
            return Optional.empty();

        } catch (SQLException e) {
            logger.error("Erreur lors de la recherche de l'article par code: {}", code, e);
            throw new RuntimeException("Erreur lors de la recherche de l'article", e);
        }
    }

    @Override
    public List<Article> findAll() {
        List<Article> articles = new ArrayList<>();

        try (Connection conn = NeonConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(FIND_ALL)) {

            while (rs.next()) {
                articles.add(ArticleMapper.mapResultSetToArticle(rs));
            }

            logger.debug("Nombre d'articles recuperes: {}", articles.size());
            return articles;

        } catch (SQLException e) {
            logger.error("Erreur lors de la recuperation de tous les articles", e);
            throw new RuntimeException("Erreur lors de la recuperation des articles", e);
        }
    }

    @Override
    public List<Article> findByCategorie(CategorieArticle categorie) {
        if (categorie == null) {
            return new ArrayList<>();
        }

        List<Article> articles = new ArrayList<>();

        try (Connection conn = NeonConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_BY_CATEGORIE)) {

            stmt.setString(1, categorie.name());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                articles.add(ArticleMapper.mapResultSetToArticle(rs));
            }

            logger.debug("Nombre d'articles {} recuperes:  {}", categorie, articles.size());
            return articles;

        } catch (SQLException e) {
            logger.error("Erreur lors de la recuperation des articles par categorie:  {}", categorie, e);
            throw new RuntimeException("Erreur lors de la recuperation des articles", e);
        }
    }

    @Override
    public List<Article> findByEstArchiver(boolean estArchiver) {
        List<Article> articles = new ArrayList<>();

        try (Connection conn = NeonConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_BY_EST_ARCHIVER)) {

            stmt.setBoolean(1, estArchiver);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                articles.add(ArticleMapper.mapResultSetToArticle(rs));
            }

            logger.debug("Nombre d'articles {} recuperes: {}",
                    estArchiver ? "archives" : "non archives", articles.size());
            return articles;

        } catch (SQLException e) {
            logger.error("Erreur lors de la recuperation des articles par statut d'archivage", e);
            throw new RuntimeException("Erreur lors de la recuperation des articles", e);
        }
    }

    @Override
    public List<Article> findAvailableByCategorie(CategorieArticle categorie) {
        if (categorie == null) {
            return new ArrayList<>();
        }

        List<Article> articles = new ArrayList<>();

        try (Connection conn = NeonConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_AVAILABLE_BY_CATEGORIE)) {

            stmt.setString(1, categorie.name());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                articles.add(ArticleMapper.mapResultSetToArticle(rs));
            }

            logger.debug("Nombre d'articles {} disponibles: {}", categorie, articles.size());
            return articles;

        } catch (SQLException e) {
            logger.error("Erreur lors de la recuperation des articles disponibles par categorie: {}", categorie, e);
            throw new RuntimeException("Erreur lors de la recuperation des articles", e);
        }
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("L'ID ne peut pas etre null");
        }

        try (Connection conn = NeonConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_ARTICLE)) {

            stmt.setLong(1, id);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                logger.warn("Aucun article supprime:  ID={}", id);
            } else {
                logger.info("Article supprime avec succes: ID={}", id);
            }

        } catch (SQLException e) {
            logger.error("Erreur lors de la suppression de l'article: ID={}", id, e);
            throw new RuntimeException("Erreur lors de la suppression de l'article", e);
        }
    }

    @Override
    public boolean existsByCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return false;
        }

        try (Connection conn = NeonConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(EXISTS_BY_CODE)) {

            stmt.setString(1, code.trim());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

            return false;

        } catch (SQLException e) {
            logger.error("Erreur lors de la verification d'existence de l'article: {}", code, e);
            throw new RuntimeException("Erreur lors de la verification d'existence", e);
        }
    }

    @Override
    public long count() {
        try (Connection conn = NeonConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(COUNT_ALL)) {

            if (rs.next()) {
                long count = rs.getLong(1);
                logger.debug("Nombre total d'articles: {}", count);
                return count;
            }

            return 0;

        } catch (SQLException e) {
            logger.error("Erreur lors du comptage des articles", e);
            throw new RuntimeException("Erreur lors du comptage des articles", e);
        }
    }

    @Override
    public long countByCategorie(CategorieArticle categorie) {
        if (categorie == null) {
            return 0;
        }

        try (Connection conn = NeonConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(COUNT_BY_CATEGORIE)) {

            stmt.setString(1, categorie.name());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                long count = rs.getLong(1);
                logger.debug("Nombre d'articles {}: {}", categorie, count);
                return count;
            }

            return 0;

        } catch (SQLException e) {
            logger.error("Erreur lors du comptage des articles par categorie:  {}", categorie, e);
            throw new RuntimeException("Erreur lors du comptage des articles", e);
        }
    }
}