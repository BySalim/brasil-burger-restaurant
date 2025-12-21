using Microsoft.Extensions.DependencyInjection;
using MediatR;
using FluentValidation;
using AutoMapper;
using BrasilBurger.Application.Mapping;
using BrasilBurger.Application.UseCases;

namespace BrasilBurger.Web.Extensions;

public static class ServiceCollectionExtensions
{
    public static IServiceCollection AddApplicationServices(this IServiceCollection services)
    {
        services.AddMediatR(typeof(SeConnecterCommand).Assembly);
        services.AddAutoMapper(typeof(MappingProfile).Assembly);
        services.AddValidatorsFromAssembly(typeof(SeConnecterValidator).Assembly);

        return services;
    }
}