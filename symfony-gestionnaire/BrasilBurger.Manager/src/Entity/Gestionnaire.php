<?php

namespace App\Entity;

use App\Enum\Role;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity]
class Gestionnaire extends Utilisateur
{
    public function getRole(): Role
    {
        return Role::GESTIONNAIRE;
    }
}
