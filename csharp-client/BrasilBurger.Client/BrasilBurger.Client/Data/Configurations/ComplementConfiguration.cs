using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;
using BrasilBurger.Client.Models.Entities;

namespace BrasilBurger.Client.Data.Configurations;

public class ComplementConfiguration : IEntityTypeConfiguration<Complement>
{
    public void Configure(EntityTypeBuilder<Complement> builder)
    {
        builder.HasKey(c => c.Id);

        builder.Property(c => c.Name)
            .IsRequired()
            .HasMaxLength(256);

        builder.Property(c => c.Price)
            .HasPrecision(10, 2);

        builder.Property(c => c.ImageUrl);

        builder.Property(c => c.Available)
            .HasDefaultValue(true);
    }
}
