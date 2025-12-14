package com.brasilburger.domain.services;

import com.brasilburger.domain.entities.Zone;

import java.util.List;
import java.util.Optional;

/**
 * Interface du service Zone
 * Contient la logique métier pour la gestion des zones
 */
public interface IZoneService {

    /**
     * Crée une nouvelle zone
     * @param nom Nom de la zone
     * @param prixLivraison Prix de livraison en FCFA
     * @return Zone créée
     * @throws IllegalArgumentException si les paramètres sont invalides
     * @throws com.brasilburger.domain.exceptions.DuplicateZoneException si le nom existe déjà
     */
    Zone creerZone(String nom, Integer prixLivraison);

    /**
     * Modifie une zone existante
     * @param id ID de la zone
     * @param nouveauNom Nouveau nom (null pour ne pas modifier)
     * @param nouveauPrix Nouveau prix (null pour ne pas modifier)
     * @return Zone modifiée
     */
    Zone modifierZone(Long id, String nouveauNom, Integer nouveauPrix);

    /**
     * Archive une zone
     * @param id ID de la zone
     * @return Zone archivée
     */
    Zone archiverZone(Long id);

    /**
     * Restaure une zone archivée
     * @param id ID de la zone
     * @return Zone restaurée
     */
    Zone restaurerZone(Long id);

    /**
     * Supprime une zone
     * @param id ID de la zone
     */
    void supprimerZone(Long id);

    /**
     * Récupère une zone par son ID
     * @param id ID de la zone
     * @return Optional contenant la zone si trouvée
     */
    Optional<Zone> obtenirZoneParId(Long id);

    /**
     * Récupère une zone par son nom
     * @param nom Nom de la zone
     * @return Optional contenant la zone si trouvée
     */
    Optional<Zone> obtenirZoneParNom(String nom);

    /**
     * Liste toutes les zones
     * @return Liste de toutes les zones
     */
    List<Zone> listerToutesLesZones();

    /**
     * Liste les zones actives (non archivées)
     * @return Liste des zones actives
     */
    List<Zone> listerZonesActives();

    /**
     * Liste les zones archivées
     * @return Liste des zones archivées
     */
    List<Zone> listerZonesArchivees();

    /**
     * Compte le nombre total de zones
     * @return Nombre de zones
     */
    long compterZones();

    /**
     * Vérifie si une zone existe par son nom
     * @param nom Nom de la zone
     * @return true si la zone existe
     */
    boolean zoneExiste(String nom);
}