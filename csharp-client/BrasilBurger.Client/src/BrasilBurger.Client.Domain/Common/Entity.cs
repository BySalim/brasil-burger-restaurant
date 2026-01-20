namespace BrasilBurger.Client.Domain.Common;

public abstract class Entity
{
    public int Id { get; protected set; }

    public bool IsTransient => Id == default;

    public override bool Equals(object? obj)
    {
        if (obj is not Entity other) return false;
        if (ReferenceEquals(this, other)) return true;
        if (GetType() != other.GetType()) return false;
        if (IsTransient || other.IsTransient) return false;
        return Id == other.Id;
    }

    public override int GetHashCode()
        => (GetType().ToString() + Id).GetHashCode();

    public static bool operator ==(Entity? a, Entity? b) => a?.Equals(b) ?? b is null;
    public static bool operator !=(Entity? a, Entity? b) => !(a == b);
}
