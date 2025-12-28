<?php

namespace App\Infrastructure\Images;

/**
 * Interface pour la gestion du stockage et de l'accès aux images
 * Permet de découpler le code métier des implémentations spécifiques
 * (Cloudinary, S3, local storage, etc.)
 */
interface ImageStorageInterface
{
    /**
     * Génère l'URL complète d'une image à partir du public ID
     *
     * @param string $publicId Identifiant public de l'image
     * @param array<string, mixed> $transformations Transformations optionnelles (w, h, c, q, f, etc.)
     * @return string URL complète vers l'image
     */
    public function getImageUrl(string $publicId, array $transformations = []): string;

    /**
     * Génère l'URL d'une vignette (thumbnail) avec dimensions prédéfinies
     *
     * @param string $publicId Identifiant public de l'image
     * @param int $width Largeur en pixels (défaut : 400)
     * @param int $height Hauteur en pixels (défaut : 400)
     * @return string URL complète vers la vignette
     */
    public function getThumbnailUrl(string $publicId, int $width = 400, int $height = 400): string;
}
