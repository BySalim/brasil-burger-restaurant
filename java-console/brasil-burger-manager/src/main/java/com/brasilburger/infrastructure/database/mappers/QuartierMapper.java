package com.brasilburger.infrastructure.database.mappers;

import com.brasilburger.domain.entities.Quartier;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Mapper pour convertir entre ResultSet SQL et entite Quartier
 */
public class QuartierMapper {

    /**
     * Convertit un ResultSet en entite Quartier
     */
    public static Quartier mapResultSetToQuartier(ResultSet rs) throws SQLException {
        Quartier quartier = new Quartier();
        quartier.setId(rs.getLong("id"));
        quartier.setNom(rs.getString("nom"));
        quartier.setIdZone(rs.getLong("id_zone"));
        return quartier;
    }

    /**
     * Empeche l'instantiation
     */
    private QuartierMapper() {
        throw new UnsupportedOperationException("Classe utilitaire - pas d'instantiation");
    }
}