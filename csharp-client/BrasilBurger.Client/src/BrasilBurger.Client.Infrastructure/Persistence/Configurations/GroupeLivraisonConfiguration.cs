using BrasilBurger.Client.Domain.Entities;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;

namespace BrasilBurger.Client.Infrastructure.Persistence.Configurations;

public sealed class GroupeLivraisonConfiguration : IEntityTypeConfiguration<GroupeLivraison>
{
    public void Configure(EntityTypeBuilder<GroupeLivraison> builder)
    {
        builder.ToTable("groupe_livraison");
        builder.HasKey(x => x.Id);

        builder.Property(x => x.Id).HasColumnName("id").ValueGeneratedOnAdd();

        builder.Property(x => x.LivreurId).HasColumnName("id_livreur").IsRequired();

        builder.Property(x => x.Statut)
            .HasColumnName("statut")
            .HasMaxLength(20)
            .HasConversion<string>()
            .IsRequired();

        builder.HasOne(x => x.Livreur)
            .WithMany()
            .HasForeignKey(x => x.LivreurId)
            .OnDelete(DeleteBehavior.Cascade);

        builder.Navigation(x => x.Livraisons)
            .HasField("_livraisons")
            .UsePropertyAccessMode(PropertyAccessMode.Field);
    }
}