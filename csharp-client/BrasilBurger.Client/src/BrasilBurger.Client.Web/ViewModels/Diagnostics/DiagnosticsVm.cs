namespace BrasilBurger.Client.Web.ViewModels.Diagnostics;

public sealed class DiagnosticsVm
{
    public bool DbOk { get; init; }
    public string DbMessage { get; init; } = "";

    public bool CloudinaryOk { get; init; }
    public string CloudinaryMessage { get; init; } = "";
}
