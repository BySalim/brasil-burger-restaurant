using BrasilBurger.Client.Domain.Entities;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;

namespace BrasilBurger.Client.Infrastructure.Persistence.Configurations;


public sealed class QuartierConfiguration : IEntityTypeConfiguration<Quartier>
{
    public void Configure(EntityTypeBuilder<Quartier> builder)
    {
        builder.ToTable("quartier");
        builder.HasKey(x => x.Id);

        builder.Property(x => x.Id).HasColumnName("id").ValueGeneratedOnAdd();

        builder.Property(x => x.Nom).HasColumnName("nom").HasMaxLength(100).IsRequired();
        builder.HasIndex(x => x.Nom).IsUnique();

        builder.Property(x => x.ZoneId).HasColumnName("id_zone").IsRequired();

        builder.HasOne(x => x.Zone)
            .WithMany(z => z.Quartiers)
            .HasForeignKey(x => x.ZoneId)
            .OnDelete(DeleteBehavior.Cascade);
    }
}