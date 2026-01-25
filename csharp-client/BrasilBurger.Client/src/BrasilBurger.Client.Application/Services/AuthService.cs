using BrasilBurger.Client.Application.Abstractions.Persistence;
using BrasilBurger.Client.Application.Abstractions.Security;
using BrasilBurger.Client.Application.Common;
using BrasilBurger.Client.Application.Dtos;
using BrasilBurger.Client.Domain.Entities;
using BrasilBurger.Client.Domain.Enums;
using BrasilBurger.Client.Application.Abstractions.Services;

namespace BrasilBurger.Client.Application.Services;

public sealed class AuthService : IAuthService
{
    private readonly IUtilisateurRepository _utilisateurRepo;
    private readonly IQuartierRepository _quartierRepo;
    private readonly IPasswordHasher _passwordHasher;
    private readonly IUnitOfWork _unitOfWork;

    public AuthService(
        IUtilisateurRepository utilisateurRepo,
        IQuartierRepository quartierRepo,
        IPasswordHasher passwordHasher,
        IUnitOfWork unitOfWork)
    {
        _utilisateurRepo = utilisateurRepo;
        _quartierRepo = quartierRepo;
        _passwordHasher = passwordHasher;
        _unitOfWork = unitOfWork;
    }
    

    public async Task<Result<Domain.Entities.Client>> RegisterClientAsync(
        RegisterClientDto dto,
        CancellationToken ct = default)
    {
        //  Validation 1 : Email déjà utilisé 
        var existingByEmail = await _utilisateurRepo.GetByLoginAsync(dto.Email, ct);
        if (existingByEmail is not null)
        {
            return Result<Domain.Entities.Client>.Failure(
                "Cette adresse email est déjà utilisée.");
        }

        //  Validation 2 : Téléphone déjà utilisé
        var existingByPhone = await _utilisateurRepo.GetByTelephoneAsync(dto.Telephone, ct);
        
        if (existingByPhone is not null)
        {
            return Result<Domain.Entities.Client>.Failure(
                "Ce numéro de téléphone est déjà utilisé.");
        }

        //  Validation 3 : Quartier existe (si fourni) 
        Quartier? quartier = null;
        if (dto.QuartierId.HasValue)
        {
            quartier = await _quartierRepo.GetByIdAsync(dto.QuartierId.Value, ct);
            if (quartier is null)
            {
                return Result<Domain.Entities.Client>.Failure(
                    "Le quartier sélectionné n'existe pas.");
            }
        }

        //  Création du client 
        var hashedPassword = _passwordHasher.Hash(dto.Password);
        var client = new Domain.Entities.Client(
            nom: dto.Nom,
            prenom: dto.Prenom,
            login: dto.Email,
            motDePasse: hashedPassword,
            telephone: dto.Telephone
        );

        //  Configuration des préférences 
        if (quartier is not null)
        {
            client.DefinirQuartierLivraisonDefaut(quartier);
        }

        if (!string.IsNullOrWhiteSpace(dto.ModePaiement) &&
            Enum.TryParse<ModePaiement>(dto.ModePaiement, ignoreCase: true, out var modePaie))
        {
            if (!string.IsNullOrWhiteSpace(dto.ModeRecuperation) &&
                Enum.TryParse<ModeRecuperation>(dto.ModeRecuperation, ignoreCase: true, out var modeRecup))
            {
                client.DefinirModesDefaut(modePaie, modeRecup);
            }
            else
            {
                client.DefinirModesDefaut(modePaie, null);
            }
        }

        //  Persistance 
        await _utilisateurRepo.AddAsync(client, ct);
        await _unitOfWork.SaveChangesAsync(ct);

        return Result<Domain.Entities.Client>.Success(client);
    }

    public async Task<Utilisateur?> AuthenticateAsync(
        string login,
        string password,
        CancellationToken ct = default)
    {
        var user = await _utilisateurRepo.GetByLoginAsync(login, ct);
        if (user is null)
            return null;

        if (!_passwordHasher.Verify(user.MotDePasse, password))
            return null;

        return user;
    }
}
