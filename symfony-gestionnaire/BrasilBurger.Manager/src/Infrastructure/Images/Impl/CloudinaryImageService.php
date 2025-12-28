<?php

namespace App\Infrastructure\Images\Impl;

use App\Infrastructure\Images\ImageStorageInterface;


final readonly class CloudinaryImageService implements ImageStorageInterface
{
    public function __construct(
        private string $cloudName,
        private string $baseUrl,
    ) {}

    /**
     * {@inheritdoc}
     */
    public function getImageUrl(string $publicId, array $transformations = []): string
    {
        $baseUrl = "{$this->baseUrl}/{$this->cloudName}";

        if (!empty($transformations)) {
            $transformString = $this->buildTransformationString($transformations);
            $baseUrl .= "/{$transformString}";
        }

        return "{$baseUrl}/{$publicId}";
    }

    /**
     * {@inheritdoc}
     */
    public function getThumbnailUrl(string $publicId, int $width = 400, int $height = 400): string
    {
        return $this->getImageUrl($publicId, [
            'w' => $width,
            'h' => $height,
            'c' => 'fill',      // crop (fill = remplir la zone)
            'q' => 'auto',      // qualité auto
            'f' => 'auto',      // format auto (webp si navigateur supporte)
        ]);
    }

    /**
     * Construit la chaîne de transformations Cloudinary
     * Exemple : ['w' => 400, 'h' => 400, 'c' => 'fill', 'q' => 'auto']
     * Résultat : 'w_400,h_400,c_fill,q_auto'
     */
    private function buildTransformationString(array $transformations): string
    {
        $parts = [];
        foreach ($transformations as $key => $value) {
            $parts[] = "{$key}_{$value}";
        }
        return implode(',', $parts);
    }
}
