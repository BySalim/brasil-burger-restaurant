using BrasilBurger.Client.Domain.Entities;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;

namespace BrasilBurger.Client.Infrastructure.Persistence.Configurations;

public sealed class LivreurConfiguration : IEntityTypeConfiguration<Livreur>
{
    public void Configure(EntityTypeBuilder<Livreur> builder)
    {
        builder.ToTable("livreur");
        builder.HasKey(x => x.Id);

        builder.Property(x => x.Id).HasColumnName("id").ValueGeneratedOnAdd();

        builder.Property(x => x.Nom).HasColumnName("nom").HasMaxLength(100).IsRequired();
        builder.Property(x => x.Prenom).HasColumnName("prenom").HasMaxLength(100).IsRequired();

        builder.Property(x => x.Telephone).HasColumnName("telephone").HasMaxLength(20).IsRequired();
        builder.HasIndex(x => x.Telephone).IsUnique();

        builder.Property(x => x.EstArchiver).HasColumnName("est_archiver").HasDefaultValue(false);
        builder.Property(x => x.EstDisponible).HasColumnName("est_disponible").HasDefaultValue(true);
    }
}