<?php

namespace App\Command;

use App\Entity\Gestionnaire;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Component\Console\Attribute\AsCommand;
use Symfony\Component\Console\Command\Command;
use Symfony\Component\Console\Input\InputInterface;
use Symfony\Component\Console\Output\OutputInterface;
use Symfony\Component\Console\Style\SymfonyStyle;
use Symfony\Component\PasswordHasher\Hasher\UserPasswordHasherInterface;

#[AsCommand(
    name: 'app:create-gestionnaire',
    description: 'Créer un compte gestionnaire',
)]
class CreateGestionnaireCommand extends Command
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

        $nom = $io->ask('Nom');
        $prenom = $io->ask('Prénom');
        $login = $io->ask('Login');
        $password = $io->askHidden('Mot de passe');

        $gestionnaire = new Gestionnaire();
        $gestionnaire->setNom($nom);
        $gestionnaire->setPrenom($prenom);
        $gestionnaire->setLogin($login);

        $hashedPassword = $this->passwordHasher->hashPassword($gestionnaire, $password);
        $gestionnaire->setMotDePasse($hashedPassword);

        $this->entityManager->persist($gestionnaire);
        $this->entityManager->flush();

        $io->success('Gestionnaire créé avec succès !');

        return Command::SUCCESS;
    }
}
