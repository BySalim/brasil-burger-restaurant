# Dockerfile

FROM php:8.4-fpm-alpine

# Installer les dépendances système et extensions PHP
RUN apk add --no-cache \
    postgresql-dev \
    nginx \
    supervisor \
    git \
    unzip \
    && docker-php-ext-install pdo pdo_pgsql opcache

# Installer Composer
COPY --from=composer:latest /usr/bin/composer /usr/bin/composer

# Configuration PHP optimisée pour la production
RUN echo "opcache.enable=1" >> /usr/local/etc/php/conf.d/opcache.ini && \
    echo "opcache.memory_consumption=128" >> /usr/local/etc/php/conf.d/opcache.ini && \
    echo "opcache.interned_strings_buffer=8" >> /usr/local/etc/php/conf.d/opcache.ini && \
    echo "opcache.max_accelerated_files=10000" >> /usr/local/etc/php/conf.d/opcache.ini && \
    echo "opcache.validate_timestamps=0" >> /usr/local/etc/php/conf.d/opcache.ini && \
    echo "memory_limit=256M" >> /usr/local/etc/php/conf.d/memory.ini

# Définir le répertoire de travail
WORKDIR /app

# Copier tout le code source
COPY . /app

# Variables d'environnement pour Composer
ENV COMPOSER_ALLOW_SUPERUSER=1
ENV APP_ENV=prod

# Créer les répertoires nécessaires et ajuster les permissions
RUN mkdir -p /app/var/cache /app/var/log /var/log/supervisor && \
    chmod -R 777 /app/var /var/log/supervisor

# Installer les dépendances Composer (sans scripts)
RUN composer install --no-dev --optimize-autoloader --no-scripts --no-interaction

# Nettoyer le cache manuellement
RUN php bin/console cache:clear --env=prod --no-debug || true

# Configuration Nginx
COPY docker/nginx/default.conf /etc/nginx/http.d/default.conf

# Configuration Supervisor
COPY docker/supervisor/supervisord.conf /etc/supervisor/conf.d/supervisord.conf

# Permissions finales
RUN chown -R www-data:www-data /app/var

# Exposer le port 8080 (requis par Render)
EXPOSE 8080

# Démarrer avec supervisor
CMD ["/usr/bin/supervisord", "-c", "/etc/supervisor/conf.d/supervisord.conf"]
