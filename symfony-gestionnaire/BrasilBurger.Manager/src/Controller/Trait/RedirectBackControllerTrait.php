<?php

namespace App\Controller\Trait;

use Symfony\Component\HttpFoundation\RedirectResponse;
use Symfony\Component\HttpFoundation\Request;

trait RedirectBackControllerTrait
{
    /**
     * Redirige vers la page précédente
     */
    private function redirectBack(Request $request, string $fallbackRoute, bool $withParams = true): RedirectResponse
    {
        $referer = $request->headers->get('referer');

        if ($referer) {
            if (! $withParams) {
                $referer = parse_url($referer, PHP_URL_PATH);
            }
            return $this->redirect($referer);
        }

        return $this->redirectToRoute($fallbackRoute);
    }
}
