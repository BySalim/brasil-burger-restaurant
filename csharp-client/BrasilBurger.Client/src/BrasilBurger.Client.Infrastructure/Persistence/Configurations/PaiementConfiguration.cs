using BrasilBurger.Client.Domain.Entities;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;

namespace BrasilBurger.Client.Infrastructure.Persistence.Configurations;


public sealed class PaiementConfiguration : IEntityTypeConfiguration<Paiement>
{
    public void Configure(EntityTypeBuilder<Paiement> builder)
    {
        builder.ToTable("paiement");
        builder.HasKey(x => x.Id);

        builder.Property(x => x.Id).HasColumnName("id").ValueGeneratedOnAdd();

        builder.Property(x => x.DatePaie).HasColumnName("date_paie").IsRequired();
        builder.Property(x => x.MontantPaie).HasColumnName("montant_paie").IsRequired();

        builder.Property(x => x.ModePaie)
            .HasColumnName("mode_paie")
            .HasMaxLength(20)
            .HasConversion<string>()
            .IsRequired();

        builder.Property(x => x.ReferencePaiementExterne)
            .HasColumnName("reference_paiement_externe")
            .HasMaxLength(100);

        builder.HasIndex(x => x.ReferencePaiementExterne).IsUnique();

        builder.Property(x => x.Test).HasColumnName("test").HasDefaultValue(false).IsRequired();

        builder.Property(x => x.ClientId).HasColumnName("id_client");
        builder.Property(x => x.CommandeId).HasColumnName("id_commande").IsRequired();

        builder.HasIndex(x => x.CommandeId).IsUnique();

        builder.HasOne(x => x.Client)
            .WithMany()
            .HasForeignKey(x => x.ClientId)
            .OnDelete(DeleteBehavior.SetNull);

        builder.HasOne(x => x.Commande)
            .WithOne(c => c.Paiement)
            .HasForeignKey<Paiement>(x => x.CommandeId)
            .OnDelete(DeleteBehavior.Cascade);
    }
}
