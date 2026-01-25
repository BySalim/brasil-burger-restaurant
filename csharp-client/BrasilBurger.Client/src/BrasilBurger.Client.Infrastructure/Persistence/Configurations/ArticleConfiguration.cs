using BrasilBurger.Client.Domain.Entities;
using BrasilBurger.Client.Domain.Enums;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;

namespace BrasilBurger.Client.Infrastructure.Persistence.Configurations;

public sealed class ArticleConfiguration : IEntityTypeConfiguration<Article>
{
    public void Configure(EntityTypeBuilder<Article> builder)
    {
        builder.ToTable("article");
        builder.HasKey(x => x.Id);

        builder.Property(x => x.Id).HasColumnName("id").ValueGeneratedOnAdd();

        builder.Property(x => x.Code)
            .HasColumnName("code")
            .HasMaxLength(50)
            .IsRequired();
        builder.HasIndex(x => x.Code).IsUnique();

        builder.Property(x => x.Libelle)
            .HasColumnName("libelle")
            .HasMaxLength(150)
            .IsRequired();

        builder.Property(x => x.ImagePublicId)
            .HasColumnName("image_public_id")
            .IsRequired();

        builder.Property(x => x.EstArchiver)
            .HasColumnName("est_archiver")
            .HasDefaultValue(false);

        builder.Property(x => x.Description)
            .HasColumnName("description");

        builder.Property(x => x.Prix)
            .HasColumnName("prix");

        builder.Property(x => x.TypeComplement)
            .HasColumnName("type_complement")
            .HasMaxLength(20)
            .HasConversion<string>(); // enum -> string

        builder.HasDiscriminator<string>("categorie")
            .HasValue<Burger>("BURGER")
            .HasValue<Menu>("MENU")
            .HasValue<Complement>("COMPLEMENT");

        builder.Property<string>("categorie")
            .HasColumnName("categorie")
            .HasMaxLength(20)
            .IsRequired();
    }
}
