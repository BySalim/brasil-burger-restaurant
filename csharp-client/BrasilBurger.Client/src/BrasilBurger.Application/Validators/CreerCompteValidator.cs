using FluentValidation;

namespace BrasilBurger.Application.Validators;

public class CreerCompteValidator : AbstractValidator<CreerCompteCommand>
{
    public CreerCompteValidator()
    {
        RuleFor(x => x.Nom).NotEmpty().WithMessage("Le nom est requis.");
        RuleFor(x => x.Prenom).NotEmpty().WithMessage("Le prénom est requis.");
        RuleFor(x => x.Login).NotEmpty().WithMessage("Le login est requis.");
        RuleFor(x => x.MotDePasse).NotEmpty().MinimumLength(6).WithMessage("Le mot de passe doit contenir au moins 6 caractères.");
        RuleFor(x => x.Telephone).NotEmpty().WithMessage("Le téléphone est requis pour les clients.");
    }
}