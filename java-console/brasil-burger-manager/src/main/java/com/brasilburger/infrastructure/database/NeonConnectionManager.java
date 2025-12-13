package com.brasilburger.infrastructure.database;

import com.brasilburger.config.AppConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Gestionnaire de connexions a la base de donnees Neon (PostgreSQL)
 * Utilise HikariCP pour le pool de connexions
 */
public class NeonConnectionManager {
    private static final Logger logger = LoggerFactory.getLogger(NeonConnectionManager.class);
    private static HikariDataSource dataSource;

    static {
        try {
            initializeDataSource();
            logger.info("Pool de connexions initialise avec succes");
        } catch (Exception e) {
            logger.error("Erreur lors de l'initialisation du pool de connexions", e);
            throw new RuntimeException("Impossible d'initialiser la connexion a la base de donnees", e);
        }
    }

    /**
     * Initialise le pool de connexions HikariCP
     */
    private static void initializeDataSource() {
        HikariConfig config = new HikariConfig();

        // Configuration de la connexion
        config.setJdbcUrl(AppConfig.getDatabaseUrl());
        config.setUsername(AppConfig.getDatabaseUsername());
        config.setPassword(AppConfig.getDatabasePassword());
        config.setDriverClassName(AppConfig.getDatabaseDriver());

        // Configuration du pool
        config.setMaximumPoolSize(AppConfig.getDatabasePoolSize());
        config.setConnectionTimeout(AppConfig.getDatabasePoolTimeout());
        config.setMaxLifetime(AppConfig.getDatabasePoolMaxLifetime());
        config.setMinimumIdle(2);

        // Proprietes PostgreSQL
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        // Nom du pool
        config.setPoolName("BrasilBurger-Pool");

        // Creation du DataSource
        dataSource = new HikariDataSource(config);

        logger.info("Configuration HikariCP:");
        logger.info("  - URL: {}", maskPassword(AppConfig.getDatabaseUrl()));
        logger.info("  - Username: {}", AppConfig.getDatabaseUsername());
        logger.info("  - Pool Size: {}", AppConfig.getDatabasePoolSize());
        logger.info("  - Timeout: {}ms", AppConfig.getDatabasePoolTimeout());
    }

    /**
     * Obtient une connexion depuis le pool
     */
    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("Le pool de connexions n'est pas initialise");
        }
        return dataSource.getConnection();
    }

    /**
     * Teste la connexion a la base de donnees
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            boolean isValid = conn.isValid(5);
            if (isValid) {
                logger.info("Test de connexion reussi");
            } else {
                logger.error("Test de connexion echoue");
            }
            return isValid;
        } catch (SQLException e) {
            logger.error("Erreur lors du test de connexion", e);
            return false;
        }
    }

    /**
     * Obtient des informations sur le pool de connexions
     */
    public static String getPoolInfo() {
        if (dataSource == null) {
            return "Pool non initialise";
        }

        return String.format(
                "Pool Info - Actives: %d, Idle: %d, En attente: %d, Total: %d",
                dataSource.getHikariPoolMXBean().getActiveConnections(),
                dataSource.getHikariPoolMXBean().getIdleConnections(),
                dataSource.getHikariPoolMXBean().getThreadsAwaitingConnection(),
                dataSource.getHikariPoolMXBean().getTotalConnections()
        );
    }

    /**
     * Ferme le pool de connexions
     */
    public static void shutdown() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            logger.info("Pool de connexions ferme");
        }
    }

    /**
     * Masque le mot de passe dans l'URL pour les logs
     */
    private static String maskPassword(String url) {
        if (url == null) return "null";
        if (url.contains("password=")) {
            return url.replaceAll("password=[^&]*", "password=****");
        }
        return url;
    }

    /**
     * Empeche l'instantiation
     */
    private NeonConnectionManager() {
        throw new UnsupportedOperationException("Classe utilitaire - pas d'instantiation");
    }
}