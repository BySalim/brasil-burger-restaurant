<?php

namespace App\DataFixtures;

use App\Entity\Client;
use App\Entity\Gestionnaire;
use Doctrine\Bundle\FixturesBundle\Fixture;
use Doctrine\Persistence\ObjectManager;
use Symfony\Component\PasswordHasher\Hasher\UserPasswordHasherInterface;

class UtilisateurFixtures extends Fixture
{
    public function __construct(
        private UserPasswordHasherInterface $passwordHasher
    ) {
    }

    public function load(ObjectManager $manager): void
    {
        // Création de 2 gestionnaires
        $gestionnaire1 = new Gestionnaire();
        $gestionnaire1->setNom('Admin');
        $gestionnaire1->setPrenom('Super');
        $gestionnaire1->setLogin('admin@exemple.com');
        $hashedPassword = $this->passwordHasher->hashPassword($gestionnaire1, 'admin123');
        $gestionnaire1->setMotDePasse($hashedPassword);
        $manager->persist($gestionnaire1);

        $gestionnaire2 = new Gestionnaire();
        $gestionnaire2->setNom('Diop');
        $gestionnaire2->setPrenom('Fatou');
        $gestionnaire2->setLogin('fatou.diop@exemple.com');
        $hashedPassword = $this->passwordHasher->hashPassword($gestionnaire2, 'password123');
        $gestionnaire2->setMotDePasse($hashedPassword);
        $manager->persist($gestionnaire2);

        // Création de 3 clients
        $client1 = new Client();
        $client1->setNom('Ndiaye');
        $client1->setPrenom('Moussa');
        $client1->setLogin('moussa.ndiaye@exemple.com');
        $client1->setTelephone('771234567');
        $hashedPassword = $this->passwordHasher->hashPassword($client1, 'client123');
        $client1->setMotDePasse($hashedPassword);
        $manager->persist($client1);

        $client2 = new Client();
        $client2->setNom('Fall');
        $client2->setPrenom('Aminata');
        $client2->setLogin('aminata.fall@exemple.com');
        $client2->setTelephone('776543210');
        $hashedPassword = $this->passwordHasher->hashPassword($client2, 'client123');
        $client2->setMotDePasse($hashedPassword);
        $manager->persist($client2);

        $client3 = new Client();
        $client3->setNom('Sow');
        $client3->setPrenom('Ibrahima');
        $client3->setLogin('ibrahima.sow@exemple.com');
        $client3->setTelephone('778901234');
        $hashedPassword = $this->passwordHasher->hashPassword($client3, 'client123');
        $client3->setMotDePasse($hashedPassword);
        $manager->persist($client3);

        $manager->flush();
    }
}
