namespace BrasilBurger.Client.Web.EnumsUi.ColorUi;

public static class UiColorExtensions
{
    public static string GetValue(this UiColor color) => color switch
    {
        UiColor.Blue => "blue",
        UiColor.Indigo => "indigo",
        UiColor.Purple => "purple",
        UiColor.Pink => "pink",
        UiColor.Red => "red",
        UiColor.Orange => "orange",
        UiColor.Yellow => "yellow",
        UiColor.Green => "green",
        UiColor.Teal => "teal",
        UiColor.Cyan => "cyan",
        UiColor.Gray => "gray",
        UiColor.Slate => "slate",
        UiColor.Black => "black",
        UiColor.White => "white",
        _ => throw new ArgumentOutOfRangeException(nameof(color), color, null)
    };

    public static string GetBadgeClasses(this UiColor color) => color switch
    {
        UiColor.Blue   => "bg-blue-100 text-blue-700 border-blue-200 dark:bg-blue-900/30 dark:text-blue-300 dark:border-blue-800",
        UiColor.Indigo => "bg-indigo-100 text-indigo-700 border-indigo-200 dark:bg-indigo-900/30 dark:text-indigo-300 dark:border-indigo-800",
        UiColor.Purple => "bg-purple-100 text-purple-700 border-purple-200 dark:bg-purple-900/30 dark:text-purple-300 dark:border-purple-800",
        UiColor.Pink   => "bg-pink-100 text-pink-700 border-pink-200 dark:bg-pink-900/30 dark:text-pink-300 dark:border-pink-800",
        UiColor.Red    => "bg-red-100 text-red-700 border-red-200 dark:bg-red-900/30 dark:text-red-300 dark:border-red-800",
        UiColor.Orange => "bg-orange-100 text-orange-800 border-orange-200 dark:bg-orange-900/30 dark:text-orange-300 dark:border-orange-800",
        UiColor.Yellow => "bg-yellow-100 text-yellow-800 border-yellow-200 dark:bg-yellow-900/30 dark:text-yellow-300 dark:border-yellow-800",
        UiColor.Green  => "bg-green-100 text-green-700 border-green-200 dark:bg-green-900/30 dark:text-green-300 dark:border-green-800",
        UiColor.Teal   => "bg-teal-100 text-teal-700 border-teal-200 dark:bg-teal-900/30 dark:text-teal-300 dark:border-teal-800",
        UiColor.Cyan   => "bg-cyan-100 text-cyan-800 border-cyan-200 dark:bg-cyan-900/30 dark:text-cyan-300 dark:border-cyan-800",
        UiColor.Gray   => "bg-gray-100 text-gray-700 border-gray-200 dark:bg-gray-900/30 dark:text-gray-300 dark:border-gray-800",
        UiColor.Slate  => "bg-slate-100 text-slate-700 border-slate-200 dark:bg-slate-800 dark:text-slate-300 dark:border-slate-700",
        UiColor.Black  => "bg-gray-200 text-gray-900 border-gray-300 dark:bg-gray-900/40 dark:text-gray-100 dark:border-gray-800",
        UiColor.White  => "bg-white text-gray-700 border-gray-200 dark:bg-gray-800 dark:text-gray-100 dark:border-gray-700",
        _ => throw new ArgumentOutOfRangeException(nameof(color), color, null)
    };

    /// <summary>Classes Tailwind pour le background d’un icône (chip icône).</summary>
    public static string GetIconClasseBg(this UiColor color) => color switch
    {
        UiColor.Blue   => "bg-blue-50 dark:bg-blue-900/10 text-blue-600",
        UiColor.Indigo => "bg-indigo-50 dark:bg-indigo-900/10 text-indigo-600",
        UiColor.Purple => "bg-purple-50 dark:bg-purple-900/10 text-purple-600",
        UiColor.Pink   => "bg-pink-50 dark:bg-pink-900/10 text-pink-600",
        UiColor.Red    => "bg-red-50 dark:bg-red-900/10 text-red-600",
        UiColor.Orange => "bg-orange-50 dark:bg-orange-900/10 text-orange-600",
        UiColor.Yellow => "bg-yellow-50 dark:bg-yellow-900/10 text-yellow-600",
        UiColor.Green  => "bg-green-50 dark:bg-green-900/10 text-green-600",
        UiColor.Teal   => "bg-teal-50 dark:bg-teal-900/10 text-teal-600",
        UiColor.Cyan   => "bg-cyan-50 dark:bg-cyan-900/10 text-cyan-600",
        UiColor.Gray   => "bg-gray-50 dark:bg-gray-900/10 text-gray-600",
        UiColor.Slate  => "bg-slate-50 dark:bg-slate-900/10 text-slate-600",
        UiColor.Black  => "bg-gray-200 dark:bg-gray-900/10 text-gray-900",
        UiColor.White  => "bg-white dark:bg-gray-800 text-gray-600",
        _ => throw new ArgumentOutOfRangeException(nameof(color), color, null)
    };

    /// <summary>Retourne les classes Tailwind CSS pour un badge foncé (solid).</summary>
    public static string GetSolidBadgeClasses(this UiColor color) => color switch
    {
        UiColor.Blue   => "bg-blue-600 text-white border-blue-700 dark:bg-blue-700 dark:border-blue-800",
        UiColor.Indigo => "bg-indigo-600 text-white border-indigo-700 dark:bg-indigo-700 dark:border-indigo-800", // correction typo
        UiColor.Purple => "bg-purple-600 text-white border-purple-700 dark:bg-purple-700 dark:border-purple-800",
        UiColor.Pink   => "bg-pink-600 text-white border-pink-700 dark:bg-pink-700 dark:border-pink-800",
        UiColor.Red    => "bg-red-600 text-white border-red-700 dark:bg-red-700 dark:border-red-800",
        UiColor.Orange => "bg-orange-600 text-white border-orange-700 dark:bg-orange-700 dark:border-orange-800",
        UiColor.Yellow => "bg-yellow-500 text-white border-yellow-600 dark:bg-yellow-600 dark:border-yellow-700",
        UiColor.Green  => "bg-green-600 text-white border-green-700 dark:bg-green-700 dark:border-green-800",
        UiColor.Teal   => "bg-teal-600 text-white border-teal-700 dark:bg-teal-700 dark:border-teal-800",
        UiColor.Cyan   => "bg-cyan-600 text-white border-cyan-700 dark:bg-cyan-700 dark:border-cyan-800",
        UiColor.Gray   => "bg-gray-600 text-white border-gray-700 dark:bg-gray-700 dark:border-gray-800",
        UiColor.Slate  => "bg-slate-600 text-white border-slate-700 dark:bg-slate-700 dark:border-slate-800",
        UiColor.Black  => "bg-gray-900 text-white border-gray-900 dark:bg-black dark:border-black",
        UiColor.White  => "bg-white text-gray-800 border-gray-300 dark:bg-gray-100 dark:text-gray-900 dark:border-gray-300",
        _ => throw new ArgumentOutOfRangeException(nameof(color), color, null)
    };

    /// <summary>Label (FR) comme votre PHP.</summary>
    public static string GetLabel(this UiColor color) => color switch
    {
        UiColor.Blue => "Bleu",
        UiColor.Indigo => "Indigo",
        UiColor.Purple => "Violet",
        UiColor.Pink => "Rose",
        UiColor.Red => "Rouge",
        UiColor.Orange => "Orange",
        UiColor.Yellow => "Jaune",
        UiColor.Green => "Vert",
        UiColor.Teal => "Sarcelle",
        UiColor.Cyan => "Cyan",
        UiColor.Gray => "Gris",
        UiColor.Slate => "Ardoise",
        UiColor.Black => "Black",
        UiColor.White => "White",
        _ => throw new ArgumentOutOfRangeException(nameof(color), color, null)
    };
}
