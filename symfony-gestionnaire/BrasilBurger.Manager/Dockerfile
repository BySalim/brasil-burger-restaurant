FROM php:8.4-fpm-alpine

# Installer les dépendances système et extensions PHP
RUN apk add --no-cache \
    postgresql-dev \
    nginx \
    supervisor \
    git \
    unzip \
    curl \
    && docker-php-ext-install pdo pdo_pgsql opcache

# Installer Composer
COPY --from=composer:latest /usr/bin/composer /usr/bin/composer

# Installer Node.js (pour importmap:install)
RUN apk add --no-cache nodejs npm

# Configuration PHP optimisée pour la production
RUN echo "opcache.enable=1" >> /usr/local/etc/php/conf.d/opcache.ini && \
    echo "opcache.memory_consumption=128" >> /usr/local/etc/php/conf.d/opcache.ini && \
    echo "opcache.interned_strings_buffer=8" >> /usr/local/etc/php/conf.d/opcache.ini && \
    echo "opcache.max_accelerated_files=10000" >> /usr/local/etc/php/conf.d/opcache.ini && \
    echo "opcache.validate_timestamps=0" >> /usr/local/etc/php/conf.d/opcache.ini && \
    echo "memory_limit=256M" >> /usr/local/etc/php/conf.d/memory.ini && \
    echo "max_execution_time=300" >> /usr/local/etc/php/conf.d/memory.ini

# Créer les répertoires Nginx et Supervisor
RUN mkdir -p /var/log/nginx /var/log/supervisor

# Définir le répertoire de travail
WORKDIR /app

# Copier les fichiers de configuration AVANT le code
COPY docker/nginx/default.conf /etc/nginx/http.d/default.conf
COPY docker/supervisor/supervisord.conf /etc/supervisor/conf.d/supervisord.conf

# Copier le code source
COPY . /app

# Variables d'environnement
ENV COMPOSER_ALLOW_SUPERUSER=1
ENV APP_ENV=prod

# Créer les répertoires var/ et ajuster les permissions
RUN mkdir -p /app/var/cache /app/var/log && \
    chmod -R 777 /app/var /var/log/nginx /var/log/supervisor

# Installer les dépendances Composer
RUN composer install --no-dev --optimize-autoloader --no-scripts --no-interaction

# ========== ASSET MAPPER INSTALLATION ==========
# Télécharger les assets importmap (Stimulus, etc.)
RUN php bin/console importmap:install --no-interaction || true

# Compiler les assets si AssetMapper nécessite
RUN php bin/console asset-map:compile 2>/dev/null || true

# ========== CACHE WARMUP ==========
# Warmer le cache
RUN php bin/console cache:warmup --env=prod || true

# ========== PERMISSIONS FINALES ==========
# Permissions finales pour www-data
RUN chown -R www-data:www-data /app/var /app/public /app/vendor

# Port Render
EXPOSE 8080

# Démarrer Supervisor
CMD ["/usr/bin/supervisord", "-c", "/etc/supervisor/conf.d/supervisord.conf"]
