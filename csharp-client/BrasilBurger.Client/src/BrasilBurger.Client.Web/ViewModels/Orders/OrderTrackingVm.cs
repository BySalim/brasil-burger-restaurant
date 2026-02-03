using BrasilBurger.Client.Domain.Enums;

namespace BrasilBurger.Client.Web.ViewModels.Orders;

public sealed class OrderTrackingVm
{
    public required IReadOnlyList<OrderTrackingStepVm> Steps { get; init; }
    public required int CurrentIndex { get; init; }
    public required bool EstAnnulee { get; init; }
    public int TotalSteps => Steps.Count;

    /// <summary>
    /// Crée un tracking pour une commande donnée.
    /// </summary>
    public static OrderTrackingVm Create(
        EtatCommande etat,
        ModeRecuperation mode,
        StatutLivraison? statutLivraison)
    {
        var currentIndex = GetCurrentStepIndex(etat, mode, statutLivraison);
        var steps = BuildSteps(mode, currentIndex);

        return new OrderTrackingVm
        {
            Steps = steps,
            CurrentIndex = currentIndex,
            EstAnnulee = etat == EtatCommande.ANNULER
        };
    }

    private static int GetCurrentStepIndex(
        EtatCommande etat,
        ModeRecuperation mode,
        StatutLivraison? statutLivraison)
    {
        if (etat == EtatCommande.ANNULER)
            return -1;

        if (etat == EtatCommande.EN_ATTENTE)
            return 0;

        if (etat == EtatCommande.EN_PREPARATION)
            return 1;

        // TERMINER
        if (etat == EtatCommande.TERMINER)
        {
            if (mode != ModeRecuperation.LIVRER)
                return 2;

            if (!statutLivraison.HasValue)
                return 2;

            return statutLivraison == StatutLivraison.EN_COURS ? 3 : 4;
        }

        return 0;
    }

    private static List<OrderTrackingStepVm> BuildSteps(ModeRecuperation mode, int currentIndex)
    {
        var steps = new List<OrderTrackingStepVm>
        {
            new()
            {
                Label = "Commandée",
                IconName = "schedule",
                Index = 0,
                IsCompleted = currentIndex > 0,
                IsCurrent = currentIndex == 0,
                IsFinal = false
            },
            new()
            {
                Label = "Préparation",
                IconName = "skillet",
                Index = 1,
                IsCompleted = currentIndex > 1,
                IsCurrent = currentIndex == 1,
                IsFinal = false
            },
            new()
            {
                Label = "Prête",
                IconName = "package_2",
                Index = 2,
                IsCompleted = currentIndex > 2,
                IsCurrent = currentIndex == 2,
                IsFinal = mode != ModeRecuperation.LIVRER
            }
        };

        if (mode == ModeRecuperation.LIVRER)
        {
            steps.Add(new OrderTrackingStepVm
            {
                Label = "En cours",
                IconName = "scooter",
                Index = 3,
                IsCompleted = currentIndex > 3,
                IsCurrent = currentIndex == 3,
                IsFinal = false
            });

            steps.Add(new OrderTrackingStepVm
            {
                Label = "Terminée",
                IconName = "flag",
                Index = 4,
                IsCompleted = currentIndex >= 4,
                IsCurrent = currentIndex == 4,
                IsFinal = true
            });
        }

        return steps;
    }
}

public sealed class OrderTrackingStepVm
{
    public required string Label { get; init; }
    public required string IconName { get; init; }
    public required int Index { get; init; }
    public required bool IsCompleted { get; init; }
    public required bool IsCurrent { get; init; }
    public bool IsFinal { get; init; }
}