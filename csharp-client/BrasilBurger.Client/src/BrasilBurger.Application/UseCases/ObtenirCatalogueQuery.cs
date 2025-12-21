using MediatR;
using BrasilBurger.Application.DTOs;

namespace BrasilBurger.Application.UseCases;

public class ObtenirCatalogueQuery : IRequest<IEnumerable<ArticleDto>>
{
}