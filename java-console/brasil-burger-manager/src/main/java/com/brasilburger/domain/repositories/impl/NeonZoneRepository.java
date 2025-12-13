package com.brasilburger.domain.repositories.impl;

import com.brasilburger.domain.entities.Zone;
import com.brasilburger.domain.exceptions.DuplicateZoneException;
import com.brasilburger.domain.repositories.IZoneRepository;
import com.brasilburger.infrastructure.database.NeonConnectionManager;
import com.brasilburger.infrastructure.database.mappers.ZoneMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation du repository Zone pour Neon (PostgreSQL)
 */
public class NeonZoneRepository implements IZoneRepository {
    private static final Logger logger = LoggerFactory.getLogger(NeonZoneRepository.class);

    private static final String INSERT_ZONE =
            "INSERT INTO zone (nom, prix_livraison, est_archiver) VALUES (?, ?, ?) RETURNING id";

    private static final String UPDATE_ZONE =
            "UPDATE zone SET nom = ?, prix_livraison = ?, est_archiver = ? WHERE id = ?";

    private static final String FIND_BY_ID =
            "SELECT id, nom, prix_livraison, est_archiver FROM zone WHERE id = ?";

    private static final String FIND_BY_NOM =
            "SELECT id, nom, prix_livraison, est_archiver FROM zone WHERE nom = ?";

    private static final String FIND_ALL =
            "SELECT id, nom, prix_livraison, est_archiver FROM zone ORDER BY nom";

    private static final String FIND_BY_EST_ARCHIVER =
            "SELECT id, nom, prix_livraison, est_archiver FROM zone WHERE est_archiver = ?  ORDER BY nom";

    private static final String DELETE_ZONE =
            "DELETE FROM zone WHERE id = ?";

    private static final String EXISTS_BY_NOM =
            "SELECT COUNT(*) FROM zone WHERE nom = ? ";

    private static final String COUNT_ALL =
            "SELECT COUNT(*) FROM zone";

    @Override
    public Zone save(Zone zone) {
        if (zone == null) {
            throw new IllegalArgumentException("La zone ne peut pas etre null");
        }

        // Verifier si le nom existe deja
        Optional<Zone> existingByNom = findByNom(zone.getNom());

        if (zone.getId() == null) {
            // Mode INSERT :  verifier qu'aucune zone avec ce nom n'existe
            if (existingByNom.isPresent()) {
                throw new DuplicateZoneException(zone.getNom());
            }
            return insert(zone);
        } else {
            // Mode UPDATE : verifier qu'aucune AUTRE zone avec ce nom n'existe
            if (existingByNom.isPresent() && !existingByNom.get().getId().equals(zone.getId())) {
                throw new DuplicateZoneException(zone.getNom());
            }
            return update(zone);
        }
    }

    /**
     * Insere une nouvelle zone
     */
    private Zone insert(Zone zone) {
        try (Connection conn = NeonConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_ZONE)) {

            stmt.setString(1, zone.getNom());
            stmt.setInt(2, zone.getPrixLivraison());
            stmt.setBoolean(3, zone.isEstArchiver());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                zone.setId(rs.getLong("id"));
                logger.info("Zone creee avec succes:  ID={}, Nom={}", zone.getId(), zone.getNom());
            }

            return zone;

        } catch (SQLException e) {
            logger.error("Erreur lors de l'insertion de la zone:  {}", zone.getNom(), e);
            throw new RuntimeException("Erreur lors de la creation de la zone", e);
        }
    }

    /**
     * Met a jour une zone existante
     */
    private Zone update(Zone zone) {
        try (Connection conn = NeonConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_ZONE)) {

            stmt.setString(1, zone.getNom());
            stmt.setInt(2, zone.getPrixLivraison());
            stmt.setBoolean(3, zone.isEstArchiver());
            stmt.setLong(4, zone.getId());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                logger.warn("Aucune zone trouvee avec l'ID: {}", zone.getId());
            } else {
                logger.info("Zone mise a jour avec succes:  ID={}, Nom={}", zone.getId(), zone.getNom());
            }

            return zone;

        } catch (SQLException e) {
            logger.error("Erreur lors de la mise a jour de la zone:  ID={}", zone.getId(), e);
            throw new RuntimeException("Erreur lors de la mise a jour de la zone", e);
        }
    }

    @Override
    public Optional<Zone> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }

        try (Connection conn = NeonConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_BY_ID)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Zone zone = ZoneMapper.mapResultSetToZone(rs);
                logger.debug("Zone trouvee:  ID={}, Nom={}", zone.getId(), zone.getNom());
                return Optional.of(zone);
            }

            logger.debug("Aucune zone trouvee avec l'ID: {}", id);
            return Optional.empty();

        } catch (SQLException e) {
            logger.error("Erreur lors de la recherche de la zone par ID: {}", id, e);
            throw new RuntimeException("Erreur lors de la recherche de la zone", e);
        }
    }

    @Override
    public Optional<Zone> findByNom(String nom) {
        if (nom == null || nom.trim().isEmpty()) {
            return Optional.empty();
        }

        try (Connection conn = NeonConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_BY_NOM)) {

            stmt.setString(1, nom.trim());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Zone zone = ZoneMapper.mapResultSetToZone(rs);
                logger.debug("Zone trouvee:  Nom={}", zone.getNom());
                return Optional.of(zone);
            }

            logger.debug("Aucune zone trouvee avec le nom:  {}", nom);
            return Optional.empty();

        } catch (SQLException e) {
            logger.error("Erreur lors de la recherche de la zone par nom: {}", nom, e);
            throw new RuntimeException("Erreur lors de la recherche de la zone", e);
        }
    }

    @Override
    public List<Zone> findAll() {
        List<Zone> zones = new ArrayList<>();

        try (Connection conn = NeonConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(FIND_ALL)) {

            while (rs.next()) {
                zones.add(ZoneMapper.mapResultSetToZone(rs));
            }

            logger.debug("Nombre de zones recuperees: {}", zones.size());
            return zones;

        } catch (SQLException e) {
            logger.error("Erreur lors de la recuperation de toutes les zones", e);
            throw new RuntimeException("Erreur lors de la recuperation des zones", e);
        }
    }

    @Override
    public List<Zone> findByEstArchiver(boolean estArchiver) {
        List<Zone> zones = new ArrayList<>();

        try (Connection conn = NeonConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_BY_EST_ARCHIVER)) {

            stmt.setBoolean(1, estArchiver);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                zones.add(ZoneMapper.mapResultSetToZone(rs));
            }

            logger.debug("Nombre de zones {} recuperees:  {}",
                    estArchiver ? "archivees" : "non archivees", zones.size());
            return zones;

        } catch (SQLException e) {
            logger.error("Erreur lors de la recuperation des zones par statut d'archivage", e);
            throw new RuntimeException("Erreur lors de la recuperation des zones", e);
        }
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("L'ID ne peut pas etre null");
        }

        try (Connection conn = NeonConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_ZONE)) {

            stmt.setLong(1, id);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                logger.warn("Aucune zone supprimee:  ID={}", id);
            } else {
                logger.info("Zone supprimee avec succes:  ID={}", id);
            }

        } catch (SQLException e) {
            logger.error("Erreur lors de la suppression de la zone: ID={}", id, e);
            throw new RuntimeException("Erreur lors de la suppression de la zone", e);
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
            logger.error("Erreur lors de la verification d'existence de la zone:  {}", nom, e);
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
                logger.debug("Nombre total de zones: {}", count);
                return count;
            }

            return 0;

        } catch (SQLException e) {
            logger.error("Erreur lors du comptage des zones", e);
            throw new RuntimeException("Erreur lors du comptage des zones", e);
        }
    }
}