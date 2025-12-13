package com.brasilburger.domain.repositories.impl;

import com.brasilburger.domain.entities.Livreur;
import com.brasilburger.domain.repositories.ILivreurRepository;
import com.brasilburger.infrastructure.database.NeonConnectionManager;
import com.brasilburger.infrastructure.database.mappers.LivreurMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation du repository Livreur pour Neon (PostgreSQL)
 */
public class NeonLivreurRepository implements ILivreurRepository {
    private static final Logger logger = LoggerFactory.getLogger(NeonLivreurRepository.class);

    private static final String INSERT_LIVREUR =
            "INSERT INTO livreur (nom, prenom, telephone, est_archiver, est_disponible) " +
                    "VALUES (?, ?, ?, ?, ?) RETURNING id";

    private static final String UPDATE_LIVREUR =
            "UPDATE livreur SET nom = ?, prenom = ?, telephone = ?, est_archiver = ?, est_disponible = ? " +
                    "WHERE id = ?";

    private static final String FIND_BY_ID =
            "SELECT id, nom, prenom, telephone, est_archiver, est_disponible FROM livreur WHERE id = ?";

    private static final String FIND_BY_TELEPHONE =
            "SELECT id, nom, prenom, telephone, est_archiver, est_disponible FROM livreur WHERE telephone = ?";

    private static final String FIND_ALL =
            "SELECT id, nom, prenom, telephone, est_archiver, est_disponible FROM livreur ORDER BY nom, prenom";

    private static final String FIND_BY_EST_ARCHIVER =
            "SELECT id, nom, prenom, telephone, est_archiver, est_disponible FROM livreur " +
                    "WHERE est_archiver = ? ORDER BY nom, prenom";

    private static final String FIND_AVAILABLE =
            "SELECT id, nom, prenom, telephone, est_archiver, est_disponible FROM livreur " +
                    "WHERE est_archiver = false AND est_disponible = true ORDER BY nom, prenom";

    private static final String FIND_BY_EST_DISPONIBLE =
            "SELECT id, nom, prenom, telephone, est_archiver, est_disponible FROM livreur " +
                    "WHERE est_disponible = ?  AND est_archiver = false ORDER BY nom, prenom";

    private static final String DELETE_LIVREUR =
            "DELETE FROM livreur WHERE id = ?";

    private static final String EXISTS_BY_TELEPHONE =
            "SELECT COUNT(*) FROM livreur WHERE telephone = ?";

    private static final String COUNT_ALL =
            "SELECT COUNT(*) FROM livreur";

    private static final String COUNT_AVAILABLE =
            "SELECT COUNT(*) FROM livreur WHERE est_archiver = false AND est_disponible = true";

    @Override
    public Livreur save(Livreur livreur) {
        if (livreur == null) {
            throw new IllegalArgumentException("Le livreur ne peut pas etre null");
        }

        if (livreur.getId() == null) {
            return insert(livreur);
        } else {
            return update(livreur);
        }
    }

    /**
     * Insere un nouveau livreur
     */
    private Livreur insert(Livreur livreur) {
        try (Connection conn = NeonConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_LIVREUR)) {

            stmt.setString(1, livreur.getNom());
            stmt.setString(2, livreur.getPrenom());
            stmt.setString(3, livreur.getTelephone());
            stmt.setBoolean(4, livreur.isEstArchiver());
            stmt.setBoolean(5, livreur.isEstDisponible());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                livreur.setId(rs.getLong("id"));
                logger.info("Livreur cree avec succes: ID={}, Nom={} {}",
                        livreur.getId(), livreur.getPrenom(), livreur.getNom());
            }

            return livreur;

        } catch (SQLException e) {
            logger.error("Erreur lors de l'insertion du livreur:  {} {}",
                    livreur.getPrenom(), livreur.getNom(), e);
            throw new RuntimeException("Erreur lors de la creation du livreur", e);
        }
    }

    /**
     * Met a jour un livreur existant
     */
    private Livreur update(Livreur livreur) {
        try (Connection conn = NeonConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_LIVREUR)) {

            stmt.setString(1, livreur.getNom());
            stmt.setString(2, livreur.getPrenom());
            stmt.setString(3, livreur.getTelephone());
            stmt.setBoolean(4, livreur.isEstArchiver());
            stmt.setBoolean(5, livreur.isEstDisponible());
            stmt.setLong(6, livreur.getId());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                logger.warn("Aucun livreur trouve avec l'ID: {}", livreur.getId());
            } else {
                logger.info("Livreur mis a jour avec succes: ID={}, Nom={} {}",
                        livreur.getId(), livreur.getPrenom(), livreur.getNom());
            }

            return livreur;

        } catch (SQLException e) {
            logger.error("Erreur lors de la mise a jour du livreur: ID={}", livreur.getId(), e);
            throw new RuntimeException("Erreur lors de la mise a jour du livreur", e);
        }
    }

    @Override
    public Optional<Livreur> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }

        try (Connection conn = NeonConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_BY_ID)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Livreur livreur = LivreurMapper.mapResultSetToLivreur(rs);
                logger.debug("Livreur trouve:  ID={}, Nom={} {}",
                        livreur.getId(), livreur.getPrenom(), livreur.getNom());
                return Optional.of(livreur);
            }

            logger.debug("Aucun livreur trouve avec l'ID: {}", id);
            return Optional.empty();

        } catch (SQLException e) {
            logger.error("Erreur lors de la recherche du livreur par ID: {}", id, e);
            throw new RuntimeException("Erreur lors de la recherche du livreur", e);
        }
    }

    @Override
    public Optional<Livreur> findByTelephone(String telephone) {
        if (telephone == null || telephone.trim().isEmpty()) {
            return Optional.empty();
        }

        try (Connection conn = NeonConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_BY_TELEPHONE)) {

            stmt.setString(1, telephone.trim());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Livreur livreur = LivreurMapper.mapResultSetToLivreur(rs);
                logger.debug("Livreur trouve:  Telephone={}", telephone);
                return Optional.of(livreur);
            }

            logger.debug("Aucun livreur trouve avec le telephone: {}", telephone);
            return Optional.empty();

        } catch (SQLException e) {
            logger.error("Erreur lors de la recherche du livreur par telephone: {}", telephone, e);
            throw new RuntimeException("Erreur lors de la recherche du livreur", e);
        }
    }

    @Override
    public List<Livreur> findAll() {
        List<Livreur> livreurs = new ArrayList<>();

        try (Connection conn = NeonConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(FIND_ALL)) {

            while (rs.next()) {
                livreurs.add(LivreurMapper.mapResultSetToLivreur(rs));
            }

            logger.debug("Nombre de livreurs recuperes: {}", livreurs.size());
            return livreurs;

        } catch (SQLException e) {
            logger.error("Erreur lors de la recuperation de tous les livreurs", e);
            throw new RuntimeException("Erreur lors de la recuperation des livreurs", e);
        }
    }

    @Override
    public List<Livreur> findByEstArchiver(boolean estArchiver) {
        List<Livreur> livreurs = new ArrayList<>();

        try (Connection conn = NeonConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_BY_EST_ARCHIVER)) {

            stmt.setBoolean(1, estArchiver);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                livreurs.add(LivreurMapper.mapResultSetToLivreur(rs));
            }

            logger.debug("Nombre de livreurs {} recuperes:  {}",
                    estArchiver ? "archives" : "non archives", livreurs.size());
            return livreurs;

        } catch (SQLException e) {
            logger.error("Erreur lors de la recuperation des livreurs par statut d'archivage", e);
            throw new RuntimeException("Erreur lors de la recuperation des livreurs", e);
        }
    }

    @Override
    public List<Livreur> findAvailable() {
        List<Livreur> livreurs = new ArrayList<>();

        try (Connection conn = NeonConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(FIND_AVAILABLE)) {

            while (rs.next()) {
                livreurs.add(LivreurMapper.mapResultSetToLivreur(rs));
            }

            logger.debug("Nombre de livreurs disponibles:  {}", livreurs.size());
            return livreurs;

        } catch (SQLException e) {
            logger.error("Erreur lors de la recuperation des livreurs disponibles", e);
            throw new RuntimeException("Erreur lors de la recuperation des livreurs", e);
        }
    }

    @Override
    public List<Livreur> findByEstDisponible(boolean estDisponible) {
        List<Livreur> livreurs = new ArrayList<>();

        try (Connection conn = NeonConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_BY_EST_DISPONIBLE)) {

            stmt.setBoolean(1, estDisponible);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                livreurs.add(LivreurMapper.mapResultSetToLivreur(rs));
            }

            logger.debug("Nombre de livreurs {} recuperes: {}",
                    estDisponible ? "disponibles" : "occupes", livreurs.size());
            return livreurs;

        } catch (SQLException e) {
            logger.error("Erreur lors de la recuperation des livreurs par disponibilite", e);
            throw new RuntimeException("Erreur lors de la recuperation des livreurs", e);
        }
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("L'ID ne peut pas etre null");
        }

        try (Connection conn = NeonConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_LIVREUR)) {

            stmt.setLong(1, id);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                logger.warn("Aucun livreur supprime:  ID={}", id);
            } else {
                logger.info("Livreur supprime avec succes: ID={}", id);
            }

        } catch (SQLException e) {
            logger.error("Erreur lors de la suppression du livreur:  ID={}", id, e);
            throw new RuntimeException("Erreur lors de la suppression du livreur", e);
        }
    }

    @Override
    public boolean existsByTelephone(String telephone) {
        if (telephone == null || telephone.trim().isEmpty()) {
            return false;
        }

        try (Connection conn = NeonConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(EXISTS_BY_TELEPHONE)) {

            stmt.setString(1, telephone.trim());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

            return false;

        } catch (SQLException e) {
            logger.error("Erreur lors de la verification d'existence du livreur:  {}", telephone, e);
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
                logger.debug("Nombre total de livreurs: {}", count);
                return count;
            }

            return 0;

        } catch (SQLException e) {
            logger.error("Erreur lors du comptage des livreurs", e);
            throw new RuntimeException("Erreur lors du comptage des livreurs", e);
        }
    }

    @Override
    public long countAvailable() {
        try (Connection conn = NeonConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(COUNT_AVAILABLE)) {

            if (rs.next()) {
                long count = rs.getLong(1);
                logger.debug("Nombre de livreurs disponibles: {}", count);
                return count;
            }

            return 0;

        } catch (SQLException e) {
            logger.error("Erreur lors du comptage des livreurs disponibles", e);
            throw new RuntimeException("Erreur lors du comptage des livreurs", e);
        }
    }
}