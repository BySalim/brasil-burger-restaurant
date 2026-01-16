namespace BrasilBurger.Client.Infrastructure.Diagnostics;

public interface ICloudinaryProbe
{
    Task<ProbeResult> TestAsync(CancellationToken ct);
}
