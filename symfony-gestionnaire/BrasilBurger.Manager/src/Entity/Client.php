<?php

namespace App\Entity;

use App\Enum\Role;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity]
class Client extends Utilisateur
{
    #[ORM\Column(length: 20, unique: true, nullable: true)]
    private ?string $telephone = null;

    public function getRole(): Role
    {
        return Role::CLIENT;
    }

    public function getTelephone(): ?string
    {
        return $this->telephone;
    }

    public function setTelephone(?string $telephone): static
    {
        $this->telephone = $telephone;
        return $this;
    }
}
