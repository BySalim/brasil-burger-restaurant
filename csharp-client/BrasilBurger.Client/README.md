# Brasil Burger - Interface Client Web

Application web de commande et livraison de burgers pour Brasil Burger Sénégal.

## 🚀 Technologies

- ASP.NET Core 9.0
- Entity Framework Core (PostgreSQL via Neon)
- Cloudinary (stockage images)
- Wave & Orange Money (paiement mobile)

## ⚙️ Configuration

1. Copier .env.example vers .env
2. Remplir les variables d'environnement
3. Exécuter les migrations : dotnet ef database update
4. Lancer l'application : dotnet run --project src/BrasilBurger.Web

## 🚀 Déploiement sur Render

1. Pousser le code sur un repository GitHub.
2. Se connecter à [Render](https://render.com) et créer un nouveau service web.
3. Sélectionner le repository GitHub.
4. Choisir "Docker" comme runtime (Render détectera automatiquement le Dockerfile).
5. Configurer les variables d'environnement dans le dashboard Render (utiliser les valeurs de votre .env, sans les committer).
6. Déployer.

Le fichier `render.yaml` est fourni pour une configuration avancée si nécessaire.

## 📂 Architecture

- **Domain** : Entités métier
- **Application** : Use cases
- **Infrastructure** : Implémentations techniques
- **Web** : Interface utilisateur

## 🎓 Projet Académique

Réalisé dans le cadre du cours de développement web.
