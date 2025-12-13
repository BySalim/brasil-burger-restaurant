package com.brasilburger.infrastructure.database.mappers;

import com.brasilburger.domain.entities.Zone;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Mapper pour convertir entre ResultSet SQL et entite Zone
 */
public class ZoneMapper {

    /**
     * Convertit un ResultSet en entite Zone
     */
    public static Zone mapResultSetToZone(ResultSet rs) throws SQLException {
        Zone zone = new Zone();
        zone.setId(rs.getLong("id"));
        zone.setNom(rs.getString("nom"));
        zone.setPrixLivraison(rs.getInt("prix_livraison"));
        zone.setEstArchiver(rs.getBoolean("est_archiver"));
        return zone;
    }

    /**
     * Empeche l'instantiation
     */
    private ZoneMapper() {
        throw new UnsupportedOperationException("Classe utilitaire - pas d'instantiation");
    }
}