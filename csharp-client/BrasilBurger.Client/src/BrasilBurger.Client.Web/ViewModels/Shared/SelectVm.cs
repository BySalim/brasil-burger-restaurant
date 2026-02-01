namespace BrasilBurger.Client.Web.ViewModels.Shared;

public class SelectVm
{
    public required string Name { get; init; }
    
    public string? Label { get; init; }
    public string? Placeholder { get; init; }
    
    public required IReadOnlyList<SelectOptionVm> Options { get; init; }
    
    public string? SelectedValue { get; set; }
    
    public bool AutoSubmit { get; init; } = false;
    public bool Required { get; init; } = false;
    public bool Disabled { get; init; } = false;
    
    public string? CustomCss { get; init; }
    public string? IconName { get; init; }
    
    public string Size { get; init; } = "md";

    public string CssClasses => CustomCss ?? BuildDefaultCss();
    
    private string BuildDefaultCss()
    {
        var sizeClass = Size switch
        {
            "sm" => "px-2 py-1.5 text-xs",
            "md" => "px-3 py-2.5 text-sm",
            "lg" => "px-4 py-3 text-base",
            _ => "px-3 py-2.5 text-sm"
        };
        
        var paddingRight = !string.IsNullOrEmpty(IconName) ? "pr-8" : "pr-8";
        
        return $"w-full {paddingRight} {sizeClass} bg-white dark:bg-surface-dark border border-gray-200 dark:border-border-dark rounded-lg text-gray-600 dark:text-gray-300 focus:ring-2 focus:ring-primary/50 focus:border-primary transition-all duration-200 cursor-pointer appearance-none";
    }
}