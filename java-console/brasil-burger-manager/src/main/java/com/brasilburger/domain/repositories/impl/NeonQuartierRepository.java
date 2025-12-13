package com.brasilburger.domain.repositories.impl;

import com.brasilburger.domain.entities.Quartier;
import com.brasilburger.domain.repositories.IQuartierRepository;
import com.brasilburger.infrastructure.database.NeonConnectionManager;
import com.brasilburger.infrastructure.database.mappers.QuartierMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation du repository Quartier pour Neon (PostgreSQL)
 */
public class NeonQuartierRepository implements IQuartierRepository {
    private static final Logger logger = LoggerFactory.getLogger(NeonQuartierRepository.class);

    private static final String INSERT_QUARTIER =
            "INSERT INTO quartier (nom, id_zone) VALUES (?, ?) RETURNING id";

    private static final String UPDATE_QUARTIER =
            "UPDATE quartier SET nom = ?, id_zone = ? WHERE id = ? ";

    private static final String FIND_BY_ID =
            "SELECT id, nom, id_zone FROM quartier WHERE id = ?";

    private static final String FIND_BY_NOM =
            "SELECT id, nom, id_zone FROM quartier WHERE nom = ?";

    private static final String FIND_ALL =
            "SELECT id, nom, id_zone FROM quartier ORDER BY nom";

    private static final String FIND_BY_ZONE_ID =
            "SELECT id, nom, id_zone FROM quartier WHERE id_zone = ?  ORDER BY nom";

    private static final String DELETE_QUARTIER =
            "DELETE FROM quartier WHERE id = ?";

    private static final String EXISTS_BY_NOM =
            "SELECT COUNT(*) FROM quartier WHERE nom = ?";

    private static final String COUNT_ALL =
            "SELECT COUNT(*) FROM quartier";

    private static final String COUNT_BY_ZONE_ID =
            "SELECT COUNT(*) FROM quartier WHERE id_zone = ?";

    @Override
    public Quartier save(Quartier quartier) {
        if (quartier == null) {
            throw new IllegalArgumentException("Le quartier ne peut pas etre null");
        }

        if (quartier.getId() == null) {
            return insert(quartier);
        } else {
            return update(quartier);
        }
    }

    /**
     * Insere un nouveau quartier
     */
    private Quartier insert(Quartier quartier) {
        try (Connection conn = NeonConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_QUARTIER)) {

            stmt.setString(1, quartier.getNom());
            stmt.setLong(2, quartier.getIdZone());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                quartier.setId(rs.getLong("id"));
                logger.info("Quartier cree avec succes: ID={}, Nom={}", quartier.getId(), quartier.getNom());
            }

            return quartier;

        } catch (SQLException e) {
            logger.error("Erreur lors de l'insertion du quartier:  {}", quartier.getNom(), e);
            throw new RuntimeException("Erreur lors de la creation du quartier", e);
        }
    }

    /**
     * Met a jour un quartier existant
     */
    private Quartier update(Quartier quartier) {
        try (Connection conn = NeonConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_QUARTIER)) {

            stmt.setString(1, quartier.getNom());
            stmt.setLong(2, quartier.getIdZone());
            stmt.setLong(3, quartier.getId());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                logger.warn("Aucun quartier trouve avec l'ID: {}", quartier.getId());
            } else {
                logger.info("Quartier mis a jour avec succes: ID={}, Nom={}", quartier.getId(), quartier.getNom());
            }

            return quartier;

        } catch (SQLException e) {
            logger.error("Erreur lors de la mise a jour du quartier: ID={}", quartier.getId(), e);
            throw new RuntimeException("Erreur lors de la mise a jour du quartier", e);
        }
    }

    @Override
    public Optional<Quartier> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }

        try (Connection conn = NeonConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_BY_ID)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Quartier quartier = QuartierMapper.mapResultSetToQuartier(rs);
                logger.debug("Quartier trouve:  ID={}, Nom={}", quartier.getId(), quartier.getNom());
                return Optional.of(quartier);
            }

            logger.debug("Aucun quartier trouve avec l'ID: {}", id);
            return Optional.empty();

        } catch (SQLException e) {
            logger.error("Erreur lors de la recherche du quartier par ID: {}", id, e);
            throw new RuntimeException("Erreur lors de la recherche du quartier", e);
        }
    }

    @Override
    public Optional<Quartier> findByNom(String nom) {
        if (nom == null || nom.trim().isEmpty()) {
            return Optional.empty();
        }

        try (Connection conn = NeonConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_BY_NOM)) {

            stmt.setString(1, nom.trim());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Quartier quartier = QuartierMapper.mapResultSetToQuartier(rs);
                logger.debug("Quartier trouve: Nom={}", quartier.getNom());
                return Optional.of(quartier);
            }

            logger.debug("Aucun quartier trouve avec le nom: {}", nom);
            return Optional.empty();

        } catch (SQLException e) {
            logger.error("Erreur lors de la recherche du quartier par nom: {}", nom, e);
            throw new RuntimeException("Erreur lors de la recherche du quartier", e);
        }
    }

    @Override
    public List<Quartier> findAll() {
        List<Quartier> quartiers = new ArrayList<>();

        try (Connection conn = NeonConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(FIND_ALL)) {

            while (rs.next()) {
                quartiers.add(QuartierMapper.mapResultSetToQuartier(rs));
            }

            logger.debug("Nombre de quartiers recuperes: {}", quartiers.size());
            return quartiers;

        } catch (SQLException e) {
            logger.error("Erreur lors de la recuperation de tous les quartiers", e);
            throw new RuntimeException("Erreur lors de la recuperation des quartiers", e);
        }
    }

    @Override
    public List<Quartier> findByZoneId(Long zoneId) {
        if (zoneId == null) {
            return new ArrayList<>();
        }

        List<Quartier> quartiers = new ArrayList<>();

        try (Connection conn = NeonConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_BY_ZONE_ID)) {

            stmt.setLong(1, zoneId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                quartiers.add(QuartierMapper.mapResultSetToQuartier(rs));
            }

            logger.debug("Nombre de quartiers recuperes pour la zone {}: {}", zoneId, quartiers.size());
            return quartiers;

        } catch (SQLException e) {
            logger.error("Erreur lors de la recuperation des quartiers par zone:  {}", zoneId, e);
            throw new RuntimeException("Erreur lors de la recuperation des quartiers", e);
        }
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("L'ID ne peut pas etre null");
        }

        try (Connection conn = NeonConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_QUARTIER)) {

            stmt.setLong(1, id);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                logger.warn("Aucun quartier supprime:  ID={}", id);
            } else {
                logger.info("Quartier supprime avec succes: ID={}", id);
            }

        } catch (SQLException e) {
            logger.error("Erreur lors de la suppression du quartier: ID={}", id, e);
            throw new RuntimeException("Erreur lors de la suppression du quartier", e);
        }
    }

    @Override
    public boolean existsByNom(String nom) {
        if (nom == null || nom.trim().isEmpty()) {
            return false;
        }

        try (Connection conn = NeonConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(EXISTS_BY_NOM)) {

            stmt.setString(1, nom.trim());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

            return false;

        } catch (SQLException e) {
            logger.error("Erreur lors de la verification d'existence du quartier:  {}", nom, e);
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
                logger.debug("Nombre total de quartiers: {}", count);
                return count;
            }

            return 0;

        } catch (SQLException e) {
            logger.error("Erreur lors du comptage des quartiers", e);
            throw new RuntimeException("Erreur lors du comptage des quartiers", e);
        }
    }

    @Override
    public long countByZoneId(Long zoneId) {
        if (zoneId == null) {
            return 0;
        }

        try (Connection conn = NeonConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(COUNT_BY_ZONE_ID)) {

            stmt.setLong(1, zoneId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                long count = rs.getLong(1);
                logger.debug("Nombre de quartiers dans la zone {}: {}", zoneId, count);
                return count;
            }

            return 0;

        } catch (SQLException e) {
            logger.error("Erreur lors du comptage des quartiers par zone: {}", zoneId, e);
            throw new RuntimeException("Erreur lors du comptage des quartiers", e);
        }
    }
}