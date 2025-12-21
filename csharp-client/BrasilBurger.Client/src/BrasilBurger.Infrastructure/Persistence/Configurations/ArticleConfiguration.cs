using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;
using BrasilBurger.Domain.Entities;

namespace BrasilBurger.Infrastructure.Persistence.Configurations;

public class ArticleConfiguration : IEntityTypeConfiguration<Article>
{
    public void Configure(EntityTypeBuilder<Article> builder)
    {
        builder.HasKey(a => a.Id);
        builder.Property(a => a.Code).IsRequired().HasMaxLength(50).IsUnicode();
        builder.Property(a => a.Libelle).IsRequired().HasMaxLength(150);
        builder.Property(a => a.ImagePublicId).IsRequired();
        builder.Property(a => a.Categorie).IsRequired();
        builder.Property(a => a.Description).HasMaxLength(500);
        builder.Property(a => a.EstArchiver).HasDefaultValue(false);
        builder.HasIndex(a => a.Code).IsUnique();
    }
}