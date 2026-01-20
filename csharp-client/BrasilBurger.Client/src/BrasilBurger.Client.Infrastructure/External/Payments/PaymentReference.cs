using BrasilBurger.Client.Domain.Enums;

namespace BrasilBurger.Client.Infrastructure.External.Payments;

internal static class PaymentReference
{
    public static string Prefix(ModePaiement mode) => mode switch
    {
        ModePaiement.WAVE => "WAVE_",
        ModePaiement.OM => "OM_",
        _ => throw new NotSupportedException($"ModePaiement non supporté: {mode}")
    };

    public static ModePaiement ModeFromReference(string reference)
    {
        if (reference.StartsWith("WAVE_", StringComparison.Ordinal)) return ModePaiement.WAVE;
        if (reference.StartsWith("OM_", StringComparison.Ordinal)) return ModePaiement.OM;

        throw new ArgumentException("Référence invalide. Préfixe attendu: WAVE_ ou OM_.", nameof(reference));
    }
}
