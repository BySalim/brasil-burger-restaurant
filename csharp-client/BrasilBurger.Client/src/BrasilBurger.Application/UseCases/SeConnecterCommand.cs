using MediatR;

namespace BrasilBurger.Application.UseCases;

public class SeConnecterCommand : IRequest<string>
{
    public string Login { get; set; }
    public string MotDePasse { get; set; }
}