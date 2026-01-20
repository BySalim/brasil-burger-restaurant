using BrasilBurger.Client.Domain.Entities;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;

namespace BrasilBurger.Client.Infrastructure.Persistence.Configurations;


public sealed class LivraisonConfiguration : IEntityTypeConfiguration<Livraison>
{
    public void Configure(EntityTypeBuilder<Livraison> builder)
    {
        builder.ToTable("livraison");
        builder.HasKey(x => x.Id);

        builder.Property(x => x.Id).HasColumnName("id").ValueGeneratedOnAdd();

        builder.Property(x => x.Statut)
            .HasColumnName("statut")
            .HasMaxLength(20)
            .HasConversion<string>()
            .IsRequired();

        builder.Property(x => x.DateDebut).HasColumnName("date_debut").IsRequired();
        builder.Property(x => x.DateFin).HasColumnName("date_fin");

        builder.Property(x => x.GroupeLivraisonId).HasColumnName("id_groupe_livraison").IsRequired();

        builder.HasOne(x => x.GroupeLivraison)
            .WithMany(g => g.Livraisons)
            .HasForeignKey(x => x.GroupeLivraisonId)
            .OnDelete(DeleteBehavior.Cascade);
    }
}
