# Étape 1 : Build
FROM mcr.microsoft.com/dotnet/sdk:9.0 AS build
WORKDIR /app

# Copier les fichiers de projet
COPY src/BrasilBurger.Domain/BrasilBurger.Domain.csproj src/BrasilBurger.Domain/
COPY src/BrasilBurger.Application/BrasilBurger.Application.csproj src/BrasilBurger.Application/
COPY src/BrasilBurger.Infrastructure/BrasilBurger.Infrastructure.csproj src/BrasilBurger.Infrastructure/
COPY src/BrasilBurger.Web/BrasilBurger.Web.csproj src/BrasilBurger.Web/

# Restaurer les dépendances
RUN dotnet restore src/BrasilBurger.Web/BrasilBurger.Web.csproj

# Copier tout le code source
COPY . .

# Build
WORKDIR /app/src/BrasilBurger.Web
RUN dotnet publish -c Release -o /app/publish

# Étape 2 : Runtime
FROM mcr.microsoft.com/dotnet/aspnet:9.0
WORKDIR /app
COPY --from=build /app/publish .

# Exposer le port (Render utilise )
ENV ASPNETCORE_URLS=http://+:$PORT

ENTRYPOINT ["dotnet", "BrasilBurger.Web.dll"]
