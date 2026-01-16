namespace BrasilBurger.Client.Infrastructure.Diagnostics;

public interface IDatabaseProbe
{
    Task<ProbeResult> TestAsync(CancellationToken ct);
}
