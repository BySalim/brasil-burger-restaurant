package com.brasilburger.domain.services.impl;

import com.brasilburger.domain.entities.Zone;
import com.brasilburger.domain.repositories.IZoneRepository;
import com.brasilburger.domain.services.IZoneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * Implémentation du service Zone
 * Contient la logique métier pour la gestion des zones
 */
public class ZoneServiceImpl implements IZoneService {
    private static final Logger logger = LoggerFactory.getLogger(ZoneServiceImpl.class);

    private final IZoneRepository zoneRepository;

    public ZoneServiceImpl(IZoneRepository zoneRepository) {
        this.zoneRepository = zoneRepository;
    }

    @Override
    public Zone creerZone(String nom, Integer prixLivraison) {
        logger.info("Création d'une zone:  nom={}, prix={}", nom, prixLivraison);

        // Validation
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom de la zone ne peut pas être vide");
        }

        if (prixLivraison == null || prixLivraison < 0) {
            throw new IllegalArgumentException("Le prix de livraison doit être positif ou nul");
        }

        // Création
        Zone zone = new Zone(nom.trim(), prixLivraison);
        zone = zoneRepository.save(zone);

        logger.info("Zone créée avec succès:  ID={}, nom={}", zone.getId(), zone.getNom());
        return zone;
    }

    @Override
    public Zone modifierZone(Long id, String nouveauNom, Integer nouveauPrix) {
        logger.info("Modification de la zone ID={}", id);

        if (id == null) {
            throw new IllegalArgumentException("L'ID ne peut pas être null");
        }

        // Récupérer la zone
        Zone zone = zoneRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Zone non trouvée avec l'ID: " + id));

        // Modifier le nom si fourni
        if (nouveauNom != null && ! nouveauNom.trim().isEmpty()) {
            zone.modifierNom(nouveauNom.trim());
        }

        // Modifier le prix si fourni
        if (nouveauPrix != null) {
            zone.modifierPrixLivraison(nouveauPrix);
        }

        // Sauvegarder
        zone = zoneRepository.save(zone);

        logger.info("Zone modifiée avec succès: ID={}, nom={}", zone.getId(), zone.getNom());
        return zone;
    }

    @Override
    public Zone archiverZone(Long id) {
        logger.info("Archivage de la zone ID={}", id);

        if (id == null) {
            throw new IllegalArgumentException("L'ID ne peut pas être null");
        }

        Zone zone = zoneRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Zone non trouvée avec l'ID: " + id));

        zone.archiver();
        zone = zoneRepository.save(zone);

        logger.info("Zone archivée avec succès: ID={}", zone.getId());
        return zone;
    }

    @Override
    public Zone restaurerZone(Long id) {
        logger.info("Restauration de la zone ID={}", id);

        if (id == null) {
            throw new IllegalArgumentException("L'ID ne peut pas être null");
        }

        Zone zone = zoneRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Zone non trouvée avec l'ID: " + id));

        zone.restaurer();
        zone = zoneRepository.save(zone);

        logger.info("Zone restaurée avec succès: ID={}", zone.getId());
        return zone;
    }

    @Override
    public void supprimerZone(Long id) {
        logger.info("Suppression de la zone ID={}", id);

        if (id == null) {
            throw new IllegalArgumentException("L'ID ne peut pas être null");
        }

        // Vérifier l'existence
        if (!zoneRepository.findById(id).isPresent()) {
            throw new IllegalArgumentException("Zone non trouvée avec l'ID: " + id);
        }

        zoneRepository.delete(id);
        logger.info("Zone supprimée avec succès: ID={}", id);
    }

    @Override
    public Optional<Zone> obtenirZoneParId(Long id) {
        logger.debug("Recherche de la zone par ID: {}", id);
        return zoneRepository.findById(id);
    }

    @Override
    public Optional<Zone> obtenirZoneParNom(String nom) {
        logger.debug("Recherche de la zone par nom: {}", nom);
        return zoneRepository.findByNom(nom);
    }

    @Override
    public List<Zone> listerToutesLesZones() {
        logger.debug("Liste de toutes les zones");
        return zoneRepository.findAll();
    }

    @Override
    public List<Zone> listerZonesActives() {
        logger.debug("Liste des zones actives");
        return zoneRepository.findByEstArchiver(false);
    }

    @Override
    public List<Zone> listerZonesArchivees() {
        logger.debug("Liste des zones archivées");
        return zoneRepository.findByEstArchiver(true);
    }

    @Override
    public long compterZones() {
        return zoneRepository.count();
    }

    @Override
    public boolean zoneExiste(String nom) {
        return zoneRepository.existsByNom(nom);
    }
}