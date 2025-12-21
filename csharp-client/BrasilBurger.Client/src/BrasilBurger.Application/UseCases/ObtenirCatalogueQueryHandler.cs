using MediatR;
using AutoMapper;
using BrasilBurger.Domain.Interfaces;
using BrasilBurger.Application.DTOs;

namespace BrasilBurger.Application.UseCases;

public class ObtenirCatalogueQueryHandler : IRequestHandler<ObtenirCatalogueQuery, IEnumerable<ArticleDto>>
{
    private readonly IArticleRepository _articleRepository;
    private readonly IMapper _mapper;

    public ObtenirCatalogueQueryHandler(IArticleRepository articleRepository, IMapper mapper)
    {
        _articleRepository = articleRepository;
        _mapper = mapper;
    }

    public async Task<IEnumerable<ArticleDto>> Handle(ObtenirCatalogueQuery request, CancellationToken cancellationToken)
    {
        var articles = await _articleRepository.GetNonArchivesAsync();
        return _mapper.Map<IEnumerable<ArticleDto>>(articles);
    }
}