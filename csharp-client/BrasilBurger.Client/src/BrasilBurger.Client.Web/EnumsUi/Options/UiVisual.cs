namespace BrasilBurger.Client.Web.EnumsUi.Options;

public abstract record UiVisual;

public sealed record UiIcon(string IconName) : UiVisual;

public sealed record UiImage(string Path) : UiVisual;
