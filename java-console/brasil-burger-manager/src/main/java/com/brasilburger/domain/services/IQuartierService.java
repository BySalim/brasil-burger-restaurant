package com.brasilburger.domain.services;

import com.brasilburger.domain.entities.Quartier;

import java.util.List;
import java.util.Optional;

/**
 * Interface du service Quartier
 * Contient la logique métier pour la gestion des quartiers
 */
public interface IQuartierService {

    /**
     * Crée un nouveau quartier
     * @param nom Nom du quartier
     * @param idZone ID de la zone
     * @return Quartier créé
     */
    Quartier creerQuartier(String nom, Long idZone);

    /**
     * Modifie un quartier existant
     * @param id ID du quartier
     * @param nouveauNom Nouveau nom (null pour ne pas modifier)
     * @return Quartier modifié
     */
    Quartier modifierQuartier(Long id, String nouveauNom);

    /**
     * Change la zone d'un quartier
     * @param id ID du quartier
     * @param nouvelleIdZone Nouvelle ID de zone
     * @return Quartier modifié
     */
    Quartier changerZoneQuartier(Long id, Long nouvelleIdZone);

    /**
     * Supprime un quartier
     * @param id ID du quartier
     */
    void supprimerQuartier(Long id);

    /**
     * Récupère un quartier par son ID
     * @param id ID du quartier
     * @return Optional contenant le quartier si trouvé
     */
    Optional<Quartier> obtenirQuartierParId(Long id);

    /**
     * Récupère un quartier par son nom
     * @param nom Nom du quartier
     * @return Optional contenant le quartier si trouvé
     */
    Optional<Quartier> obtenirQuartierParNom(String nom);

    /**
     * Liste tous les quartiers
     * @return Liste de tous les quartiers
     */
    List<Quartier> listerTousLesQuartiers();

    /**
     * Liste les quartiers d'une zone
     * @param idZone ID de la zone
     * @return Liste des quartiers de la zone
     */
    List<Quartier> listerQuartiersParZone(Long idZone);

    /**
     * Compte le nombre total de quartiers
     * @return Nombre de quartiers
     */
    long compterQuartiers();

    /**
     * Compte le nombre de quartiers dans une zone
     * @param idZone ID de la zone
     * @return Nombre de quartiers
     */
    long compterQuartiersParZone(Long idZone);

    /**
     * Vérifie si un quartier existe par son nom
     * @param nom Nom du quartier
     * @return true si le quartier existe
     */
    boolean quartierExiste(String nom);
}