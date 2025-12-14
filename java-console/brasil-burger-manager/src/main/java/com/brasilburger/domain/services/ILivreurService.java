package com.brasilburger.domain.services;

import com.brasilburger.domain.entities.Livreur;

import java.util.List;
import java.util.Optional;

/**
 * Interface du service Livreur
 * Contient la logique métier pour la gestion des livreurs
 */
public interface ILivreurService {

    /**
     * Crée un nouveau livreur
     * @param nom Nom du livreur
     * @param prenom Prénom du livreur
     * @param telephone Téléphone du livreur
     * @return Livreur créé
     * @throws IllegalArgumentException si les paramètres sont invalides
     */
    Livreur creerLivreur(String nom, String prenom, String telephone);

    /**
     * Modifie un livreur existant
     * @param id ID du livreur
     * @param nouveauNom Nouveau nom (null pour ne pas modifier)
     * @param nouveauPrenom Nouveau prénom (null pour ne pas modifier)
     * @param nouveauTelephone Nouveau téléphone (null pour ne pas modifier)
     * @return Livreur modifié
     */
    Livreur modifierLivreur(Long id, String nouveauNom, String nouveauPrenom, String nouveauTelephone);

    /**
     * Marque un livreur comme disponible
     * @param id ID du livreur
     * @return Livreur modifié
     */
    Livreur marquerDisponible(Long id);

    /**
     * Marque un livreur comme occupé
     * @param id ID du livreur
     * @return Livreur modifié
     */
    Livreur marquerOccupe(Long id);

    /**
     * Archive un livreur
     * @param id ID du livreur
     * @return Livreur archivé
     */
    Livreur archiverLivreur(Long id);

    /**
     * Restaure un livreur archivé
     * @param id ID du livreur
     * @return Livreur restauré
     */
    Livreur restaurerLivreur(Long id);

    /**
     * Supprime un livreur
     * @param id ID du livreur
     */
    void supprimerLivreur(Long id);

    /**
     * Récupère un livreur par son ID
     * @param id ID du livreur
     * @return Optional contenant le livreur si trouvé
     */
    Optional<Livreur> obtenirLivreurParId(Long id);

    /**
     * Récupère un livreur par son téléphone
     * @param telephone Téléphone du livreur
     * @return Optional contenant le livreur si trouvé
     */
    Optional<Livreur> obtenirLivreurParTelephone(String telephone);

    /**
     * Liste tous les livreurs
     * @return Liste de tous les livreurs
     */
    List<Livreur> listerTousLesLivreurs();

    /**
     * Liste les livreurs disponibles (non archivés et disponibles)
     * @return Liste des livreurs disponibles
     */
    List<Livreur> listerLivreursDisponibles();

    /**
     * Liste les livreurs occupés (non archivés et occupés)
     * @return Liste des livreurs occupés
     */
    List<Livreur> listerLivreursOccupes();

    /**
     * Liste les livreurs actifs (non archivés)
     * @return Liste des livreurs actifs
     */
    List<Livreur> listerLivreursActifs();

    /**
     * Liste les livreurs archivés
     * @return Liste des livreurs archivés
     */
    List<Livreur> listerLivreursArchives();

    /**
     * Compte le nombre total de livreurs
     * @return Nombre de livreurs
     */
    long compterLivreurs();

    /**
     * Compte le nombre de livreurs disponibles
     * @return Nombre de livreurs disponibles
     */
    long compterLivreursDisponibles();

    /**
     * Vérifie si un livreur existe par son téléphone
     * @param telephone Téléphone du livreur
     * @return true si le livreur existe
     */
    boolean livreurExiste(String telephone);

    /**
     * Vérifie si un livreur peut être affecté (actif et disponible)
     * @param id ID du livreur
     * @return true si le livreur peut être affecté
     */
    boolean peutEtreAffecte(Long id);
}