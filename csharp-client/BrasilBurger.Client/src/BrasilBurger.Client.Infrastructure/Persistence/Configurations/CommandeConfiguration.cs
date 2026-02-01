using BrasilBurger.Client.Domain.Entities;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;

namespace BrasilBurger.Client.Infrastructure.Persistence.Configurations;

public sealed class CommandeConfiguration : IEntityTypeConfiguration<Commande>
{
    public void Configure(EntityTypeBuilder<Commande> builder)
    {
        builder.ToTable("commande");
        builder.HasKey(x => x.Id);

        builder.Property(x => x.Id).HasColumnName("id").ValueGeneratedOnAdd();

        builder.Property(x => x.NumCmd).HasColumnName("num_cmd").HasMaxLength(50).IsRequired();
        builder.HasIndex(x => x.NumCmd).IsUnique();

        builder.Property(x => x.DateDebut).HasColumnName("date_debut").IsRequired();
        builder.Property(x => x.DateFin).HasColumnName("date_fin");

        builder.Property(x => x.Montant).HasColumnName("montant").IsRequired();

        builder.Property(x => x.Etat)
            .HasColumnName("etat")
            .HasMaxLength(20)
            .HasConversion<string>()
            .IsRequired();

        builder.Property(x => x.TypeRecuperation)
            .HasColumnName("type_recuperation")
            .HasMaxLength(20)
            .HasConversion<string>()
            .IsRequired();

        builder.Property(x => x.PanierId).HasColumnName("id_panier");
        builder.Property(x => x.LivraisonId).HasColumnName("id_livraison");

        builder.Property(x => x.ClientId).HasColumnName("id_client").IsRequired();
        builder.Property(x => x.InfoLivraisonId).HasColumnName("id_info_livraison");

        builder.HasIndex(x => x.InfoLivraisonId).IsUnique();
        builder.HasIndex(x => x.PanierId).IsUnique();
        builder.HasIndex(x => x.LivraisonId).IsUnique();

        builder.HasOne(x => x.Client)
            .WithMany()
            .HasForeignKey(x => x.ClientId)
            .OnDelete(DeleteBehavior.Cascade);

        builder.HasOne(x => x.InfoLivraison)
            .WithMany()
            .HasForeignKey(x => x.InfoLivraisonId)
            .OnDelete(DeleteBehavior.Cascade);

        builder.HasOne(x => x.Livraison)
            .WithMany()
            .HasForeignKey(x => x.LivraisonId)
            .OnDelete(DeleteBehavior.SetNull);

        builder.HasOne(x => x.Panier)
            .WithMany()
            .HasForeignKey(x => x.PanierId)
            .OnDelete(DeleteBehavior.SetNull);

        builder.HasOne(x => x.Paiement)
            .WithOne(p => p.Commande)
            .HasForeignKey<Paiement>(p => p.CommandeId)
            .OnDelete(DeleteBehavior.Cascade);
    }
}
