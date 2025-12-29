<?php

namespace App\Entity;

use App\Enum\ModePaiement;
use App\Repository\PaiementRepository;
use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: PaiementRepository::class)]
#[ORM\Table(name: 'paiement')]
class Paiement
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\Column(name: 'date_paie', type: Types::DATETIME_MUTABLE)]
    private ?\DateTimeInterface $datePaie = null;

    #[ORM\Column]
    private ?int $montant = null;

    #[ORM\Column(name: 'mode_paie', length: 20, enumType: ModePaiement::class)]
    private ?ModePaiement $modePaie = null;

    #[ORM\OneToOne(inversedBy: 'paiement', cascade: ['persist', 'remove'])]
    #[ORM\JoinColumn(name: 'id_commande', nullable: false)]
    private ?Commande $commande = null;

    #[ORM\ManyToOne]
    #[ORM\JoinColumn(name: 'id_client', nullable: false)]
    private ?Client $client = null;

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getDatePaie(): ?\DateTimeInterface
    {
        return $this->datePaie;
    }

    public function setDatePaie(\DateTimeInterface $datePaie): static
    {
        $this->datePaie = $datePaie;
        return $this;
    }

    public function getMontant(): ?int
    {
        return $this->montant;
    }

    public function setMontant(int $montant): static
    {
        $this->montant = $montant;
        return $this;
    }

    public function getModePaie(): ?ModePaiement
    {
        return $this->modePaie;
    }

    public function setModePaie(ModePaiement $modePaie): static
    {
        $this->modePaie = $modePaie;
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

    public function getClient(): ?Client
    {
        return $this->client;
    }

    public function setClient(?Client $client): static
    {
        $this->client = $client;
        return $this;
    }
}
