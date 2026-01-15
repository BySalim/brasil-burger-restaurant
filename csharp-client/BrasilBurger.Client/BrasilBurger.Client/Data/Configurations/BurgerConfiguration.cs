using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;
using BrasilBurger.Client.Models.Entities;

namespace BrasilBurger.Client.Data.Configurations;

public class BurgerConfiguration : IEntityTypeConfiguration<Burger>
{
    public void Configure(EntityTypeBuilder<Burger> builder)
    {
        builder.HasKey(b => b.Id);

        builder.Property(b => b.Name)
            .IsRequired()
            .HasMaxLength(256);

        builder.Property(b => b.Description)
            .IsRequired();

        builder.Property(b => b.Price)
            .HasPrecision(10, 2);

        builder.Property(b => b.ImageUrl);

        builder.Property(b => b.Available)
            .HasDefaultValue(true);
    }
}
