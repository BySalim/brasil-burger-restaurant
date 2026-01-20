using BrasilBurger.Client.Domain.Entities;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;

namespace BrasilBurger.Client.Infrastructure.Persistence.Configurations;


public sealed class InfoLivraisonConfiguration : IEntityTypeConfiguration<InfoLivraison>
{
    public void Configure(EntityTypeBuilder<InfoLivraison> builder)
    {
        builder.ToTable("info_livraison");
        builder.HasKey(x => x.Id);

        builder.Property(x => x.Id).HasColumnName("id").ValueGeneratedOnAdd();

        builder.Property(x => x.IdZone).HasColumnName("id_zone").IsRequired();
        builder.Property(x => x.IdQuartier).HasColumnName("id_quartier").IsRequired();

        builder.Property(x => x.NoteLivraison).HasColumnName("note_livraison");
        builder.Property(x => x.PrixLivraison).HasColumnName("prix_livraison").IsRequired();

        builder.HasOne(x => x.Zone)
            .WithMany()
            .HasForeignKey(x => x.IdZone)
            .OnDelete(DeleteBehavior.Cascade);

        builder.HasOne(x => x.Quartier)
            .WithMany()
            .HasForeignKey(x => x.IdQuartier)
            .OnDelete(DeleteBehavior.Cascade);
    }
}
