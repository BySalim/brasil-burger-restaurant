<?php

namespace App\Entity;

use App\Enum\StatutLivraison;
use App\Repository\LivraisonRepository;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: LivraisonRepository::class)]
#[ORM\Table(name: 'livraison')]
class Livraison
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\Column(length: 20, enumType: StatutLivraison::class)]
    private ?StatutLivraison $statut = null;

    #[ORM\ManyToOne(inversedBy: 'livraisons')]
    #[ORM\JoinColumn(name: 'id_groupe_livraison', nullable: false)]
    private ?GroupeLivraison $groupeLivraison = null;

    #[ORM\OneToOne(inversedBy: 'livraison', cascade: ['persist', 'remove'])]
    #[ORM\JoinColumn(name: 'id_commande', nullable: false)]
    private ?Commande $commande = null;

    public function __construct()
    {
        $this->statut = StatutLivraison::EN_COURS;
    }

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getStatut(): ?StatutLivraison
    {
        return $this->statut;
    }

    public function setStatut(StatutLivraison $statut): static
    {
        $this->statut = $statut;
        return $this;
    }

    public function getGroupeLivraison(): ?GroupeLivraison
    {
        return $this->groupeLivraison;
    }

    public function setGroupeLivraison(?GroupeLivraison $groupeLivraison): static
    {
        $this->groupeLivraison = $groupeLivraison;
        return $this;
    }

    public function getCommande(): ?Commande
    {
        return $this->commande;
    }

    public function setCommande(Commande $commande): static
    {
        $this->commande = $commande;
        return $this;
    }
}
