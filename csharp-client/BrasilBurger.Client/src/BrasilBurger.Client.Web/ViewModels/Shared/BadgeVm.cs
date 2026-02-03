using BrasilBurger.Client.Web.EnumsUi.Options;
namespace BrasilBurger.Client.Web.ViewModels.Shared;

public class BadgeVm
{
    public required string Label { get; init; }
    
    public string? IconName { get; init; }
    public string? IconUrl { get; init; }
    
    public BadgeVariant Variant { get; init; } = BadgeVariant.Soft;
    public UiColor Color { get; init; } = UiColor.Bleu;
    
    public string? CustomCss { get; init; }
    public string? IconCss { get; init; }
    
    public string Size { get; init; } = "md";
    
    public string CssClasses => CustomCss ?? BuildDefaultCss();
    public string IconCssClasses => IconCss ?? "text-[14px]";
    public string ImageCssClasses => BuildImageCss();
    
    private string BuildDefaultCss()
    {
        var sizeClass = Size switch
        {
            "xs" => "px-2 py-0.5 text-[10px]",
            "sm" => "px-2.5 py-1 text-xs",
            "md" => "px-3 py-1 text-xs",
            "lg" => "px-3.5 py-1.5 text-sm",
            _ => "px-3 py-1 text-xs"
        };
        
        var shapeClass = Variant == BadgeVariant.PillSoft || Variant == BadgeVariant.Outline 
            ? "rounded-full" 
            : "rounded";
            
        var baseClass = "inline-flex items-center font-bold tracking-wide gap-1.5";
        var variantClass = Variant.Css(Color);
        
        return $"{baseClass} {sizeClass} {shapeClass} border {variantClass}";
    }
    
    private string BuildImageCss()
    {
        var imageHeight = Size switch
        {
            "xs" => "h-3",
            "sm" => "h-3.5",
            "md" => "h-4",
            "lg" => "h-5",
            _ => "h-4"
        };
        
        return $"{imageHeight} w-auto object-contain";
    }
}