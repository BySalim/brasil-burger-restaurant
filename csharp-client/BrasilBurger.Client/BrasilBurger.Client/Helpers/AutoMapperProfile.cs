using AutoMapper;
using BrasilBurger.Client.Models.DTOs.Catalog;
using BrasilBurger.Client.Models.DTOs.Auth;
using BrasilBurger.Client.Models.Entities;

namespace BrasilBurger.Client.Helpers;

public class AutoMapperProfile : Profile
{
    public AutoMapperProfile()
    {
        // Auth
        CreateMap<User, UserDto>();

        // Catalog
        CreateMap<Burger, BurgerDto>();
        CreateMap<Menu, MenuDto>();
        CreateMap<Complement, ComplementDto>();

        // Reverse maps
        CreateMap<UserDto, User>();
        CreateMap<BurgerDto, Burger>();
        CreateMap<MenuDto, Menu>();
        CreateMap<ComplementDto, Complement>();
    }
}
