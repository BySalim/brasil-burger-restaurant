using Microsoft.AspNetCore.Mvc;

namespace BrasilBurger.Client.Web.Extensions;

/// <summary>
/// Extensions pour ajouter des notifications
/// </summary>
public static class NotificationExtensions
{
    public static void AddSuccessMessage(this Controller controller, string message)
        => controller.TempData["SuccessMessage"] = message;

    public static void AddErrorMessage(this Controller controller, string message)
        => controller.TempData["ErrorMessage"] = message;

    public static void AddWarningMessage(this Controller controller, string message)
        => controller.TempData["WarningMessage"] = message;

    public static void AddInfoMessage(this Controller controller, string message)
        => controller.TempData["InfoMessage"] = message;
}
