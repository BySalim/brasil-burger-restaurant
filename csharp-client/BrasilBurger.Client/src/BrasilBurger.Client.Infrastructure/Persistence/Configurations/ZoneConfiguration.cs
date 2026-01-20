using BrasilBurger.Client.Domain.Entities;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;

namespace BrasilBurger.Client.Infrastructure.Persistence.Configurations;

public sealed class ZoneConfiguration : IEntityTypeConfiguration<Zone>
{
    public void Configure(EntityTypeBuilder<Zone> builder)
    {
        builder.ToTable("zone");
        builder.HasKey(x => x.Id);

        builder.Property(x => x.Id).HasColumnName("id").ValueGeneratedOnAdd();

        builder.Property(x => x.Nom).HasColumnName("nom").HasMaxLength(100).IsRequired();
        builder.HasIndex(x => x.Nom).IsUnique();

        builder.Property(x => x.PrixLivraison).HasColumnName("prix_livraison").IsRequired();
        builder.Property(x => x.EstArchiver).HasColumnName("est_archiver").HasDefaultValue(false);

        builder.Navigation(x => x.Quartiers)
            .HasField("_quartiers")
            .UsePropertyAccessMode(PropertyAccessMode.Field);
    }
}


