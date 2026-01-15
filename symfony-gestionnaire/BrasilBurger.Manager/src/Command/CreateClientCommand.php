<?php

namespace App\Command;

use App\Entity\Client;
use App\Entity\Quartier;
use App\Enum\ModePaiement;
use App\Enum\ModeRecuperation;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Component\Console\Attribute\AsCommand;
use Symfony\Component\Console\Command\Command;
use Symfony\Component\Console\Input\InputInterface;
use Symfony\Component\Console\Output\OutputInterface;
use Symfony\Component\Console\Style\SymfonyStyle;
use Symfony\Component\PasswordHasher\Hasher\UserPasswordHasherInterface;

#[AsCommand(
    name: 'app:create-client',
    description: 'Créer un compte client',
)]
class CreateClientCommand extends Command
{
    public function __construct(
        private EntityManagerInterface $entityManager,
        private UserPasswordHasherInterface $passwordHasher
    ) {
        parent::__construct();
    }

    protected function execute(InputInterface $input, OutputInterface $output): int
    {
        $io = new SymfonyStyle($input, $output);

        $io->title('Création d\'un nouveau client');

        // Informations de base
        $nom = $io->ask('Nom', null, function ($value) {
            if (empty($value)) {
                throw new \RuntimeException('Le nom ne peut pas être vide');
            }
            return $value;
        });

        $prenom = $io->ask('Prénom', null, function ($value) {
            if (empty($value)) {
                throw new \RuntimeException('Le prénom ne peut pas être vide');
            }
            return $value;
        });

        $login = $io->ask('Login', null, function ($value) {
            if (empty($value)) {
                throw new \RuntimeException('Le login ne peut pas être vide');
            }

            // Vérifier si le login existe déjà
            $existingUser = $this->entityManager
                ->getRepository(Client::class)
                ->findOneBy(['login' => $value]);

            if ($existingUser) {
                throw new \RuntimeException('Ce login existe déjà');
            }

            return $value;
        });

        $password = $io->askHidden('Mot de passe', function ($value) {
            if (empty($value)) {
                throw new \RuntimeException('Le mot de passe ne peut pas être vide');
            }
            if (strlen($value) < 6) {
                throw new \RuntimeException('Le mot de passe doit contenir au moins 6 caractères');
            }
            return $value;
        });

        $telephone = $io->ask('Téléphone (obligatoire)', null, function ($value) {
            if (empty($value)) {
                throw new \RuntimeException('Le téléphone est obligatoire pour un client');
            }

            // Vérifier si le téléphone existe déjà
            $existingClient = $this->entityManager
                ->getRepository(Client::class)
                ->findOneBy(['telephone' => $value]);

            if ($existingClient) {
                throw new \RuntimeException('Ce numéro de téléphone est déjà utilisé');
            }

            return $value;
        });

        // Paramètres optionnels par défaut
        $io->section('Paramètres par défaut (optionnels)');

        // Quartier de livraison par défaut
        $quartierLivraisonDefaut = null;
        if ($io->confirm('Définir un quartier de livraison par défaut ?', false)) {
            $quartiers = $this->entityManager->getRepository(Quartier::class)->findAll();

            if (empty($quartiers)) {
                $io->warning('Aucun quartier disponible dans la base de données');
            } else {
                $quartierChoices = [];
                foreach ($quartiers as $quartier) {
                    $quartierChoices[$quartier->getId()] = sprintf(
                        '%s (Zone: %s)',
                        $quartier->getNom(),
                        $quartier->getZone()->getNom()
                    );
                }

                $quartierIdChoisi = $io->choice('Sélectionnez un quartier', $quartierChoices);
                $quartierLivraisonDefaut = $this->entityManager
                    ->getRepository(Quartier::class)
                    ->find(array_search($quartierIdChoisi, $quartierChoices));
            }
        }

        // Mode de paiement par défaut
        $modePaiementDefaut = null;
        if ($io->confirm('Définir un mode de paiement par défaut ?', false)) {
            $modePaiementChoice = $io->choice(
                'Mode de paiement',
                ['WAVE', 'OM']
            );
            $modePaiementDefaut = ModePaiement::from($modePaiementChoice);
        }

        // Mode de récupération par défaut
        $modeRecuperationDefaut = null;
        if ($io->confirm('Définir un mode de récupération par défaut ?', false)) {
            $modeRecuperationChoice = $io->choice(
                'Mode de récupération',
                ['SUR_PLACE', 'EMPORTER', 'LIVRER']
            );
            $modeRecuperationDefaut = ModeRecuperation::from($modeRecuperationChoice);
        }

        // Création du client
        $client = new Client();
        $client->setNom($nom);
        $client->setPrenom($prenom);
        $client->setLogin($login);
        $client->setTelephone($telephone);

        $hashedPassword = $this->passwordHasher->hashPassword($client, $password);
        $client->setMotDePasse($hashedPassword);

        if ($quartierLivraisonDefaut) {
            $client->setQuartierLivraisonDefaut($quartierLivraisonDefaut);
        }
        if ($modePaiementDefaut) {
            $client->setModePaiementDefaut($modePaiementDefaut);
        }
        if ($modeRecuperationDefaut) {
            $client->setModeRecuperationDefaut($modeRecuperationDefaut);
        }

        // Sauvegarde
        $this->entityManager->persist($client);
        $this->entityManager->flush();

        $io->success('Client créé avec succès !');

        // Affichage récapitulatif
        $io->table(
            ['Propriété', 'Valeur'],
            [
                ['ID', $client->getId()],
                ['Nom', $client->getNom()],
                ['Prénom', $client->getPrenom()],
                ['Login', $client->getLogin()],
                ['Téléphone', $client->getTelephone()],
                ['Quartier par défaut', $quartierLivraisonDefaut ? $quartierLivraisonDefaut->getNom() : 'Non défini'],
                ['Mode paiement par défaut', $modePaiementDefaut?->value ?? 'Non défini'],
                ['Mode récupération par défaut', $modeRecuperationDefaut?->value ?? 'Non défini'],
            ]
        );

        return Command::SUCCESS;
    }
}
