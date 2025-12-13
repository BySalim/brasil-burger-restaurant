package com.brasilburger.infrastructure.database.mappers;

import com.brasilburger.domain.entities.Livreur;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Mapper pour convertir entre ResultSet SQL et entite Livreur
 */
public class LivreurMapper {

    /**
     * Convertit un ResultSet en entite Livreur
     */
    public static Livreur mapResultSetToLivreur(ResultSet rs) throws SQLException {
        Livreur livreur = new Livreur();
        livreur.setId(rs.getLong("id"));
        livreur.setNom(rs.getString("nom"));
        livreur.setPrenom(rs.getString("prenom"));
        livreur.setTelephone(rs.getString("telephone"));
        livreur.setEstArchiver(rs.getBoolean("est_archiver"));
        livreur.setEstDisponible(rs.getBoolean("est_disponible"));
        return livreur;
    }

    /**
     * Empeche l'instantiation
     */
    private LivreurMapper() {
        throw new UnsupportedOperationException("Classe utilitaire - pas d'instantiation");
    }
}