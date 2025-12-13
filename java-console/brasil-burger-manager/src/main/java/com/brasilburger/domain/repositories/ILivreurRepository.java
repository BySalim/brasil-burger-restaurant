package com.brasilburger.domain.repositories;

import com.brasilburger.domain.entities.Livreur;

import java.util.List;
import java.util.Optional;

/**
 * Interface du repository pour l'entite Livreur
 */
public interface ILivreurRepository {

    /**
     * Sauvegarde un livreur (creation ou mise a jour)
     * @return Livreur sauvegarde avec son ID genere
     */
    Livreur save(Livreur livreur);

    /**
     * Trouve un livreur par son ID
     */
    Optional<Livreur> findById(Long id);

    /**
     * Trouve un livreur par son telephone
     */
    Optional<Livreur> findByTelephone(String telephone);

    /**
     * Retourne tous les livreurs
     */
    List<Livreur> findAll();

    /**
     * Retourne les livreurs selon leur statut d'archivage
     */
    List<Livreur> findByEstArchiver(boolean estArchiver);

    /**
     * Retourne les livreurs disponibles (non archives et disponibles)
     */
    List<Livreur> findAvailable();

    /**
     * Retourne les livreurs selon leur disponibilite
     */
    List<Livreur> findByEstDisponible(boolean estDisponible);

    /**
     * Supprime un livreur par son ID
     */
    void delete(Long id);

    /**
     * Verifie si un livreur avec ce telephone existe deja
     */
    boolean existsByTelephone(String telephone);

    /**
     * Compte le nombre total de livreurs
     */
    long count();

    /**
     * Compte le nombre de livreurs disponibles
     */
    long countAvailable();
}