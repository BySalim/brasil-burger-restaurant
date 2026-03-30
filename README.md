# Brasil Burger Restaurant

Mon travail pour l'examen de Génie Logiciel Réseaux et Systèmes en L3 Semestre 1 à l'ISM consiste en ce projet. Ce projet consiste à créer une application de gestion des commandes et livraisons destinée au restaurant Brasil Burger, spécialiste en vente de burger.



## Technologies

- **Modélisation** : Diagrammes UML (Use case, Classes, Séquence), prototypes Figma, Modèle Logique de Données (ML, script SQL pour la base de données.
- **Client** : Utilise C# ASP.NET MVC.
- **Ressources** : Java Console (burgers, menus, accompagnements).
- **Administrateur** : Symfony (commandes, suivi, statistiques).

Tous partagent la même BD.


## Branches Git

- [`main`](https://github.com/BySalim/brasil-burger-restaurant/tree/main) - Branche principale
- [`modelisation`](https://github.com/BySalim/brasil-burger-restaurant/tree/modelisation) - Modélisation du projet
- [`java`](https://github.com/BySalim/brasil-burger-restaurant/tree/java) - Implémentation Java
- [`csharp`](https://github.com/BySalim/brasil-burger-restaurant/tree/csharp) - Implémentation C#
- [`symfony`](https://github.com/BySalim/brasil-burger-restaurant/tree/symfony) - Implémentation Symfony/PHP


📂 **GitHub :** [github.com/DevByDelta/brasil-burger-restaurant](https://github.com/DevByDelta/brasil-burger-restaurant)


## Context
```
Le Restaurant  Brasil Burger fait appel à vous pour la réalisation d’une application web et mobile  de gestion des commandes et livraisons. 
Le Restaurant  Brasil Burger est un spécialiste dans la vente de burger.Les burgers sont vendus en menu(Burger +boisson+frites) ou simple(burger).Ce restaurant offre aussi des compléments(frites ou boissons. 
Le Gestionnaire a la possibilité d'ajouter, modifier ou d’archiver les burgers(nom,prix, image) , les menus(nom, image) et les compléments(nom, images ,prix) . Il peut aussi lister les commandes faites par un client, annuler une commande d’un client(nom,prénom,téléphone). 
Le prix d’un menu est la somme des prix qui composent ce menu. 
Un client a la possibilité à partir d’une application mobile de voir le catalogue de burgers, de menus  disponibles, voir les détails d’un burger, d’un menu , commander un menu ou un burger et suivre ses commandes .  
Lorsque le client commande un burger, on lui propose des compléments et il devra  préciser si la commande est  à consommer sur place, à récupérer par le client ou à livrer .La commande devra être payée pour être valide. 
Lors du processus de commande , le client devra se connecter ou  créer un compte. 
Pour suivre l'état de ses commandes , le client devra se connecter. 
Les fonctionnalités du gestionnaire sont accessibles après une connexion. 
Lorsque la commande est prête ,le gestionnaire change l'état de la commande à Terminer . 
Lorsque la commande doit être livrée, le Gestionnaire doit les regrouper par zone et les affecter à un livreur.Une zone couvre des quartiers et à un prix. 
Le client peut payer sa commande à partir  de l’application mobile.
Le paiement(date,montant) de la commande  est enregistré.Le paiement peut se faire par Wave ou OM. 
Une commande est payée une est une seule fois 
Un client  peut filtrer le catalogue par menu, burger . 
Un Gestionnaire peut filtrer les commande par burger ou menu , par date, par état et par client 
On voudrais avoir les statiques suivantes: 
●  Les commandes en cours de la journée ,     
●  Les commandes Validés de la journée , 
●  Les Recettes Journalières 
●  Les Burger au menu les plus vendus  de journée 
●  Les Commandes annulées du Jour 
```
