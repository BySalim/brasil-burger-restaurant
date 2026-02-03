namespace BrasilBurger.Client.Web.EnumsUi.Options;

public static class UiColorCss
{
    public static string BadgeNone()
        => "inline-flex items-center gap-2 rounded-lg px-3 py-1 text-sm font-semibold ring-1";
    
    public static string BadgeSoft(this UiColor c)
        => $"inline-flex items-center gap-2 rounded-lg px-3 py-1 text-sm font-semibold ring-1 {c.BgSoft()} {c.TextStrong()} {c.RingSoft()}";

    public static string BadgeSolid(this UiColor c)
        => $"inline-flex items-center gap-2 rounded-lg px-3 py-1 text-sm font-semibold {c.BgSolid()} text-white";

    public static string BadgeOutline(this UiColor c)
        => $"inline-flex items-center gap-2 rounded-lg px-3 py-1 text-sm font-semibold bg-transparent ring-1 {c.TextStrong()} {c.RingStrong()}";

    public static string BadgePillSoft(this UiColor c)
        => $"inline-flex items-center gap-2 rounded-full px-3 py-1 text-sm font-semibold ring-1 {c.BgSoft()} {c.TextStrong()} {c.RingSoft()}";

    public static string BadgeCompact(this UiColor c)
        => $"inline-flex items-center gap-2 rounded-md px-2 py-0.5 text-xs font-semibold ring-1 {c.BgSoft()} {c.TextStrong()} {c.RingSoft()}";
    
    public static string Icon(this UiColor c) => c.TextBase();
    
    public static string Dot(this UiColor c) => $"inline-block h-2 w-2 rounded-full {c.BgSolid()}";

    // ─────────────────────────────────────────────────────────────────
    // Background Soft (fond clair pour badges soft)
    // ─────────────────────────────────────────────────────────────────
    private static string BgSoft(this UiColor c) => c switch
    {
        UiColor.Rouge     => "bg-red-50",
        UiColor.Jaune     => "bg-yellow-50",
        UiColor.Bleu      => "bg-blue-50",
        UiColor.Vert      => "bg-emerald-50",
        UiColor.Orange    => "bg-orange-100",
        UiColor.Violet    => "bg-violet-50",
        UiColor.Rose      => "bg-pink-50",
        UiColor.Turquoise => "bg-teal-50",
        UiColor.Gris      => "bg-slate-100",
        UiColor.Noir      => "bg-zinc-100",
        UiColor.Indigo    => "bg-indigo-50",
        UiColor.Cyan      => "bg-cyan-50",
        UiColor.Lime      => "bg-lime-50",
        UiColor.Ambre     => "bg-amber-50",
        UiColor.Fuchsia   => "bg-fuchsia-50",
        UiColor.Sky       => "bg-sky-50",
        _ => "bg-gray-50"
    };

    // ─────────────────────────────────────────────────────────────────
    // Background Solid (fond plein pour badges solid)
    // ─────────────────────────────────────────────────────────────────
    private static string BgSolid(this UiColor c) => c switch
    {
        UiColor.Rouge     => "bg-red-600",
        UiColor.Jaune     => "bg-yellow-500",
        UiColor.Bleu      => "bg-blue-600",
        UiColor.Vert      => "bg-emerald-600",
        UiColor.Orange    => "bg-orange-500",
        UiColor.Violet    => "bg-violet-600",
        UiColor.Rose      => "bg-pink-600",
        UiColor.Turquoise => "bg-teal-600",
        UiColor.Gris      => "bg-slate-500",
        UiColor.Noir      => "bg-zinc-900",
        UiColor.Indigo    => "bg-indigo-600",
        UiColor.Cyan      => "bg-cyan-600",
        UiColor.Lime      => "bg-lime-600",
        UiColor.Ambre     => "bg-amber-500",
        UiColor.Fuchsia   => "bg-fuchsia-600",
        UiColor.Sky       => "bg-sky-600",
        _ => "bg-gray-600"
    };

    // ─────────────────────────────────────────────────────────────────
    // Text Base (icônes, texte normal)
    // ─────────────────────────────────────────────────────────────────
    private static string TextBase(this UiColor c) => c switch
    {
        UiColor.Rouge     => "text-red-600",
        UiColor.Jaune     => "text-yellow-600",
        UiColor.Bleu      => "text-blue-600",
        UiColor.Vert      => "text-emerald-600",
        UiColor.Orange    => "text-orange-600",
        UiColor.Violet    => "text-violet-600",
        UiColor.Rose      => "text-pink-600",
        UiColor.Turquoise => "text-teal-600",
        UiColor.Gris      => "text-slate-600",
        UiColor.Noir      => "text-zinc-900",
        UiColor.Indigo    => "text-indigo-600",
        UiColor.Cyan      => "text-cyan-600",
        UiColor.Lime      => "text-lime-600",
        UiColor.Ambre     => "text-amber-600",
        UiColor.Fuchsia   => "text-fuchsia-600",
        UiColor.Sky       => "text-sky-600",
        _ => "text-gray-600"
    };

    // ─────────────────────────────────────────────────────────────────
    // Text Strong (texte sur fond clair, contraste élevé)
    // ─────────────────────────────────────────────────────────────────
    private static string TextStrong(this UiColor c) => c switch
    {
        UiColor.Rouge     => "text-red-700",
        UiColor.Jaune     => "text-yellow-800",
        UiColor.Bleu      => "text-blue-700",
        UiColor.Vert      => "text-emerald-700",
        UiColor.Orange    => "text-orange-700",
        UiColor.Violet    => "text-violet-700",
        UiColor.Rose      => "text-pink-700",
        UiColor.Turquoise => "text-teal-700",
        UiColor.Gris      => "text-slate-700",
        UiColor.Noir      => "text-zinc-900",
        UiColor.Indigo    => "text-indigo-700",
        UiColor.Cyan      => "text-cyan-700",
        UiColor.Lime      => "text-lime-700",
        UiColor.Ambre     => "text-amber-800",
        UiColor.Fuchsia   => "text-fuchsia-700",
        UiColor.Sky       => "text-sky-700",
        _ => "text-gray-700"
    };

    // ─────────────────────────────────────────────────────────────────
    // Ring Soft (bordure légère)
    // ─────────────────────────────────────────────────────────────────
    private static string RingSoft(this UiColor c) => c switch
    {
        UiColor.Rouge     => "ring-red-600/20",
        UiColor.Jaune     => "ring-yellow-600/25",
        UiColor.Bleu      => "ring-blue-600/20",
        UiColor.Vert      => "ring-emerald-600/20",
        UiColor.Orange    => "ring-orange-500/25",
        UiColor.Violet    => "ring-violet-600/20",
        UiColor.Rose      => "ring-pink-600/20",
        UiColor.Turquoise => "ring-teal-600/20",
        UiColor.Gris      => "ring-slate-400/30",
        UiColor.Noir      => "ring-zinc-900/15",
        UiColor.Indigo    => "ring-indigo-600/20",
        UiColor.Cyan      => "ring-cyan-600/20",
        UiColor.Lime      => "ring-lime-600/25",
        UiColor.Ambre     => "ring-amber-500/25",
        UiColor.Fuchsia   => "ring-fuchsia-600/20",
        UiColor.Sky       => "ring-sky-600/20",
        _ => "ring-gray-400/25"
    };

    // ─────────────────────────────────────────────────────────────────
    // Ring Strong (bordure plus visible pour outline)
    // ─────────────────────────────────────────────────────────────────
    private static string RingStrong(this UiColor c) => c switch
    {
        UiColor.Rouge     => "ring-red-600/40",
        UiColor.Jaune     => "ring-yellow-600/45",
        UiColor.Bleu      => "ring-blue-600/40",
        UiColor.Vert      => "ring-emerald-600/40",
        UiColor.Orange    => "ring-orange-500/45",
        UiColor.Violet    => "ring-violet-600/40",
        UiColor.Rose      => "ring-pink-600/40",
        UiColor.Turquoise => "ring-teal-600/40",
        UiColor.Gris      => "ring-slate-500/45",
        UiColor.Noir      => "ring-zinc-900/30",
        UiColor.Indigo    => "ring-indigo-600/40",
        UiColor.Cyan      => "ring-cyan-600/40",
        UiColor.Lime      => "ring-lime-600/45",
        UiColor.Ambre     => "ring-amber-500/45",
        UiColor.Fuchsia   => "ring-fuchsia-600/40",
        UiColor.Sky       => "ring-sky-600/40",
        _ => "ring-gray-500/40"
    };
}