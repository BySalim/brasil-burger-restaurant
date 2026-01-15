<?php

namespace App\Command;

use App\Entity\Article;
use App\Entity\ArticleQuantifier;
use App\Entity\Burger;
use App\Entity\Client;
use App\Entity\Commande;
use App\Entity\Complement;
use App\Entity\InfoLivraison;
use App\Entity\Menu;
use App\Entity\Paiement;
use App\Entity\Panier;
use App\Entity\Quartier;
use App\Entity\Zone;
use App\Enum\CategorieArticleQuantifier;
use App\Enum\CategoriePanier;
use App\Enum\EtatCommande;
use App\Enum\ModePaiement;
use App\Enum\ModeRecuperation;
use App\Enum\TypeComplement;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Component\Console\Attribute\AsCommand;
use Symfony\Component\Console\Command\Command;
use Symfony\Component\Console\Input\InputInterface;
use Symfony\Component\Console\Output\OutputInterface;
use Symfony\Component\Console\Style\SymfonyStyle;

#[AsCommand(
    name: 'app:create-commande',
    description: 'Créer une commande pour un client',
)]
class CreateCommandeCommand extends Command
{
    public function __construct(
        private EntityManagerInterface $entityManager
    ) {
        parent::__construct();
    }

    protected function execute(InputInterface $input, OutputInterface $output): int
    {
        $io = new SymfonyStyle($input, $output);

        $io->title('Création d\'une nouvelle commande');

        // ====== 1. SÉLECTION DU CLIENT ======
        $client = $this->selectClient($io);
        if (!$client) {
            $io->error('Aucun client disponible. Créez d\'abord un client avec app:create-client');
            return Command::FAILURE;
        }

        $io->success(sprintf('Client sélectionné: %s %s (%s)',
            $client->getPrenom(),
            $client->getNom(),
            $client->getTelephone()
        ));

        // ====== 2. CHOIX DU TYPE DE COMMANDE (MENU OU BURGER) ======
        $typeCommande = $io->choice(
            'Type de commande',
            ['Menu', 'Burger'],
            'Menu'
        );

        $panier = new Panier();
        $montantTotalPanier = 0;
        $articlesQuantifies = [];

        if ($typeCommande === 'Menu') {
            // ====== 3A. SÉLECTION D'UN MENU ======
            $panier->setCategoriePanier(CategoriePanier::MENU);

            $menu = $this->selectMenu($io);
            if (!$menu) {
                $io->error('Aucun menu disponible.');
                return Command::FAILURE;
            }

            $quantiteMenu = (int) $io->ask('Quantité de menus', '1', function ($value) {
                if (!is_numeric($value) || $value < 1) {
                    throw new \RuntimeException('La quantité doit être un nombre positif');
                }
                return $value;
            });

            $prixMenu = $menu->getPrix();
            $montantMenu = $prixMenu * $quantiteMenu;
            $montantTotalPanier += $montantMenu;

            $articleQuantifier = new ArticleQuantifier();
            $articleQuantifier->setArticle($menu);
            $articleQuantifier->setQuantite($quantiteMenu);
            $articleQuantifier->setMontant($montantMenu);
            $articleQuantifier->setCategorie(CategorieArticleQuantifier::COMMANDE);
            $articleQuantifier->setPanier($panier);

            $articlesQuantifies[] = $articleQuantifier;

            $io->info(sprintf('Menu: %s x%d = %d FCFA',
                $menu->getLibelle(),
                $quantiteMenu,
                $montantMenu
            ));

        } else {
            // ====== 3B. SÉLECTION D'UN BURGER + COMPLÉMENTS ======
            $panier->setCategoriePanier(CategoriePanier::BURGER);

            $burger = $this->selectBurger($io);
            if (!$burger) {
                $io->error('Aucun burger disponible.');
                return Command::FAILURE;
            }

            $quantiteBurger = (int) $io->ask('Quantité de burgers', '1', function ($value) {
                if (!is_numeric($value) || $value < 1) {
                    throw new \RuntimeException('La quantité doit être un nombre positif');
                }
                return $value;
            });

            $prixBurger = $burger->getPrix();
            $montantBurger = $prixBurger * $quantiteBurger;
            $montantTotalPanier += $montantBurger;

            $articleQuantifier = new ArticleQuantifier();
            $articleQuantifier->setArticle($burger);
            $articleQuantifier->setQuantite($quantiteBurger);
            $articleQuantifier->setMontant($montantBurger);
            $articleQuantifier->setCategorie(CategorieArticleQuantifier::COMMANDE);
            $articleQuantifier->setPanier($panier);

            $articlesQuantifies[] = $articleQuantifier;

            $io->info(sprintf('Burger: %s x%d = %d FCFA',
                $burger->getLibelle(),
                $quantiteBurger,
                $montantBurger
            ));

            // ====== 3C. AJOUT DE COMPLÉMENTS ======
            if ($io->confirm('Ajouter des compléments ?', true)) {
                $complements = $this->addComplements($io, $panier, $montantTotalPanier, $articlesQuantifies);
                $montantTotalPanier = $complements['montant'];
                $articlesQuantifies = $complements['articles'];
            }
        }

        $panier->setMontantTotal($montantTotalPanier);

        // ====== 4. MODE DE RÉCUPÉRATION ======
        $modeRecuperationChoice = $io->choice(
            'Mode de récupération',
            ['SUR_PLACE', 'EMPORTER', 'LIVRER'],
            $client->getModeRecuperationDefaut()?->value ?? 'LIVRER'
        );
        $modeRecuperation = ModeRecuperation::from($modeRecuperationChoice);

        // ====== 5. INFO DE LIVRAISON (si mode LIVRER) ======
        $infoLivraison = null;
        $prixLivraison = 0;

        if ($modeRecuperation === ModeRecuperation::LIVRER) {
            $infoLivraisonData = $this->createInfoLivraison($io, $client);
            $infoLivraison = $infoLivraisonData['infoLivraison'];
            $prixLivraison = $infoLivraisonData['prix'];
        } else {
            // Créer une info de livraison vide pour les autres modes
            $zones = $this->entityManager->getRepository(Zone::class)->findAll();
            if (empty($zones)) {
                $io->error('Aucune zone disponible. Créez au moins une zone.');
                return Command::FAILURE;
            }
            $zone = $zones[0];

            $quartiers = $zone->getQuartiers();
            if ($quartiers->isEmpty()) {
                $io->error('Aucun quartier disponible dans cette zone.');
                return Command::FAILURE;
            }
            $quartier = $quartiers->first();

            $infoLivraison = new InfoLivraison();
            $infoLivraison->setZone($zone);
            $infoLivraison->setQuartier($quartier);
            $infoLivraison->setPrixLivraison(0);
        }

        // ====== 6. CALCUL DU MONTANT TOTAL ======
        $montantTotal = $montantTotalPanier + $prixLivraison;

        $io->section('Récapitulatif');
        $io->table(
            ['Description', 'Montant'],
            [
                ['Montant panier', $montantTotalPanier . ' FCFA'],
                ['Frais de livraison', $prixLivraison . ' FCFA'],
                ['TOTAL', $montantTotal . ' FCFA'],
            ]
        );

        // ====== 7. MODE DE PAIEMENT ======
        $modePaiementChoice = $io->choice(
            'Mode de paiement',
            ['WAVE', 'OM'],
            $client->getModePaiementDefaut()?->value ?? 'WAVE'
        );
        $modePaiement = ModePaiement::from($modePaiementChoice);

        $isTestPaiement = $io->confirm('Paiement de test ?', true);

        // ====== 8. GÉNÉRATION NUMÉRO DE COMMANDE ======
        $numCmd = $this->generateNumeroCommande();

        // ====== 9. CRÉATION DE LA COMMANDE ======
        $commande = new Commande();
        $commande->setNumCmd($numCmd);
        $commande->setMontant($montantTotal);
        $commande->setEtat(EtatCommande::EN_ATTENTE);
        $commande->setTypeRecuperation($modeRecuperation);
        $commande->setClient($client);
        $commande->setInfoLivraison($infoLivraison);
        $commande->setPanier($panier);

        // ====== 10. CRÉATION DU PAIEMENT ======
        $referencePaiement = $this->generateReferencePaiement($modePaiement);

        $paiement = new Paiement();
        $paiement->setMontantPaie($montantTotal);
        $paiement->setModePaie($modePaiement);
        $paiement->setReferencePaiementExterne($referencePaiement);
        $paiement->setTest($isTestPaiement);
        $paiement->setClient($client);
        $paiement->setCommande($commande);

        // ====== 11. PERSISTANCE ======
        try {
            $this->entityManager->persist($infoLivraison);
            $this->entityManager->persist($panier);

            foreach ($articlesQuantifies as $articleQuantifier) {
                $this->entityManager->persist($articleQuantifier);
            }

            $this->entityManager->persist($commande);
            $this->entityManager->persist($paiement);

            $this->entityManager->flush();

            $io->success('Commande créée avec succès !');

            // ====== 12. AFFICHAGE RÉCAPITULATIF FINAL ======
            $io->section('Détails de la commande');
            $io->table(
                ['Propriété', 'Valeur'],
                [
                    ['Numéro de commande', $commande->getNumCmd()],
                    ['Client', sprintf('%s %s (%s)', $client->getPrenom(), $client->getNom(), $client->getTelephone())],
                    ['Type', $panier->getCategoriePanier()->getLabel()],
                    ['Montant panier', $montantTotalPanier . ' FCFA'],
                    ['Frais de livraison', $prixLivraison . ' FCFA'],
                    ['Montant total', $montantTotal . ' FCFA'],
                    ['Mode de récupération', $modeRecuperation->getLabel()],
                    ['Mode de paiement', $modePaiement->getLabel()],
                    ['Référence paiement', $referencePaiement],
                    ['Test', $isTestPaiement ? 'Oui' : 'Non'],
                    ['État', $commande->getEtat()->getLabel()],
                    ['Date', $commande->getDateDebut()->format('d/m/Y H:i:s')],
                ]
            );

            if ($modeRecuperation === ModeRecuperation::LIVRER) {
                $io->section('Informations de livraison');
                $io->table(
                    ['Propriété', 'Valeur'],
                    [
                        ['Zone', $infoLivraison->getZone()->getNom()],
                        ['Quartier', $infoLivraison->getQuartier()->getNom()],
                        ['Prix livraison', $infoLivraison->getPrixLivraison() . ' FCFA'],
                        ['Note', $infoLivraison->getNoteLivraison() ?? 'Aucune'],
                    ]
                );
            }

            return Command::SUCCESS;

        } catch (\Exception $e) {
            $io->error('Erreur lors de la création de la commande: ' . $e->getMessage());
            return Command::FAILURE;
        }
    }

    private function selectClient(SymfonyStyle $io): ?Client
    {
        $clients = $this->entityManager->getRepository(Client::class)->findAll();

        if (empty($clients)) {
            return null;
        }

        $clientChoices = [];
        foreach ($clients as $client) {
            $clientChoices[$client->getId()] = sprintf(
                '%s %s - %s (%s)',
                $client->getPrenom(),
                $client->getNom(),
                $client->getTelephone(),
                $client->getLogin()
            );
        }

        $clientChoice = $io->choice('Sélectionnez un client', $clientChoices);
        $clientId = array_search($clientChoice, $clientChoices);

        return $this->entityManager->getRepository(Client::class)->find($clientId);
    }

    private function selectMenu(SymfonyStyle $io): ?Menu
    {
        $menus = $this->entityManager->getRepository(Menu::class)->findBy(['estArchiver' => false]);

        if (empty($menus)) {
            return null;
        }

        $menuChoices = [];
        foreach ($menus as $menu) {
            $menuChoices[$menu->getId()] = sprintf(
                '%s - %s - %d FCFA',
                $menu->getCode(),
                $menu->getLibelle(),
                $menu->getPrix()
            );
        }

        $menuChoice = $io->choice('Sélectionnez un menu', $menuChoices);
        $menuId = array_search($menuChoice, $menuChoices);

        return $this->entityManager->getRepository(Menu::class)->find($menuId);
    }

    private function selectBurger(SymfonyStyle $io): ?Burger
    {
        $burgers = $this->entityManager->getRepository(Burger::class)->findBy(['estArchiver' => false]);

        if (empty($burgers)) {
            return null;
        }

        $burgerChoices = [];
        foreach ($burgers as $burger) {
            $burgerChoices[$burger->getId()] = sprintf(
                '%s - %s - %d FCFA',
                $burger->getCode(),
                $burger->getLibelle(),
                $burger->getPrix()
            );
        }

        $burgerChoice = $io->choice('Sélectionnez un burger', $burgerChoices);
        $burgerId = array_search($burgerChoice, $burgerChoices);

        return $this->entityManager->getRepository(Burger::class)->find($burgerId);
    }

    private function addComplements(SymfonyStyle $io, Panier $panier, int $montantTotal, array $articlesQuantifies): array
    {
        $complements = $this->entityManager->getRepository(Complement::class)->findBy(['estArchiver' => false]);

        if (empty($complements)) {
            $io->warning('Aucun complément disponible.');
            return ['montant' => $montantTotal, 'articles' => $articlesQuantifies];
        }

        $io->section('Compléments disponibles');

        do {
            $complementChoices = ['Terminer'];
            $complementMap = [];

            foreach ($complements as $complement) {
                $key = sprintf(
                    '%s - %s - %d FCFA',
                    $complement->getCode(),
                    $complement->getLibelle(),
                    $complement->getPrix()
                );
                $complementChoices[] = $key;
                $complementMap[$key] = $complement;
            }

            $complementChoice = $io->choice('Ajouter un complément', $complementChoices, 'Terminer');

            if ($complementChoice === 'Terminer') {
                break;
            }

            $complement = $complementMap[$complementChoice];

            $quantite = (int) $io->ask(
                sprintf('Quantité de %s', $complement->getLibelle()),
                '1',
                function ($value) {
                    if (!is_numeric($value) || $value < 1) {
                        throw new \RuntimeException('La quantité doit être un nombre positif');
                    }
                    return $value;
                }
            );

            $prixComplement = $complement->getPrix();
            $montantComplement = $prixComplement * $quantite;
            $montantTotal += $montantComplement;

            $articleQuantifier = new ArticleQuantifier();
            $articleQuantifier->setArticle($complement);
            $articleQuantifier->setQuantite($quantite);
            $articleQuantifier->setMontant($montantComplement);
            $articleQuantifier->setCategorie(CategorieArticleQuantifier::COMMANDE);
            $articleQuantifier->setPanier($panier);

            $articlesQuantifies[] = $articleQuantifier;

            $io->info(sprintf('Complément ajouté: %s x%d = %d FCFA',
                $complement->getLibelle(),
                $quantite,
                $montantComplement
            ));

        } while (true);

        return ['montant' => $montantTotal, 'articles' => $articlesQuantifies];
    }

    private function createInfoLivraison(SymfonyStyle $io, Client $client): array
    {
        $io->section('Informations de livraison');

        // Sélection de la zone
        $zones = $this->entityManager->getRepository(Zone::class)->findBy(['estArchiver' => false]);

        if (empty($zones)) {
            throw new \RuntimeException('Aucune zone disponible. Créez au moins une zone.');
        }

        $zoneChoices = [];
        foreach ($zones as $zone) {
            $zoneChoices[$zone->getId()] = sprintf(
                '%s - %d FCFA',
                $zone->getNom(),
                $zone->getPrixLivraison()
            );
        }

        $zoneChoice = $io->choice('Sélectionnez une zone', $zoneChoices);
        $zoneId = array_search($zoneChoice, $zoneChoices);
        $zone = $this->entityManager->getRepository(Zone::class)->find($zoneId);

        // Sélection du quartier
        $quartiers = $zone->getQuartiers()->toArray();

        if (empty($quartiers)) {
            throw new \RuntimeException('Aucun quartier disponible dans cette zone.');
        }

        $quartierChoices = [];
        foreach ($quartiers as $quartier) {
            $quartierChoices[$quartier->getId()] = $quartier->getNom();
        }

        $quartierChoice = $io->choice('Sélectionnez un quartier', $quartierChoices);
        $quartierId = array_search($quartierChoice, $quartierChoices);
        $quartier = $this->entityManager->getRepository(Quartier::class)->find($quartierId);

        // Note de livraison
        $noteLivraison = $io->ask('Note de livraison (optionnel)', null);

        $infoLivraison = new InfoLivraison();
        $infoLivraison->setZone($zone);
        $infoLivraison->setQuartier($quartier);
        $infoLivraison->setPrixLivraison($zone->getPrixLivraison());

        if ($noteLivraison) {
            $infoLivraison->setNoteLivraison($noteLivraison);
        }

        return [
            'infoLivraison' => $infoLivraison,
            'prix' => $zone->getPrixLivraison()
        ];
    }

    private function generateNumeroCommande(): string
    {
        // Format: CMD-YYYYMMDD-XXXXXX (où X est un nombre aléatoire)
        $date = date('Ymd');
        $random = str_pad((string) random_int(1, 999999), 6, '0', STR_PAD_LEFT);

        $numCmd = sprintf('CMD-%s-%s', $date, $random);

        // Vérifier l'unicité
        $existing = $this->entityManager->getRepository(Commande::class)->findOneBy(['numCmd' => $numCmd]);

        if ($existing) {
            // Si par hasard le numéro existe déjà, on relance
            return $this->generateNumeroCommande();
        }

        return $numCmd;
    }

    private function generateReferencePaiement(ModePaiement $modePaiement): string
    {
        // Format: WAVE-YYYYMMDDHHMMSS-XXXX ou OM-YYYYMMDDHHMMSS-XXXX
        $prefix = $modePaiement->value;
        $datetime = date('YmdHis');
        $random = str_pad((string) random_int(1, 9999), 4, '0', STR_PAD_LEFT);

        $reference = sprintf('%s-%s-%s', $prefix, $datetime, $random);

        // Vérifier l'unicité
        $existing = $this->entityManager->getRepository(Paiement::class)
            ->findOneBy(['referencePaiementExterne' => $reference]);

        if ($existing) {
            return $this->generateReferencePaiement($modePaiement);
        }

        return $reference;
    }
}
