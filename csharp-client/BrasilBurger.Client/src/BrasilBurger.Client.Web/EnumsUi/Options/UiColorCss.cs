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
    

    private static string BgSoft(this UiColor c) => c switch
    {
        UiColor.Rouge     => "bg-red-50",
        UiColor.Jaune     => "bg-amber-50",
        UiColor.Bleu      => "bg-blue-50",
        UiColor.Vert      => "bg-emerald-50",
        UiColor.Orange    => "bg-orange-50",
        UiColor.Violet    => "bg-purple-50",
        UiColor.Rose      => "bg-pink-50",
        UiColor.Turquoise => "bg-teal-50",
        UiColor.Gris      => "bg-slate-50",
        UiColor.Noir      => "bg-zinc-100",
        UiColor.Indigo    => "bg-indigo-50",
        _ => throw new ArgumentOutOfRangeException(nameof(c), c, "UiColor non supportée.")
    };

    private static string BgSolid(this UiColor c) => c switch
    {
        UiColor.Rouge     => "bg-red-600",
        UiColor.Jaune     => "bg-amber-600",
        UiColor.Bleu      => "bg-blue-600",
        UiColor.Vert      => "bg-emerald-600",
        UiColor.Orange    => "bg-orange-600",
        UiColor.Violet    => "bg-purple-600",
        UiColor.Rose      => "bg-pink-600",
        UiColor.Turquoise => "bg-teal-600",
        UiColor.Gris      => "bg-slate-600",
        UiColor.Noir      => "bg-zinc-900",
        UiColor.Indigo    => "bg-indigo-600",
        _ => throw new ArgumentOutOfRangeException(nameof(c), c, "UiColor non supportée.")
    };

    private static string TextBase(this UiColor c) => c switch
    {
        UiColor.Rouge     => "text-red-600",
        UiColor.Jaune     => "text-amber-700",
        UiColor.Bleu      => "text-blue-600",
        UiColor.Vert      => "text-emerald-600",
        UiColor.Orange    => "text-orange-600",
        UiColor.Violet    => "text-purple-600",
        UiColor.Rose      => "text-pink-600",
        UiColor.Turquoise => "text-teal-600",
        UiColor.Gris      => "text-slate-600",
        UiColor.Noir      => "text-zinc-900",
        UiColor.Indigo    => "text-indigo-600",
        _ => throw new ArgumentOutOfRangeException(nameof(c), c, "UiColor non supportée.")
    };

    private static string TextStrong(this UiColor c) => c switch
    {
        UiColor.Rouge     => "text-red-800",
        UiColor.Jaune     => "text-amber-900",
        UiColor.Bleu      => "text-blue-800",
        UiColor.Vert      => "text-emerald-800",
        UiColor.Orange    => "text-orange-800",
        UiColor.Violet    => "text-purple-800",
        UiColor.Rose      => "text-pink-800",
        UiColor.Turquoise => "text-teal-800",
        UiColor.Gris      => "text-slate-700",
        UiColor.Noir      => "text-zinc-900",
        UiColor.Indigo    => "text-indigo-800",
        _ => throw new ArgumentOutOfRangeException(nameof(c), c, "UiColor non supportée.")
    };

    private static string RingSoft(this UiColor c) => c switch
    {
        UiColor.Rouge     => "ring-red-600/15",
        UiColor.Jaune     => "ring-amber-600/20",
        UiColor.Bleu      => "ring-blue-600/15",
        UiColor.Vert      => "ring-emerald-600/15",
        UiColor.Orange    => "ring-orange-600/15",
        UiColor.Violet    => "ring-purple-600/15",
        UiColor.Rose      => "ring-pink-600/15",
        UiColor.Turquoise => "ring-teal-600/15",
        UiColor.Gris      => "ring-slate-400/25",
        UiColor.Noir      => "ring-zinc-900/10",
        UiColor.Indigo    => "ring-indigo-600/15",
        _ => throw new ArgumentOutOfRangeException(nameof(c), c, "UiColor non supportée.")
    };

    private static string RingStrong(this UiColor c) => c switch
    {
        UiColor.Rouge     => "ring-red-600/30",
        UiColor.Jaune     => "ring-amber-600/35",
        UiColor.Bleu      => "ring-blue-600/30",
        UiColor.Vert      => "ring-emerald-600/30",
        UiColor.Orange    => "ring-orange-600/30",
        UiColor.Violet    => "ring-purple-600/30",
        UiColor.Rose      => "ring-pink-600/30",
        UiColor.Turquoise => "ring-teal-600/30",
        UiColor.Gris      => "ring-slate-500/35",
        UiColor.Noir      => "ring-zinc-900/25",
        UiColor.Indigo    => "ring-indigo-600/30",
        _ => throw new ArgumentOutOfRangeException(nameof(c), c, "UiColor non supportée.")
    };
}

