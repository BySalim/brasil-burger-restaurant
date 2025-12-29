<?php

namespace App\Security;

use App\Entity\Utilisateur;
use Symfony\Component\Security\Core\Authentication\Token\TokenInterface;
use Symfony\Component\Security\Core\Exception\CustomUserMessageAccountStatusException;
use Symfony\Component\Security\Core\User\UserCheckerInterface;
use Symfony\Component\Security\Core\User\UserInterface;

class GestionnaireUserChecker implements UserCheckerInterface
{
    public function checkPreAuth(UserInterface $user): void
    {
        if (!$user instanceof Utilisateur) {
            return;
        }

        if (in_array('ROLE_CLIENT', $user->getRoles()) && !in_array('ROLE_GESTIONNAIRE', $user->getRoles())) {
            throw new CustomUserMessageAccountStatusException('Accès réservé aux gestionnaires. Les clients ne peuvent pas se connecter ici.');
        }
    }

    public function checkPostAuth(UserInterface $user, TokenInterface|null $token = null): void
    {
        // Rien à faire ici pour ton cas
    }
}
