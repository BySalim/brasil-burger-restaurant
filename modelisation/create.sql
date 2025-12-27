-- ============================================
-- Script PostgreSQL - Brasil Burger
-- Gestion des commandes et livraisons
-- VERSION FINALE - Création uniquement
-- ============================================

-- Extension pour UUID (si nécessaire)
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ============================================
-- TABLE:  ZONE
-- ============================================
CREATE TABLE IF NOT EXISTS zone (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) UNIQUE NOT NULL,
    prix_livraison INTEGER NOT NULL,
    est_archiver BOOLEAN DEFAULT false
);

-- ============================================
-- TABLE: QUARTIER
-- ============================================
CREATE TABLE IF NOT EXISTS quartier (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) UNIQUE NOT NULL,
    id_zone INTEGER NOT NULL,
    
    -- Clé étrangère
    CONSTRAINT fk_quartier_zone 
        FOREIGN KEY (id_zone) 
        REFERENCES zone(id) ON DELETE CASCADE
);

-- ============================================
-- TABLE: UTILISATEUR (Clients et Gestionnaires)
-- ============================================
CREATE TABLE IF NOT EXISTS utilisateur (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    login VARCHAR(100) UNIQUE NOT NULL,
    mot_de_passe VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('CLIENT', 'GESTIONNAIRE')),
    telephone VARCHAR(20) UNIQUE, -- NULL pour gestionnaire, obligatoire pour client
    id_quartier_livraison_defaut INTEGER,
    mode_paiement_defaut VARCHAR(20) CHECK (mode_paiement_defaut IN ('WAVE', 'OM')),
    mode_recuperation_defaut VARCHAR(20) CHECK (mode_recuperation_defaut IN ('SUR_PLACE', 'EMPORTER', 'LIVRER')),
    
    -- Clé étrangère
    CONSTRAINT fk_utilisateur_quartier_livraison_defaut 
        FOREIGN KEY (id_quartier_livraison_defaut) 
        REFERENCES quartier(id) ON DELETE SET NULL,
    
    -- Contrainte :  téléphone obligatoire pour les clients
    CONSTRAINT chk_telephone_client 
        CHECK (role = 'GESTIONNAIRE' OR (role = 'CLIENT' AND telephone IS NOT NULL))
);

-- ============================================
-- TABLE: LIVREUR (Entité séparée)
-- ============================================
CREATE TABLE IF NOT EXISTS livreur (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    telephone VARCHAR(20) UNIQUE NOT NULL,
    est_archiver BOOLEAN DEFAULT false,
    est_disponible BOOLEAN DEFAULT true
);

-- ============================================
-- TABLE:  ARTICLE
-- ============================================
CREATE TABLE IF NOT EXISTS article (
    id SERIAL PRIMARY KEY,
    code VARCHAR(50) UNIQUE NOT NULL,
    libelle VARCHAR(150) NOT NULL,
    image_public_id TEXT NOT NULL, -- Cloudinary public_id ou chemin d'image
    est_archiver BOOLEAN DEFAULT false,
    categorie VARCHAR(20) NOT NULL CHECK (categorie IN ('BURGER', 'MENU', 'COMPLEMENT')),
    description TEXT,
    prix INTEGER,
    type_complement VARCHAR(20) CHECK (type_complement IN ('BOISSON', 'FRITES'))
);

-- ============================================
-- TABLE: INFO_LIVRAISON
-- ============================================
CREATE TABLE IF NOT EXISTS info_livraison (
    id SERIAL PRIMARY KEY,
    id_zone INTEGER NOT NULL,
    id_quartier INTEGER NOT NULL,
    note_livraison TEXT,
    prix_livraison INTEGER NOT NULL,
    
    -- Clés étrangères
    CONSTRAINT fk_info_livraison_zone 
        FOREIGN KEY (id_zone) 
        REFERENCES zone(id) ON DELETE CASCADE,
    CONSTRAINT fk_info_livraison_quartier 
        FOREIGN KEY (id_quartier) 
        REFERENCES quartier(id) ON DELETE CASCADE
);

-- ============================================
-- TABLE: GROUPE_LIVRAISON
-- ============================================
CREATE TABLE IF NOT EXISTS groupe_livraison (
    id SERIAL PRIMARY KEY,
    id_livreur INTEGER NOT NULL,
    statut VARCHAR(20) NOT NULL DEFAULT 'EN_COURS' 
        CHECK (statut IN ('EN_COURS', 'TERMINER')),
    
    -- Clé étrangère
    CONSTRAINT fk_groupe_livraison_livreur 
        FOREIGN KEY (id_livreur) 
        REFERENCES livreur(id) ON DELETE CASCADE
);

-- ============================================
-- TABLE: LIVRAISON
-- ============================================
CREATE TABLE IF NOT EXISTS livraison (
    id SERIAL PRIMARY KEY,
    statut VARCHAR(20) NOT NULL DEFAULT 'EN_COURS' 
        CHECK (statut IN ('EN_COURS', 'TERMINER')),
    date_debut DATE NOT NULL DEFAULT CURRENT_DATE,
    date_fin DATE,
    id_groupe_livraison INTEGER NOT NULL,
    
    -- Clé étrangère
    CONSTRAINT fk_livraison_groupe 
        FOREIGN KEY (id_groupe_livraison) 
        REFERENCES groupe_livraison(id) ON DELETE CASCADE
);

-- ============================================
-- TABLE:  PANIER
-- ============================================
CREATE TABLE IF NOT EXISTS panier (
    id SERIAL PRIMARY KEY,
    montant_total INTEGER NOT NULL DEFAULT 0,
    categorie_panier VARCHAR(20) NOT NULL CHECK (categorie_panier IN ('BURGER', 'MENU')),
);

-- ============================================
-- TABLE: COMMANDE
-- ============================================
CREATE TABLE IF NOT EXISTS commande (
    id SERIAL PRIMARY KEY,
    num_cmd VARCHAR(50) UNIQUE NOT NULL,
    date_debut DATE NOT NULL DEFAULT CURRENT_DATE,
    date_fin DATE,
    montant INTEGER NOT NULL,
    etat VARCHAR(20) NOT NULL DEFAULT 'EN_ATTENTE' 
        CHECK (etat IN ('EN_ATTENTE', 'EN_PREPARATION', 'TERMINER', 'ANNULER')),
    type_recuperation VARCHAR(20) NOT NULL 
        CHECK (type_recuperation IN ('SUR_PLACE', 'EMPORTER', 'LIVRER')),
    id_panier INTEGER,
    id_client INTEGER NOT NULL,
    id_info_livraison INTEGER UNIQUE NOT NULL,
    id_livraison INTEGER,
    
    -- Clés étrangères
    CONSTRAINT fk_commande_panier 
        FOREIGN KEY (id_panier) 
        REFERENCES panier(id) ON DELETE SET NULL,
    CONSTRAINT fk_commande_client 
        FOREIGN KEY (id_client) 
        REFERENCES utilisateur(id) ON DELETE CASCADE,
    CONSTRAINT fk_commande_info_livraison 
        FOREIGN KEY (id_info_livraison) 
        REFERENCES info_livraison(id) ON DELETE CASCADE,
    CONSTRAINT fk_commande_livraison 
        FOREIGN KEY (id_livraison) 
        REFERENCES livraison(id) ON DELETE SET NULL
);

-- ============================================
-- TABLE: PAIEMENT
-- ============================================
CREATE TABLE IF NOT EXISTS paiement (
    id SERIAL PRIMARY KEY,
    date_paie DATE NOT NULL DEFAULT CURRENT_DATE,
    montant_paie INTEGER NOT NULL,
    mode_paie VARCHAR(20) NOT NULL CHECK (mode_paie IN ('WAVE', 'OM')),
    reference_paiement_externe VARCHAR(100) UNIQUE,
    test BOOLEAN NOT NULL DEFAULT FALSE,
    id_client INTEGER,
    id_commande INTEGER UNIQUE NOT NULL,
    
    -- Clés étrangères
    CONSTRAINT fk_paiement_utilisateur 
        FOREIGN KEY (id_client) 
        REFERENCES utilisateur(id) ON DELETE SET NULL,
    CONSTRAINT fk_paiement_commande 
        FOREIGN KEY (id_commande) 
        REFERENCES commande(id) ON DELETE CASCADE
);

-- ============================================
-- TABLE: ARTICLE_QUANTIFIER
-- ============================================
CREATE TABLE IF NOT EXISTS article_quantifier (
    id SERIAL PRIMARY KEY,
    quantite INTEGER NOT NULL DEFAULT 1,
    montant INTEGER NOT NULL,
    categorie_article_quantifier VARCHAR(20) NOT NULL 
        CHECK (categorie_article_quantifier IN ('MENU', 'COMMANDE')),
    id_menu INTEGER,
    id_panier INTEGER,
    id_article INTEGER,
    
    -- Clés étrangères
    CONSTRAINT fk_article_quantifier_menu 
        FOREIGN KEY (id_menu) 
        REFERENCES article(id) ON DELETE CASCADE,
    CONSTRAINT fk_article_quantifier_panier 
        FOREIGN KEY (id_panier) 
        REFERENCES panier(id) ON DELETE CASCADE,
    CONSTRAINT fk_article_quantifier_article 
        FOREIGN KEY (id_article) 
        REFERENCES article(id) ON DELETE CASCADE
);

-- ============================================
-- CRÉATION DES INDEX
-- ============================================

-- Index sur commande
CREATE INDEX IF NOT EXISTS idx_commande_tel_client ON commande(tel_client);
CREATE INDEX IF NOT EXISTS idx_commande_date_debut ON commande(date_debut);
CREATE INDEX IF NOT EXISTS idx_commande_etat ON commande(etat);
CREATE INDEX IF NOT EXISTS idx_commande_num_cmd ON commande(num_cmd);
CREATE INDEX IF NOT EXISTS idx_commande_zone ON commande(id_zone_livraison);
CREATE INDEX IF NOT EXISTS idx_commande_livraison ON commande(id_livraison);

-- Index sur paiement
CREATE INDEX IF NOT EXISTS idx_paiement_commande ON paiement(id_commande);
CREATE INDEX IF NOT EXISTS idx_paiement_client ON paiement(id_client);
CREATE INDEX IF NOT EXISTS idx_paiement_date ON paiement(date_paie);

-- Index sur article
CREATE INDEX IF NOT EXISTS idx_article_categorie ON article(categorie);
CREATE INDEX IF NOT EXISTS idx_article_code ON article(code);
CREATE INDEX IF NOT EXISTS idx_article_archiver ON article(est_archiver);

-- Index sur utilisateur
CREATE INDEX IF NOT EXISTS idx_utilisateur_role ON utilisateur(role);
CREATE INDEX IF NOT EXISTS idx_utilisateur_telephone ON utilisateur(telephone);
CREATE INDEX IF NOT EXISTS idx_utilisateur_login ON utilisateur(login);

-- Index sur livraison
CREATE INDEX IF NOT EXISTS idx_livraison_livreur ON livraison(id_livreur);
CREATE INDEX IF NOT EXISTS idx_livraison_zone ON livraison(id_zone_livraison);
CREATE INDEX IF NOT EXISTS idx_livraison_statut ON livraison(statut);
CREATE INDEX IF NOT EXISTS idx_livraison_date ON livraison(date_debut);

-- Index sur quartier
CREATE INDEX IF NOT EXISTS idx_quartier_zone ON quartier(id_zone);

-- Index sur article_quantifier
CREATE INDEX IF NOT EXISTS idx_article_quantifier_panier ON article_quantifier(id_panier);
CREATE INDEX IF NOT EXISTS idx_article_quantifier_menu ON article_quantifier(id_menu);
CREATE INDEX IF NOT EXISTS idx_article_quantifier_article ON article_quantifier(id_article);

-- Index sur panier
CREATE INDEX IF NOT EXISTS idx_panier_commande ON panier(id_commande);

-- ============================================
-- FIN DU SCRIPT
-- ============================================