using AutoMapper;
using BrasilBurger.Domain.Entities;
using BrasilBurger.Application.DTOs;

namespace BrasilBurger.Application.Mapping;

public class MappingProfile : Profile
{
    public MappingProfile()
    {
        CreateMap<Article, ArticleDto>()
            .ForMember(dest => dest.ImageUrl, opt => opt.MapFrom(src => src.ImagePublicId)); // Simplifié, utiliser service pour URL
    }
}