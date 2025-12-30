<?php

namespace App\Entity;

use App\Enum\StatutLivraison;
use App\Repository\LivraisonRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: LivraisonRepository::class)]
#[ORM\Table(name: 'livraison')]
class Livraison
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\Column(length: 20, enumType: StatutLivraison::class, options: ['default' => 'EN_COURS'])]
    private StatutLivraison $statut = StatutLivraison::EN_COURS;

    #[ORM\Column(name: 'date_debut', type: Types::DATE_MUTABLE, options: ['default' => 'CURRENT_DATE'])]
    private ?\DateTimeInterface $dateDebut = null;

    #[ORM\Column(name: 'date_fin', type: Types::DATE_MUTABLE, nullable: true)]
    private ?\DateTimeInterface $dateFin = null;

    #[ORM\ManyToOne(targetEntity: GroupeLivraison::class, inversedBy: 'livraisons')]
    #[ORM\JoinColumn(name: 'id_groupe_livraison', referencedColumnName: 'id', nullable: false, onDelete: 'CASCADE')]
    private ?GroupeLivraison $groupeLivraison = null;

    #[ORM\OneToMany(targetEntity: Commande::class, mappedBy: 'livraison')]
    private Collection $commandes;

    public function __construct()
    {
        $this->dateDebut = new \DateTime();
        $this->commandes = new ArrayCollection();
    }

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getStatut(): StatutLivraison
    {
        return $this->statut;
    }

    public function setStatut(StatutLivraison $statut): static
    {
        $this->statut = $statut;
        return $this;
    }

    public function getDateDebut(): ?\DateTimeInterface
    {
        return $this->dateDebut;
    }

    public function setDateDebut(\DateTimeInterface $dateDebut): static
    {
        $this->dateDebut = $dateDebut;
        return $this;
    }

    public function getDateFin(): ?\DateTimeInterface
    {
        return $this->dateFin;
    }

    public function setDateFin(?\DateTimeInterface $dateFin): static
    {
        $this->dateFin = $dateFin;
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

    /**
     * @return Collection<int, Commande>
     */
    public function getCommandes(): Collection
    {
        return $this->commandes;
    }

    public function addCommande(Commande $commande): static
    {
        if (!$this->commandes->contains($commande)) {
            $this->commandes->add($commande);
            $commande->setLivraison($this);
        }

        return $this;
    }

    public function removeCommande(Commande $commande): static
    {
        if ($this->commandes->removeElement($commande)) {
            if ($commande->getLivraison() === $this) {
                $commande->setLivraison(null);
            }
        }

        return $this;
    }
}
