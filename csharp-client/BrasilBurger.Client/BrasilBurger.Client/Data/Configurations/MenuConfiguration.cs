using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;
using BrasilBurger.Client.Models.Entities;

namespace BrasilBurger.Client.Data.Configurations;

public class MenuConfiguration : IEntityTypeConfiguration<Menu>
{
    public void Configure(EntityTypeBuilder<Menu> builder)
    {
        builder.HasKey(m => m.Id);

        builder.Property(m => m.Name)
            .IsRequired()
            .HasMaxLength(256);

        builder.Property(m => m.Description)
            .IsRequired();

        builder.Property(m => m.Price)
            .HasPrecision(10, 2);

        builder.Property(m => m.ImageUrl);

        builder.Property(m => m.Available)
            .HasDefaultValue(true);
    }
}
