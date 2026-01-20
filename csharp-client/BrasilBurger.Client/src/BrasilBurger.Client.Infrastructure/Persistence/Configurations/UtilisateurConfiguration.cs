using BrasilBurger.Client.Domain.Entities;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;

namespace BrasilBurger.Client.Infrastructure.Persistence.Configurations;

public sealed class UtilisateurConfiguration : IEntityTypeConfiguration<Utilisateur>
{
    public void Configure(EntityTypeBuilder<Utilisateur> builder)
    {
        builder.ToTable("utilisateur");
        builder.HasKey(x => x.Id);

        builder.Property(x => x.Id).HasColumnName("id").ValueGeneratedOnAdd();

        builder.Property(x => x.Nom).HasColumnName("nom").HasMaxLength(100).IsRequired();
        builder.Property(x => x.Prenom).HasColumnName("prenom").HasMaxLength(100).IsRequired();

        builder.Property(x => x.Login).HasColumnName("login").HasMaxLength(100).IsRequired();
        builder.HasIndex(x => x.Login).IsUnique();

        builder.Property(x => x.MotDePasse).HasColumnName("mot_de_passe").HasMaxLength(255).IsRequired();

        builder.Property(x => x.Telephone).HasColumnName("telephone").HasMaxLength(20);
        builder.HasIndex(x => x.Telephone).IsUnique();

        builder.Property(x => x.QuartierLivraisonDefautId).HasColumnName("id_quartier_livraison_defaut");

        builder.HasOne(x => x.QuartierLivraisonDefaut)
            .WithMany()
            .HasForeignKey(x => x.QuartierLivraisonDefautId)
            .OnDelete(DeleteBehavior.SetNull);

        builder.Property(x => x.ModePaiementDefaut)
            .HasColumnName("mode_paiement_defaut")
            .HasMaxLength(20)
            .HasConversion<string>();

        builder.Property(x => x.ModeRecuperationDefaut)
            .HasColumnName("mode_recuperation_defaut")
            .HasMaxLength(20)
            .HasConversion<string>();

        builder.HasDiscriminator<string>("role")
            .HasValue<Domain.Entities.Client>("CLIENT")
            .HasValue<Domain.Entities.Gestionnaire>("GESTIONNAIRE");

        builder.Property<string>("role")
            .HasColumnName("role")
            .HasMaxLength(20)
            .IsRequired();
    }
}
