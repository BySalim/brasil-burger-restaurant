package com.brasilburger.domain.repositories;

import com.brasilburger.domain.entities.Quartier;

import java.util.List;
import java.util.Optional;

/**
 * Interface du repository pour l'entite Quartier
 */
public interface IQuartierRepository {

    /**
     * Sauvegarde un quartier (creation ou mise a jour)
     * @return Quartier sauvegarde avec son ID genere
     */
    Quartier save(Quartier quartier);

    /**
     * Trouve un quartier par son ID
     */
    Optional<Quartier> findById(Long id);

    /**
     * Trouve un quartier par son nom
     */
    Optional<Quartier> findByNom(String nom);

    /**
     * Retourne tous les quartiers
     */
    List<Quartier> findAll();

    /**
     * Retourne les quartiers d'une zone specifique
     */
    List<Quartier> findByZoneId(Long zoneId);

    /**
     * Supprime un quartier par son ID
     */
    void delete(Long id);

    /**
     * Verifie si un quartier avec ce nom existe deja
     */
    boolean existsByNom(String nom);

    /**
     * Compte le nombre total de quartiers
     */
    long count();

    /**
     * Compte le nombre de quartiers dans une zone
     */
    long countByZoneId(Long zoneId);
}