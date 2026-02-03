using Microsoft.AspNetCore.Mvc;
using BrasilBurger.Client.Domain.Enums;
using BrasilBurger.Client.Web.EnumsUi.Extensions;
using BrasilBurger.Client.Web.ViewModels.Orders;
using BrasilBurger.Client.Web.ViewModels.Shared;
using BrasilBurger.Client.Web.FormatsApp;
using BrasilBurger.Client.Web.Mappers;
using Microsoft.AspNetCore.Authorization;
using BrasilBurger.Client.Application.Abstractions.Persistence;
using BrasilBurger.Client.Application.Abstractions.Security;
using BrasilBurger.Client.Application.Common;

namespace BrasilBurger.Client.Web.Controllers;

[Route("Orders")]
[Authorize]
public class OrdersController : Controller
{
    private readonly IFormatsApp _formatsApp;
    private readonly OrderMapper _orderMapper;
    private readonly ICommandeRepository _commandeRepository;
    private readonly IUserSession _userSession;

    public OrdersController(
        IFormatsApp formatsApp,
        OrderMapper orderMapper,
        ICommandeRepository commandeRepository,
        IUserSession userSession)
    {
        _formatsApp = formatsApp;
        _orderMapper = orderMapper;
        _commandeRepository = commandeRepository;
        _userSession = userSession;
    }

    [HttpGet("")]
    public async Task<IActionResult> Index(OrderFiltersFormVm filters)
    {
        // Reset pagination quand il y a des filtres
        var hasFilters = !string.IsNullOrWhiteSpace(filters.Search) || 
                        !string.IsNullOrWhiteSpace(filters.Date) || 
                        !string.IsNullOrWhiteSpace(filters.OrderStatus) || 
                        !string.IsNullOrWhiteSpace(filters.PickupMode);
        
        var page = hasFilters ? 1 : (filters.Page ?? 1);
        var perPageSelect = BuildPerPageSelect(filters.PerPage);
        var itemsPerPage = int.TryParse(perPageSelect.SelectedValue, out var pp) ? pp : 10;
        var currentPage = page < 1 ? 1 : page;
        
        // Parse pour avoir le même format qu'en base de données (UTC)
        DateTime? dateSelect = null;
        if (DateTime.TryParse(filters.Date, System.Globalization.CultureInfo.InvariantCulture, System.Globalization.DateTimeStyles.AssumeUniversal, out DateTime filterDate))
        {
            dateSelect = filterDate.ToUniversalTime();
        }

        EtatCommande? etatFilter = ConvertEnum.FromString<EtatCommande>(filters.OrderStatus);

        ModeRecuperation? modeFilter = ConvertEnum.FromString<ModeRecuperation>(filters.PickupMode);

        var clientId = _userSession.UserId!.Value;
        var (orderItems, totalItems) = await _commandeRepository.SearchAsync(clientId, filters.Search, dateSelect, etatFilter, modeFilter, (currentPage - 1) * itemsPerPage, itemsPerPage);

        var orderItemVms = orderItems.Select(_orderMapper.MapToOrderItemVm).ToList();

        // Calculer la pagination
        var totalPages = (int)Math.Ceiling(totalItems / (double)itemsPerPage);
        currentPage = Math.Clamp(currentPage, 1, Math.Max(1, totalPages));

        // Construire le ViewModel
        var viewModel = new OrderListVm
        {
            Orders = orderItemVms,
            OrderStatusSelect = BuildEtatCommandeSelect(filters.OrderStatus),
            RecoveryMode = BuildModeRecuperationSelect(filters.PickupMode),
            Pagination = new PaginationVm
            {
                CurrentPage = currentPage,
                TotalPages = totalPages,
                TotalItems = totalItems,
                PerPageSelect = perPageSelect,
                ItemsName = "commandes"
            },
            SearchFilter = filters.Search,
            DateFilter = filters.Date
        };

        return View(viewModel);
    }

    private SelectVm BuildEtatCommandeSelect(string? selectedValue = null)
    {
        var options = EtatCommandeUiExtensions.AllUi()
            .Select(meta => new SelectOptionVm(meta.Value, meta.Label))
            .ToList();

        return new SelectVm
        {
            Name = "orderStatus",
            Placeholder = "Tous les statuts",
            SelectedValue = selectedValue,
            Options = options,
            AutoSubmit = true
        };
    }

    private SelectVm BuildModeRecuperationSelect(string? selectedValue = null)
    {
        var options = ModeRecuperationUiExtensions.AllUi()
            .Select(meta => new SelectOptionVm(meta.Value, meta.Label))
            .ToList();

        return new SelectVm
        {
            Name = "pickupMode",
            Placeholder = "Toutes les récupérations",
            SelectedValue = selectedValue,
            Options = options,
            AutoSubmit = true
        };
    }

    private SelectVm BuildPerPageSelect(int? selectedValue = null)
    {
        var select = _formatsApp.GetDefaultPerPageSelect();
        
        if (selectedValue.HasValue)
        {
            select.SelectedValue = selectedValue.Value.ToString();
        }

        return select;
    }

    [HttpGet("Details/{id}")]
    public async Task<IActionResult> Details(int id)
    {
        var commande = await _commandeRepository.GetWithDetailsAsync(id);

        if (commande == null)
            return NotFound();

        if (commande.ClientId != _userSession.UserId)
            return Forbid();

        var viewModel = _orderMapper.MapToOrderDetailVm(commande);

        return View(viewModel);
    }

}