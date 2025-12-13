package com.brasilburger.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Classe de configuration centralisee pour l'application
 * Charge les variables depuis .env puis depuis application.properties
 */
public class AppConfig {
    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);
    private static Properties properties;
    private static Dotenv dotenv;

    static {
        // 1.Charger le fichier .env (si present)
        try {
            dotenv = Dotenv.configure()
                    .directory(".")  // Cherche .env a la racine du projet
                    .ignoreIfMissing()  // Ne pas planter si .env absent
                    .load();
            logger.info("Fichier .env charge avec succes");
        } catch (Exception e) {
            logger.warn("Fichier .env non trouve, utilisation des variables systeme");
            dotenv = null;
        }

        // 2.Charger application.properties
        properties = new Properties();
        try (InputStream input = AppConfig.class.getClassLoader()
                .getResourceAsStream("application.properties")) {

            if (input == null) {
                logger.error("Impossible de trouver application.properties");
                throw new RuntimeException("Fichier application.properties introuvable");
            }

            properties.load(input);

            // Remplacer les placeholders ${VAR} par les variables d'environnement
            resolveEnvironmentVariables();

            logger.info("Configuration chargee avec succes");

        } catch (IOException e) {
            logger.error("Erreur lors du chargement de application.properties", e);
            throw new RuntimeException("Impossible de charger application.properties", e);
        }
    }

    /**
     * Recupere une variable d'environnement
     * Cherche d'abord dans .env, puis dans les variables systeme
     */
    private static String getEnvVariable(String name) {
        // 1.Chercher dans .env
        if (dotenv != null) {
            String value = dotenv.get(name);
            if (value != null) {
                return value;
            }
        }

        // 2.Chercher dans les variables systeme
        return System.getenv(name);
    }

    /**
     * Resout les variables d'environnement dans les proprietes
     * Format supporte:  ${VAR_NAME} ou ${VAR_NAME: default_value}
     */
    private static void resolveEnvironmentVariables() {
        properties.replaceAll((key, value) -> {
            String strValue = value.toString();

            // Rechercher les patterns ${... }
            while (strValue.contains("${") && strValue.contains("}")) {
                int startIdx = strValue.indexOf("${");
                int endIdx = strValue.indexOf("}", startIdx);

                if (endIdx == -1) break;

                String placeholder = strValue.substring(startIdx + 2, endIdx);
                String envVarName;
                String defaultValue = null;

                // Gerer le format ${VAR: default}
                if (placeholder.contains(":")) {
                    String[] parts = placeholder.split(":", 2);
                    envVarName = parts[0];
                    defaultValue = parts[1];
                } else {
                    envVarName = placeholder;
                }

                // Recuperer la variable d'environnement
                String envValue = getEnvVariable(envVarName);

                if (envValue != null) {
                    strValue = strValue.replace("${" + placeholder + "}", envValue);
                } else if (defaultValue != null) {
                    strValue = strValue.replace("${" + placeholder + "}", defaultValue);
                    logger.debug("Variable d'environnement {} non trouvee, utilisation de la valeur par defaut", envVarName);
                } else {
                    logger.warn("Variable d'environnement {} non trouvee et aucune valeur par defaut", envVarName);
                }
            }

            return strValue;
        });
    }

    /**
     * Recupere une propriete par sa cle
     */
    public static String get(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            logger.warn("Propriete non trouvee: {}", key);
        }
        return value;
    }

    /**
     * Recupere une propriete avec valeur par defaut
     */
    public static String get(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * Recupere une propriete de type entier
     */
    public static int getInt(String key, int defaultValue) {
        String value = get(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            logger.warn("Impossible de convertir {} en entier, utilisation de la valeur par defaut:  {}", key, defaultValue);
            return defaultValue;
        }
    }

    /**
     * Verifie si toutes les variables d'environnement obligatoires sont definies
     */
    public static boolean validateConfiguration() {
        boolean isValid = true;

        String[] requiredVars = {
                "db.url",
                "db.username",
                "db.password",
                "cloudinary.cloud_name",
                "cloudinary.api_key",
                "cloudinary.api_secret"
        };

        for (String var : requiredVars) {
            String value = get(var);
            if (value == null || value.startsWith("${")) {
                logger.error("Configuration manquante ou invalide:  {}", var);
                isValid = false;
            }
        }

        return isValid;
    }

    // ===================================
    // Methodes d'acces rapide - Application
    // ===================================

    public static String getAppName() {
        return get("app.name", "Brasil Burger Manager");
    }

    public static String getAppVersion() {
        return get("app.version", "1.0.0");
    }

    public static String getAppEnvironment() {
        return get("app.environment", "production");
    }

    // ===================================
    // Methodes d'acces rapide - Base de donnees
    // ===================================

    public static String getDatabaseUrl() {
        return get("db.url");
    }

    public static String getDatabaseUsername() {
        return get("db.username");
    }

    public static String getDatabasePassword() {
        return get("db.password");
    }

    public static String getDatabaseDriver() {
        return get("db.driver", "org.postgresql.Driver");
    }

    public static int getDatabasePoolSize() {
        return getInt("db.pool.size", 10);
    }

    public static int getDatabasePoolTimeout() {
        return getInt("db.pool.timeout", 30000);
    }

    public static int getDatabasePoolMaxLifetime() {
        return getInt("db.pool.max-lifetime", 1800000);
    }

    // ===================================
    // Methodes d'acces rapide - Cloudinary
    // ===================================

    public static String getCloudinaryCloudName() {
        return get("cloudinary.cloud_name");
    }

    public static String getCloudinaryApiKey() {
        return get("cloudinary.api_key");
    }

    public static String getCloudinaryApiSecret() {
        return get("cloudinary.api_secret");
    }

    public static String getCloudinaryArticlesFolder() {
        return get("cloudinary.folder.articles", "brasil-burger/articles");
    }

    // ===================================
    // Methodes d'acces rapide - Images
    // ===================================

    public static long getImageMaxSize() {
        return Long.parseLong(get("image.max.size", "5242880"));
    }

    public static String[] getImageAllowedFormats() {
        String formats = get("image.allowed.formats", "jpg,jpeg,png,webp");
        return formats.split(",");
    }
}