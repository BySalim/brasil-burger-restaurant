using FluentValidation;

namespace BrasilBurger.Application.UseCases;

public class SeConnecterValidator : AbstractValidator<SeConnecterCommand>
{
    public SeConnecterValidator()
    {
        RuleFor(x => x.Login).NotEmpty().WithMessage("Le login est requis.");
        RuleFor(x => x.MotDePasse).NotEmpty().WithMessage("Le mot de passe est requis.");
    }
}