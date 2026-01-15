using BrasilBurger.Client.Models.DTOs.Orders;
using BrasilBurger.Client.Models.Entities;
using BrasilBurger.Client.Models.Enums;
using BrasilBurger.Client.Repositories.Interfaces;
using BrasilBurger.Client.Services.Interfaces;
using BrasilBurger.Client.Helpers;

namespace BrasilBurger.Client.Services.Implementations;

public class OrderService : IOrderService
{
    private readonly IOrderRepository _orderRepository;
    private readonly ICatalogRepository _catalogRepository;
    private readonly IPaymentMethodRepository _paymentMethodRepository;
    private readonly ICloudinaryService _cloudinaryService;

    public OrderService(
        IOrderRepository orderRepository,
        ICatalogRepository catalogRepository,
        IPaymentMethodRepository paymentMethodRepository,
        ICloudinaryService cloudinaryService)
    {
        _orderRepository = orderRepository;
        _catalogRepository = catalogRepository;
        _paymentMethodRepository = paymentMethodRepository;
        _cloudinaryService = cloudinaryService;
    }

    public async Task<OrderDto> CreateOrderAsync(CreateOrderDto request, int userId)
    {
        // Valider la méthode de paiement
        if (!await _paymentMethodRepository.IsPaymentMethodEnabledAsync(request.PaymentMethodId))
        {
            throw new InvalidOperationException("Méthode de paiement invalide ou désactivée");
        }

        decimal totalPrice = 0;
        var orderItems = new List<OrderItem>();

        // Traiter chaque item
        foreach (var itemDto in request.Items)
        {
            decimal itemPrice = 0;

            if (itemDto.ItemType.ToLower() == "burger")
            {
                var burger = await _catalogRepository.GetBurgerByIdAsync(itemDto.ItemId);
                if (burger == null)
                    throw new KeyNotFoundException($"Burger {itemDto.ItemId} non trouvé");

                itemPrice = burger.Price;

                var orderItem = new OrderItem
                {
                    ItemType = "burger",
                    ItemId = itemDto.ItemId,
                    Quantity = itemDto.Quantity,
                    UnitPrice = itemPrice,
                    OrderComplements = new List<OrderComplement>()
                };

                // Ajouter les compléments
                if (itemDto.Complements != null && itemDto.Complements.Count > 0)
                {
                    foreach (var complementDto in itemDto.Complements)
                    {
                        var complement = await _catalogRepository.GetComplementByIdAsync(complementDto.ComplementId);
                        if (complement == null)
                            throw new KeyNotFoundException($"Complément {complementDto.ComplementId} non trouvé");

                        orderItem.OrderComplements.Add(new OrderComplement
                        {
                            ComplementId = complementDto.ComplementId,
                            Quantity = complementDto.Quantity,
                            UnitPrice = complement.Price
                        });

                        totalPrice += complement.Price * complementDto.Quantity;
                    }
                }

                orderItems.Add(orderItem);
                totalPrice += itemPrice * itemDto.Quantity;
            }
            else if (itemDto.ItemType.ToLower() == "menu")
            {
                var menu = await _catalogRepository.GetMenuByIdAsync(itemDto.ItemId);
                if (menu == null)
                    throw new KeyNotFoundException($"Menu {itemDto.ItemId} non trouvé");

                itemPrice = menu.Price;
                var orderItem = new OrderItem
                {
                    ItemType = "menu",
                    ItemId = itemDto.ItemId,
                    Quantity = itemDto.Quantity,
                    UnitPrice = itemPrice
                };

                orderItems.Add(orderItem);
                totalPrice += itemPrice * itemDto.Quantity;
            }
            else
            {
                throw new InvalidOperationException("Type d'item invalide");
            }
        }

        // Créer la commande
        var order = new Order
        {
            UserId = userId,
            Reference = OrderReferenceGenerator.Generate(),
            TotalPrice = totalPrice,
            Status = OrderStatus.Pending,
            PaymentMethodId = request.PaymentMethodId,
            CreatedAt = DateTime.UtcNow,
            OrderItems = orderItems
        };

        var createdOrder = await _orderRepository.CreateOrderAsync(order);

        return new OrderDto
        {
            Id = createdOrder.Id,
            Reference = createdOrder.Reference,
            TotalPrice = createdOrder.TotalPrice,
            Status = createdOrder.Status.ToString(),
            PaymentMethod = "À payer",
            CreatedAt = createdOrder.CreatedAt
        };
    }

    public async Task<OrderDto> GetOrderAsync(int id, int userId)
    {
        var order = await _orderRepository.GetOrderByIdAsync(id);

        if (order == null || order.UserId != userId)
            throw new UnauthorizedAccessException("Vous n'avez pas accès à cette commande");

        return MapToOrderDto(order);
    }

    public async Task<List<OrderDto>> GetUserOrdersAsync(int userId)
    {
        var orders = await _orderRepository.GetUserOrdersAsync(userId);
        return orders.Select(MapToOrderDto).ToList();
    }

    public async Task<OrderDetailDto> GetOrderDetailsAsync(int id, int userId)
    {
        var order = await _orderRepository.GetOrderByIdAsync(id);

        if (order == null || order.UserId != userId)
            throw new UnauthorizedAccessException("Vous n'avez pas accès à cette commande");

        var items = new List<OrderItemDetailDto>();

        foreach (var orderItem in order.OrderItems)
        {
            string itemName = "";

            if (orderItem.ItemType.ToLower() == "burger")
            {
                var burger = await _catalogRepository.GetBurgerByIdAsync(orderItem.ItemId);
                itemName = burger?.Name ?? "Burger supprimé";
            }
            else if (orderItem.ItemType.ToLower() == "menu")
            {
                var menu = await _catalogRepository.GetMenuByIdAsync(orderItem.ItemId);
                itemName = menu?.Name ?? "Menu supprimé";
            }

            var itemDetail = new OrderItemDetailDto
            {
                Id = orderItem.Id,
                ItemName = itemName,
                ItemType = orderItem.ItemType,
                Quantity = orderItem.Quantity,
                UnitPrice = orderItem.UnitPrice,
                Complements = new List<OrderComplementDetailDto>()
            };

            foreach (var complement in orderItem.OrderComplements)
            {
                itemDetail.Complements.Add(new OrderComplementDetailDto
                {
                    Id = complement.Id,
                    ComplementName = "Complément",
                    Quantity = complement.Quantity,
                    UnitPrice = complement.UnitPrice
                });
            }

            items.Add(itemDetail);
        }

        return new OrderDetailDto
        {
            Id = order.Id,
            Reference = order.Reference,
            TotalPrice = order.TotalPrice,
            Status = order.Status.ToString(),
            PaymentMethod = order.PaymentMethod?.Name ?? "Non disponible",
            CreatedAt = order.CreatedAt,
            Items = items
        };
    }

    private OrderDto MapToOrderDto(Order order)
    {
        return new OrderDto
        {
            Id = order.Id,
            Reference = order.Reference,
            TotalPrice = order.TotalPrice,
            Status = order.Status.ToString(),
            PaymentMethod = order.PaymentMethod?.Name ?? "Non disponible",
            CreatedAt = order.CreatedAt
        };
    }
}
