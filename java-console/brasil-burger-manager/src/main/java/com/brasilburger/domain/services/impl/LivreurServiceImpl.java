package com.brasilburger.domain.services.impl;

import com.brasilburger.domain.entities.Livreur;
import com.brasilburger.domain.repositories.ILivreurRepository;
import com.brasilburger.domain.services.ILivreurService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * Implémentation du service Livreur
 * Contient la logique métier pour la gestion des livreurs
 */
public class LivreurServiceImpl implements ILivreurService {
    private static final Logger logger = LoggerFactory.getLogger(LivreurServiceImpl.class);

    private final ILivreurRepository livreurRepository;

    public LivreurServiceImpl(ILivreurRepository livreurRepository) {
        this.livreurRepository = livreurRepository;
    }

    @Override
    public Livreur creerLivreur(String nom, String prenom, String telephone) {
        logger.info("Création d'un livreur: nom={}, prenom={}, telephone={}", nom, prenom, telephone);

        // Validation
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du livreur ne peut pas être vide");
        }

        if (prenom == null || prenom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le prénom du livreur ne peut pas être vide");
        }

        if (telephone == null || telephone.trim().isEmpty()) {
            throw new IllegalArgumentException("Le téléphone du livreur ne peut pas être vide");
        }

        // Validation format téléphone (basique)
        String telClean = telephone.trim();
        if (!telClean.matches("^[0-9]{9}$") && !telClean.matches("^\\+?[0-9]{10,15}$")) {
            throw new IllegalArgumentException("Format de téléphone invalide (ex: 771234567)");
        }

        // Vérifier que le téléphone n'existe pas déjà
        if (livreurRepository.existsByTelephone(telClean)) {
            throw new IllegalArgumentException("Un livreur avec ce téléphone existe déjà");
        }

        // Création
        Livreur livreur = new Livreur(nom.trim(), prenom.trim(), telClean);
        livreur = livreurRepository.save(livreur);

        logger.info("Livreur créé avec succès: ID={}, nom={} {}",
                livreur.getId(), livreur.getPrenom(), livreur.getNom());
        return livreur;
    }

    @Override
    public Livreur modifierLivreur(Long id, String nouveauNom, String nouveauPrenom, String nouveauTelephone) {
        logger.info("Modification du livreur ID={}", id);

        if (id == null) {
            throw new IllegalArgumentException("L'ID ne peut pas être null");
        }

        // Récupérer le livreur
        Livreur livreur = livreurRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Livreur non trouvé avec l'ID: " + id));

        // Modifier le nom si fourni
        if (nouveauNom != null && !nouveauNom.trim().isEmpty()) {
            livreur.setNom(nouveauNom.trim());
        }

        // Modifier le prénom si fourni
        if (nouveauPrenom != null && !nouveauPrenom.trim().isEmpty()) {
            livreur.setPrenom(nouveauPrenom.trim());
        }

        // Modifier le téléphone si fourni
        if (nouveauTelephone != null && !nouveauTelephone.trim().isEmpty()) {
            String telClean = nouveauTelephone.trim();

            // Valider le format
            if (!telClean.matches("^[0-9]{9}$") && !telClean.matches("^\\+?[0-9]{10,15}$")) {
                throw new IllegalArgumentException("Format de téléphone invalide");
            }

            // Vérifier que le téléphone n'est pas déjà utilisé par un autre livreur
            Optional<Livreur> existant = livreurRepository.findByTelephone(telClean);
            if (existant.isPresent() && ! existant.get().getId().equals(id)) {
                throw new IllegalArgumentException("Ce téléphone est déjà utilisé par un autre livreur");
            }

            livreur.setTelephone(telClean);
        }

        // Sauvegarder
        livreur = livreurRepository.save(livreur);

        logger.info("Livreur modifié avec succès: ID={}, nom={} {}",
                livreur.getId(), livreur.getPrenom(), livreur.getNom());
        return livreur;
    }

    @Override
    public Livreur marquerDisponible(Long id) {
        logger.info("Marquage du livreur ID={} comme disponible", id);

        if (id == null) {
            throw new IllegalArgumentException("L'ID ne peut pas être null");
        }

        Livreur livreur = livreurRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Livreur non trouvé avec l'ID: " + id));

        // Vérifier qu'il n'est pas archivé
        if (livreur.isEstArchiver()) {
            throw new IllegalStateException("Impossible de marquer comme disponible un livreur archivé");
        }

        livreur.marquerDisponible();
        livreur = livreurRepository.save(livreur);

        logger.info("Livreur marqué disponible:  ID={}", livreur.getId());
        return livreur;
    }

    @Override
    public Livreur marquerOccupe(Long id) {
        logger.info("Marquage du livreur ID={} comme occupé", id);

        if (id == null) {
            throw new IllegalArgumentException("L'ID ne peut pas être null");
        }

        Livreur livreur = livreurRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Livreur non trouvé avec l'ID: " + id));

        livreur.marquerOccupe();
        livreur = livreurRepository.save(livreur);

        logger.info("Livreur marqué occupé: ID={}", livreur.getId());
        return livreur;
    }

    @Override
    public Livreur archiverLivreur(Long id) {
        logger.info("Archivage du livreur ID={}", id);

        if (id == null) {
            throw new IllegalArgumentException("L'ID ne peut pas être null");
        }

        Livreur livreur = livreurRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Livreur non trouvé avec l'ID: " + id));

        livreur.archiver();
        livreur = livreurRepository.save(livreur);

        logger.info("Livreur archivé avec succès: ID={}", livreur.getId());
        return livreur;
    }

    @Override
    public Livreur restaurerLivreur(Long id) {
        logger.info("Restauration du livreur ID={}", id);

        if (id == null) {
            throw new IllegalArgumentException("L'ID ne peut pas être null");
        }

        Livreur livreur = livreurRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Livreur non trouvé avec l'ID: " + id));

        livreur.restaurer();
        livreur = livreurRepository.save(livreur);

        logger.info("Livreur restauré avec succès: ID={}", livreur.getId());
        return livreur;
    }

    @Override
    public void supprimerLivreur(Long id) {
        logger.info("Suppression du livreur ID={}", id);

        if (id == null) {
            throw new IllegalArgumentException("L'ID ne peut pas être null");
        }

        // Vérifier l'existence
        if (!livreurRepository.findById(id).isPresent()) {
            throw new IllegalArgumentException("Livreur non trouvé avec l'ID: " + id);
        }

        livreurRepository.delete(id);
        logger.info("Livreur supprimé avec succès: ID={}", id);
    }

    @Override
    public Optional<Livreur> obtenirLivreurParId(Long id) {
        logger.debug("Recherche du livreur par ID:  {}", id);
        return livreurRepository.findById(id);
    }

    @Override
    public Optional<Livreur> obtenirLivreurParTelephone(String telephone) {
        logger.debug("Recherche du livreur par téléphone: {}", telephone);
        return livreurRepository.findByTelephone(telephone);
    }

    @Override
    public List<Livreur> listerTousLesLivreurs() {
        logger.debug("Liste de tous les livreurs");
        return livreurRepository.findAll();
    }

    @Override
    public List<Livreur> listerLivreursDisponibles() {
        logger.debug("Liste des livreurs disponibles");
        return livreurRepository.findAvailable();
    }

    @Override
    public List<Livreur> listerLivreursOccupes() {
        logger.debug("Liste des livreurs occupés");
        return livreurRepository.findByEstDisponible(false);
    }

    @Override
    public List<Livreur> listerLivreursActifs() {
        logger.debug("Liste des livreurs actifs");
        return livreurRepository.findByEstArchiver(false);
    }

    @Override
    public List<Livreur> listerLivreursArchives() {
        logger.debug("Liste des livreurs archivés");
        return livreurRepository.findByEstArchiver(true);
    }

    @Override
    public long compterLivreurs() {
        return livreurRepository.count();
    }

    @Override
    public long compterLivreursDisponibles() {
        return livreurRepository.countAvailable();
    }

    @Override
    public boolean livreurExiste(String telephone) {
        return livreurRepository.existsByTelephone(telephone);
    }

    @Override
    public boolean peutEtreAffecte(Long id) {
        if (id == null) {
            return false;
        }

        Optional<Livreur> livreurOpt = livreurRepository.findById(id);
        return livreurOpt.isPresent() && livreurOpt.get().peutEtreAffecte();
    }
}