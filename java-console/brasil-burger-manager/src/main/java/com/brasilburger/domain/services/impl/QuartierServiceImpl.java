package com.brasilburger.domain.services.impl;

import com.brasilburger.domain.entities.Quartier;
import com.brasilburger.domain.repositories.IQuartierRepository;
import com.brasilburger.domain.repositories.IZoneRepository;
import com.brasilburger.domain.services.IQuartierService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * Implémentation du service Quartier
 * Contient la logique métier pour la gestion des quartiers
 */
public class QuartierServiceImpl implements IQuartierService {
    private static final Logger logger = LoggerFactory.getLogger(QuartierServiceImpl.class);

    private final IQuartierRepository quartierRepository;
    private final IZoneRepository zoneRepository;

    public QuartierServiceImpl(IQuartierRepository quartierRepository, IZoneRepository zoneRepository) {
        this.quartierRepository = quartierRepository;
        this.zoneRepository = zoneRepository;
    }

    @Override
    public Quartier creerQuartier(String nom, Long idZone) {
        logger.info("Création d'un quartier: nom={}, idZone={}", nom, idZone);

        // Validation
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du quartier ne peut pas être vide");
        }

        if (idZone == null) {
            throw new IllegalArgumentException("L'ID de la zone ne peut pas être null");
        }

        // Vérifier que la zone existe
        if (!zoneRepository.findById(idZone).isPresent()) {
            throw new IllegalArgumentException("Zone non trouvée avec l'ID: " + idZone);
        }

        // Création
        Quartier quartier = new Quartier(nom.trim(), idZone);
        quartier = quartierRepository.save(quartier);

        logger.info("Quartier créé avec succès: ID={}, nom={}", quartier.getId(), quartier.getNom());
        return quartier;
    }

    @Override
    public Quartier modifierQuartier(Long id, String nouveauNom) {
        logger.info("Modification du quartier ID={}", id);

        if (id == null) {
            throw new IllegalArgumentException("L'ID ne peut pas être null");
        }

        // Récupérer le quartier
        Quartier quartier = quartierRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Quartier non trouvé avec l'ID: " + id));

        // Modifier le nom si fourni
        if (nouveauNom != null && !nouveauNom.trim().isEmpty()) {
            quartier.setNom(nouveauNom.trim());
        }

        // Sauvegarder
        quartier = quartierRepository.save(quartier);

        logger.info("Quartier modifié avec succès: ID={}, nom={}", quartier.getId(), quartier.getNom());
        return quartier;
    }

    @Override
    public Quartier changerZoneQuartier(Long id, Long nouvelleIdZone) {
        logger.info("Changement de zone pour le quartier ID={}", id);

        if (id == null) {
            throw new IllegalArgumentException("L'ID ne peut pas être null");
        }

        if (nouvelleIdZone == null) {
            throw new IllegalArgumentException("L'ID de la nouvelle zone ne peut pas être null");
        }

        // Vérifier que la nouvelle zone existe
        if (!zoneRepository.findById(nouvelleIdZone).isPresent()) {
            throw new IllegalArgumentException("Zone non trouvée avec l'ID: " + nouvelleIdZone);
        }

        // Récupérer le quartier
        Quartier quartier = quartierRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Quartier non trouvé avec l'ID:  " + id));

        // Changer de zone
        quartier.changerZone(nouvelleIdZone);
        quartier = quartierRepository.save(quartier);

        logger.info("Zone changée avec succès pour le quartier ID={}", quartier.getId());
        return quartier;
    }

    @Override
    public void supprimerQuartier(Long id) {
        logger.info("Suppression du quartier ID={}", id);

        if (id == null) {
            throw new IllegalArgumentException("L'ID ne peut pas être null");
        }

        // Vérifier l'existence
        if (! quartierRepository.findById(id).isPresent()) {
            throw new IllegalArgumentException("Quartier non trouvé avec l'ID: " + id);
        }

        quartierRepository.delete(id);
        logger.info("Quartier supprimé avec succès: ID={}", id);
    }

    @Override
    public Optional<Quartier> obtenirQuartierParId(Long id) {
        logger.debug("Recherche du quartier par ID: {}", id);
        return quartierRepository.findById(id);
    }

    @Override
    public Optional<Quartier> obtenirQuartierParNom(String nom) {
        logger.debug("Recherche du quartier par nom: {}", nom);
        return quartierRepository.findByNom(nom);
    }

    @Override
    public List<Quartier> listerTousLesQuartiers() {
        logger.debug("Liste de tous les quartiers");
        return quartierRepository.findAll();
    }

    @Override
    public List<Quartier> listerQuartiersParZone(Long idZone) {
        logger.debug("Liste des quartiers de la zone ID={}", idZone);

        if (idZone == null) {
            throw new IllegalArgumentException("L'ID de la zone ne peut pas être null");
        }

        return quartierRepository.findByZoneId(idZone);
    }

    @Override
    public long compterQuartiers() {
        return quartierRepository.count();
    }

    @Override
    public long compterQuartiersParZone(Long idZone) {
        if (idZone == null) {
            throw new IllegalArgumentException("L'ID de la zone ne peut pas être null");
        }

        return quartierRepository.countByZoneId(idZone);
    }

    @Override
    public boolean quartierExiste(String nom) {
        return quartierRepository.existsByNom(nom);
    }
}