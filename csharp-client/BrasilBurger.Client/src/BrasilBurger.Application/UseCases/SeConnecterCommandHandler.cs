using MediatR;
using BrasilBurger.Domain.Interfaces;
using BrasilBurger.Domain.Entities;
using BrasilBurger.Application.Common;

namespace BrasilBurger.Application.UseCases;

public class SeConnecterCommandHandler : IRequestHandler<SeConnecterCommand, string>
{
    private readonly IUserRepository _userRepository;
    private readonly IAuthenticationService _authService;

    public SeConnecterCommandHandler(IUserRepository userRepository, IAuthenticationService authService)
    {
        _userRepository = userRepository;
        _authService = authService;
    }

    public async Task<string> Handle(SeConnecterCommand request, CancellationToken cancellationToken)
    {
        var user = await _userRepository.GetByLoginAsync(request.Login);
        if (user == null || !_authService.VerifyPassword(request.MotDePasse, user.MotDePasse))
            throw new ValidationException("Login ou mot de passe incorrect.");

        return _authService.GenerateToken(user);
    }
}