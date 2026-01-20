using BrasilBurger.Client.Domain.Entities;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;

namespace BrasilBurger.Client.Infrastructure.Persistence.Configurations;

public sealed class ArticleQuantifierConfiguration : IEntityTypeConfiguration<ArticleQuantifier>
{
    public void Configure(EntityTypeBuilder<ArticleQuantifier> builder)
    {
        builder.ToTable("article_quantifier");
        builder.HasKey(x => x.Id);

        builder.Property(x => x.Id).HasColumnName("id").ValueGeneratedOnAdd();

        builder.Property(x => x.Quantite)
            .HasColumnName("quantite")
            .HasDefaultValue(1)
            .IsRequired();

        builder.Property(x => x.Montant)
            .HasColumnName("montant")
            .IsRequired();

        builder.Property(x => x.CategorieArticleQuantifier)
            .HasColumnName("categorie_article_quantifier")
            .HasMaxLength(20)
            .HasConversion<string>()
            .IsRequired();

        builder.Property(x => x.MenuId).HasColumnName("id_menu");
        builder.Property(x => x.PanierId).HasColumnName("id_panier");
        builder.Property(x => x.ArticleId).HasColumnName("id_article");

        builder.HasOne(x => x.Article)
            .WithMany()
            .HasForeignKey(x => x.ArticleId)
            .OnDelete(DeleteBehavior.Cascade);

        builder.HasOne(x => x.Menu)
            .WithMany(m => m.MenuComposition)
            .HasForeignKey(x => x.MenuId)
            .OnDelete(DeleteBehavior.Cascade);

        builder.HasOne(x => x.Panier)
            .WithMany(p => p.Lignes)
            .HasForeignKey(x => x.PanierId)
            .OnDelete(DeleteBehavior.Cascade);
    }
}
