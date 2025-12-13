package com.brasilburger.domain.repositories;

import com.brasilburger.domain.entities.Zone;

import java.util.List;
import java.util.Optional;

/**
 * Interface du repository pour l'entite Zone
 * Definit les operations CRUD de base
 */
public interface IZoneRepository {

    /**
     * Sauvegarde une zone (creation ou mise a jour)
     * @return Zone sauvegardee avec son ID genere
     */
    Zone save(Zone zone);

    /**
     * Trouve une zone par son ID
     */
    Optional<Zone> findById(Long id);

    /**
     * Trouve une zone par son nom
     */
    Optional<Zone> findByNom(String nom);

    /**
     * Retourne toutes les zones
     */
    List<Zone> findAll();

    /**
     * Retourne les zones selon leur statut d'archivage
     */
    List<Zone> findByEstArchiver(boolean estArchiver);

    /**
     * Supprime une zone par son ID
     */
    void delete(Long id);

    /**
     * Verifie si une zone avec ce nom existe deja
     */
    boolean existsByNom(String nom);

    /**
     * Compte le nombre total de zones
     */
    long count();
}