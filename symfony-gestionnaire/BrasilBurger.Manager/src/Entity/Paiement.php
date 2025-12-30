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

    #[ORM\Column(name: 'date_paie', type: Types::DATE_MUTABLE, options: ['default' => 'CURRENT_DATE'])]
    private ?\DateTimeInterface $datePaie = null;

    #[ORM\Column(name: 'montant_paie')]
    private ?int $montantPaie = null;

    #[ORM\Column(name: 'mode_paie', length: 20, enumType: ModePaiement::class)]
    private ?ModePaiement $modePaie = null;

    #[ORM\Column(name: 'reference_paiement_externe', length: 100, unique: true, nullable: true)]
    private ?string $referencePaiementExterne = null;

    #[ORM\Column(options: ['default' => false])]
    private bool $test = false;

    #[ORM\ManyToOne(targetEntity: Client::class)]
    #[ORM\JoinColumn(name: 'id_client', referencedColumnName: 'id', nullable: true, onDelete: 'SET NULL')]
    private ?Client $client = null;

    #[ORM\OneToOne(targetEntity: Commande::class, inversedBy: 'paiement')]
    #[ORM\JoinColumn(name: 'id_commande', referencedColumnName: 'id', unique: true, nullable: false, onDelete: 'CASCADE')]
    private ?Commande $commande = null;

    public function __construct()
    {
        $this->datePaie = new \DateTime();
    }

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

    public function getMontantPaie(): ?int
    {
        return $this->montantPaie;
    }

    public function setMontantPaie(int $montantPaie): static
    {
        $this->montantPaie = $montantPaie;
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

    public function getReferencePaiementExterne(): ?string
    {
        return $this->referencePaiementExterne;
    }

    public function setReferencePaiementExterne(?string $referencePaiementExterne): static
    {
        $this->referencePaiementExterne = $referencePaiementExterne;
        return $this;
    }

    public function isTest(): bool
    {
        return $this->test;
    }

    public function setTest(bool $test): static
    {
        $this->test = $test;
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

    public function getCommande(): ?Commande
    {
        return $this->commande;
    }

    public function setCommande(?Commande $commande): static
    {
        $this->commande = $commande;
        return $this;
    }
}
