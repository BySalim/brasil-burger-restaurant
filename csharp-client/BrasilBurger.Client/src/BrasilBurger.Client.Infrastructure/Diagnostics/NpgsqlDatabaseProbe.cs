using Npgsql;

namespace BrasilBurger.Client.Infrastructure.Diagnostics;

public sealed class NpgsqlDatabaseProbe : IDatabaseProbe
{
    private readonly string _connectionString;

    public NpgsqlDatabaseProbe(string connectionString)
    {
        _connectionString = connectionString;
    }

    public async Task<ProbeResult> TestAsync(CancellationToken ct)
    {
        if (string.IsNullOrWhiteSpace(_connectionString))
            return new ProbeResult(false, "Connection string Neon manquante (ConnectionStrings:Neon).");

        try
        {
            await using var conn = new NpgsqlConnection(_connectionString);
            await conn.OpenAsync(ct);

            await using var cmd = new NpgsqlCommand("SELECT 1;", conn);
            var result = await cmd.ExecuteScalarAsync(ct);

            return new ProbeResult(true, $"Neon OK (SELECT 1 => {result}).");
        }
        catch (Exception ex)
        {
            return new ProbeResult(false, $"Neon KO: {ex.GetType().Name} - {ex.Message}");
        }
    }
}
