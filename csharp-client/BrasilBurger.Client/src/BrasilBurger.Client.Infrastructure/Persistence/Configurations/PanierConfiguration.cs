using BrasilBurger.Client.Domain.Entities;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;

namespace BrasilBurger.Client.Infrastructure.Persistence.Configurations;

public sealed class PanierConfiguration : IEntityTypeConfiguration<Panier>
{
    public void Configure(EntityTypeBuilder<Panier> builder)
    {
        builder.ToTable("panier");
        builder.HasKey(x => x.Id);

        builder.Property(x => x.Id).HasColumnName("id").ValueGeneratedOnAdd();

        builder.Property(x => x.MontantTotal)
            .HasColumnName("montant_total")
            .HasDefaultValue(0)
            .IsRequired();

        builder.Property(x => x.CategoriePanier)
            .HasColumnName("categorie_panier")
            .HasMaxLength(20)
            .HasConversion<string>()
            .IsRequired();

        builder.Navigation(x => x.Lignes)
            .HasField("_lignes")
            .UsePropertyAccessMode(PropertyAccessMode.Field);
    }
}
